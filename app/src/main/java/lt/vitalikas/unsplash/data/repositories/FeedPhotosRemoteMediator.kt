package lt.vitalikas.unsplash.data.repositories

import android.icu.util.Calendar
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import lt.vitalikas.unsplash.data.api.UnsplashApi
import lt.vitalikas.unsplash.data.db.Database
import lt.vitalikas.unsplash.data.db.entities.*
import lt.vitalikas.unsplash.data.db.mappers.PhotoToPhotoEntityMapper
import okio.IOException
import retrofit2.HttpException
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class FeedPhotosRemoteMediator @Inject constructor(
    private val api: UnsplashApi,
    private val order: String,
    private val currentOrder: String
) : RemoteMediator<Int, PhotoEntity>() {

    private val feedPhotosDao = Database.instance.photosDao()
    private val feedUserDao = Database.instance.userDao()
    private val feedUserProfileImageDao = Database.instance.userProfileImageDao()
    private val feedUserLinkDao = Database.instance.userLinkDao()
    private val feedUrlDao = Database.instance.urlDao()
    private val feedLinkDao = Database.instance.linkDao()
    private val feedCollectionDao = Database.instance.feedCollectionDao()
    private val remoteKeysDao = Database.instance.remoteKeysDao()

    override suspend fun initialize(): InitializeAction =
        if (feedPhotosDao.getFeedPhotoCount() > 0) {
            val timestamp = Calendar.getInstance().time.time
            val outdated = feedPhotosDao.outdated(timestamp, CACHE_TIMEOUT)
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
                remoteKey?.nextKey?.minus(1) ?: STARTING_PAGE_INDEX
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
            val photos = api.getFeedPhotos(
                page,
                ITEMS_PER_PAGE,
                order
            )
            val endOfPaginationReached = photos.isEmpty()

            Database.instance.withTransaction {
                // clear all tables in the database
                if (loadType == LoadType.REFRESH) {
                    remoteKeysDao.deleteAllRemoteKeys()
                    feedPhotosDao.deleteAllFeedPhotos()
                }

                val prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = photos.map { feedPhoto ->
                    RemoteKey(
                        feedPhotoId = feedPhoto.id,
                        prevKey = prevKey,
                        nextKey = nextKey
                    )
                }
                remoteKeysDao.insertAllKeys(keys)

                val feedPhotos = mutableListOf<PhotoEntity>()

                photos.map { photo ->

                    val photoEntity = PhotoToPhotoEntityMapper().map(photo)
                    feedPhotos.add(photoEntity)

                    val feedUser = UserEntity(
                        id = photo.user.id,
                        userProfileImageId = photo.user.id,
                        userLinkId = photo.user.id,
                        username = photo.user.username,
                        name = photo.user.name,
                        firstName = photo.user.firstName,
                        lastName = photo.user.lastName ?: "N/A",
                        portfolioUrl = photo.user.portfolioUrl,
                        bio = photo.user.bio,
                        location = photo.user.location,
                        totalLikes = photo.user.totalLikes,
                        totalPhotos = photo.user.totalPhotos,
                        totalCollections = photo.user.totalCollections,
                        instagramUsername = photo.user.instagramUsername,
                        twitterUsername = photo.user.twitterUsername
                    )
                    feedUserDao.insertUser(feedUser)

                    val userProfileImage = UserProfileImageEntity(
                        id = photo.user.id,
                        small = photo.user.profileImage.small,
                        medium = photo.user.profileImage.medium,
                        large = photo.user.profileImage.large
                    )
                    feedUserProfileImageDao.insertUserProfileImage(userProfileImage)

                    val userLink = UserLinkEntity(
                        id = photo.user.id,
                        self = photo.user.link.self,
                        html = photo.user.link.html,
                        photos = photo.user.link.photos,
                        likes = photo.user.link.likes,
                        portfolio = photo.user.link.portfolio,
                    )
                    feedUserLinkDao.insertUserLink(userLink)

                    val feedUrl = UrlEntity(
                        id = photo.user.id,
                        raw = photo.url.raw,
                        full = photo.url.full,
                        regular = photo.url.regular,
                        small = photo.url.small,
                        thumb = photo.url.thumb
                    )
                    feedUrlDao.insertFeedUrl(feedUrl)

                    val feedLink = LinkEntity(
                        id = photo.user.id,
                        self = photo.link.self,
                        html = photo.link.html,
                        download = photo.link.download,
                        downloadLocation = photo.link.downloadLocation
                    )
                    feedLinkDao.insertFeedLink(feedLink)
                }
                feedPhotosDao.insertAllFeedPhotos(feedPhotos)
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
            remoteKeysDao.getRemoteKeyByFeedPhotoId(feedPhoto.id)
        }

    // LoadType.APPEND
    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, PhotoEntity>): RemoteKey? =
        state.pages.lastOrNull { page ->
            page.data.isNotEmpty()
        }?.data?.lastOrNull()?.let { feedPhoto ->
            remoteKeysDao.getRemoteKeyByFeedPhotoId(feedPhoto.id)
        }

    // LoadType.REFRESH
    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, PhotoEntity>): RemoteKey? =
        state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                remoteKeysDao.getRemoteKeyByFeedPhotoId(id)
            }
        }

    companion object {
        private const val ITEMS_PER_PAGE = 10
        private const val STARTING_PAGE_INDEX = 1
        private val CACHE_TIMEOUT = TimeUnit.MILLISECONDS.convert(4, TimeUnit.HOURS)
    }
}