package lt.vitalikas.unsplash.data.repositories

import android.icu.util.Calendar
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import lt.vitalikas.unsplash.data.api.UnsplashApi
import lt.vitalikas.unsplash.data.db.Db
import lt.vitalikas.unsplash.data.db.entities.*
import lt.vitalikas.unsplash.data.db.mappers.UserToUserEntityMapper
import okio.IOException
import retrofit2.HttpException
import timber.log.Timber
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalPagingApi::class)
class PhotosRemoteMediator(
    private val api: UnsplashApi,
    private val order: String,
    private val currentOrder: String,
    private val db: Db
) : RemoteMediator<Int, PhotoEntity>() {

    private val photosDao = db.photosDao()
    private val remoteKeysDao = db.remoteKeysDao()

    override suspend fun initialize(): InitializeAction =
        if (photosDao.getPhotoCount() > 0) {
            val timestamp = Calendar.getInstance().time.time
            val outdated = photosDao.isAnyPhotoOutdated(timestamp, CACHE_TIMEOUT)
            Timber.d("OUTDATED DATA = $outdated")
            Timber.d("ORDER = $order")
            Timber.d("CURRENT ORDER = $currentOrder")
            if (outdated || order != currentOrder) {
                InitializeAction.LAUNCH_INITIAL_REFRESH
            } else {
                InitializeAction.SKIP_INITIAL_REFRESH
            }
        } else InitializeAction.LAUNCH_INITIAL_REFRESH

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PhotoEntity>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKey = getRemoteKeyClosestToCurrentPosition(state)
                val key = remoteKey?.nextKey?.minus(1) ?: STARTING_PAGE_INDEX
                key
            }
            LoadType.PREPEND -> {
                val remoteKey = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKey?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKey != null)
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKey = getRemoteKeyForLastItem(state)
                val nextKey = remoteKey?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKey != null)
                nextKey
            }
        }

        try {
            val photos = api.getPhotos(
                page,
                ITEMS_PER_PAGE,
                order
            )

            val endOfPaginationReached = photos.isEmpty()

            db.withTransaction {
                // clear all tables in the database
                if (loadType == LoadType.REFRESH) {
                    remoteKeysDao.deleteAllRemoteKeys()
                    photosDao.deleteAllPhotos()
                }

                val prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = photos.map { photo ->
                    RemoteKey(
                        feedPhotoId = photo.id,
                        prevKey = prevKey,
                        nextKey = nextKey
                    )
                }
                remoteKeysDao.insertAllKeys(keys)

                val photoEntities = mutableListOf<PhotoEntity>()
                photos.map { photo ->
                    /**
                     * Getting user.
                     */
                    val user = photo.user

                    /**
                     * Getting user profile image. Mapping it to entity for saving in database.
                     */
                    val userProfileImage = user.profileImage
                    val userProfileImageEntity = UserProfileImageEntity(
                        id = user.id,
                        small = userProfileImage.small,
                        medium = userProfileImage.medium,
                        large = userProfileImage.large
                    )
                    db.userProfileImageDao().insertUserProfileImage(userProfileImageEntity)

                    /**
                     * Getting user link. Mapping it to entity for saving in database.
                     */
                    val userLink = user.link
                    val userLinkEntity = UserLinkEntity(
                        id = user.id,
                        self = userLink.self,
                        html = userLink.html,
                        photos = userLink.photos,
                        likes = userLink.likes,
                        portfolio = userLink.portfolio,
                    )
                    db.userLinkDao().insertUserLink(userLinkEntity)

                    /**
                     * user mapping to entity and saving in database
                     */
                    val userEntity = UserToUserEntityMapper().map(user)
                    db.userDao().insertUser(userEntity)

                    /**
                     * Getting photo url. Mapping it to entity for saving in database.
                     */
                    val url = photo.url
                    val photoUrlEntity = UrlEntity(
                        id = photo.id,
                        raw = url.raw,
                        full = url.full,
                        regular = url.regular,
                        small = url.small,
                        thumb = url.thumb
                    )
                    db.urlDao().insertFeedUrl(photoUrlEntity)

                    /**
                     * Getting photo link. Mapping it to entity for saving in database.
                     */
                    val photoLink = photo.link
                    val feedLink = LinkEntity(
                        id = photo.id,
                        self = photoLink.self,
                        html = photoLink.html,
                        download = photoLink.download,
                        downloadLocation = photoLink.downloadLocation
                    )
                    db.linkDao().insertFeedLink(feedLink)

                    /**
                     * Mapping photo to entity and adding to list.
                     */
                    val photoEntity = PhotoEntity(
                        id = photo.id,
                        createdAt = photo.createdAt,
                        updatedAt = photo.updatedAt,
                        width = photo.width,
                        height = photo.height,
                        color = photo.color,
                        blurHash = photo.blurHash,
                        likes = photo.likes,
                        likedByUser = photo.likedByUser,
                        description = photo.description,
                        userId = user.id,
                        urlId = photo.id,
                        linkId = photo.id,
                        lastUpdatedAt = Calendar.getInstance()
                    )
                    photoEntities.add(photoEntity)
                }
                photosDao.insertAllPhotos(photoEntities)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: IOException) {
            return MediatorResult.Error(e)
        } catch (e: HttpException) {
            return MediatorResult.Error(e)
        }
    }

    // LoadType.PREPEND
    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, PhotoEntity>): RemoteKey? =
        state.pages.firstOrNull { page ->
            page.data.isNotEmpty()
        }?.data?.firstOrNull()?.let { feedPhoto ->
            remoteKeysDao.getRemoteKeyByPhotoId(feedPhoto.id)
        }

    // LoadType.APPEND
    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, PhotoEntity>): RemoteKey? =
        state.pages.lastOrNull { page ->
            page.data.isNotEmpty()
        }?.data?.lastOrNull()?.let { feedPhoto ->
            remoteKeysDao.getRemoteKeyByPhotoId(feedPhoto.id)
        }

    // LoadType.REFRESH
    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, PhotoEntity>): RemoteKey? =
        state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                remoteKeysDao.getRemoteKeyByPhotoId(id)
            }
        }

    companion object {
        private const val ITEMS_PER_PAGE = 10
        private const val STARTING_PAGE_INDEX = 1
        private val CACHE_TIMEOUT = TimeUnit.MILLISECONDS.convert(4, TimeUnit.HOURS)
    }
}