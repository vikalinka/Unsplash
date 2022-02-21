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
import okio.IOException
import retrofit2.HttpException
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class SearchFeedPhotosRemoteMediator @Inject constructor(
    private val query: String,
    private val api: UnsplashApi
) : RemoteMediator<Int, FeedPhotoEntity>() {
    init {
        Timber.d("REMOTE MEDIATOR INIT")
    }

    private val feedPhotosDao = Database.instance.feedPhotosDao()
    private val feedUserDao = Database.instance.feedUserDao()
    private val feedUserProfileImageDao = Database.instance.feedUserProfileImageDao()
    private val feedUserLinkDao = Database.instance.feedUserLinkDao()
    private val feedUrlDao = Database.instance.feedUrlDao()
    private val feedLinkDao = Database.instance.feedLinkDao()
    private val feedCollectionDao = Database.instance.feedCollectionDao()
    private val remoteKeysDao = Database.instance.remoteKeysDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, FeedPhotoEntity>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                Timber.d("REFRESH")
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
            Timber.d("PHOTOS SEARCH API")
            val photos = api.searchPhotos(
                query,
                page,
                ITEMS_PER_PAGE,
                ORDER_BY
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

                val feedPhotos = mutableListOf<FeedPhotoEntity>()
                var feedCollections: List<FeedCollectionEntity> = listOf()
                photos.map { feed ->

                    val userProfileImage = UserProfileImageEntity(
                        id = feed.user.id,
                        small = feed.user.imageUser.small,
                        medium = feed.user.imageUser.medium,
                        large = feed.user.imageUser.large
                    )
                    feedUserProfileImageDao.insertFeedUserProfileImage(userProfileImage)

                    val userLink = UserLinkEntity(
                        id = feed.user.id,
                        self = feed.user.links.self,
                        html = feed.user.links.html,
                        photos = feed.user.links.photos,
                        likes = feed.user.links.likes,
                        portfolio = feed.user.links.portfolio,
                    )
                    feedUserLinkDao.insertFeedUserLink(userLink)

                    val user = UserEntity(
                        id = feed.user.id,
                        userProfileImageId = feed.user.id,
                        userLinkId = feed.user.id,
                        username = feed.user.username,
                        name = feed.user.name,
                        portfolioUrl = feed.user.portfolioUrl,
                        bio = feed.user.bio,
                        location = feed.user.location,
                        totalLikes = feed.user.totalLikes,
                        totalPhotos = feed.user.totalPhotos,
                        totalCollections = feed.user.totalCollections,
                        instagram = feed.user.instagram,
                        twitter = feed.user.twitter
                    )
                    feedUserDao.insertFeedUser(user)

                    val feedUrl = FeedUrlEntity(
                        id = feed.user.id,
                        raw = feed.urls.raw,
                        full = feed.urls.full,
                        regular = feed.urls.regular,
                        small = feed.urls.small,
                        thumb = feed.urls.thumb
                    )
                    feedUrlDao.insertFeedUrl(feedUrl)

                    val feedLink = FeedLinkEntity(
                        id = feed.user.id,
                        self = feed.links.self,
                        html = feed.links.html,
                        download = feed.links.download,
                        downloadLocation = feed.links.downloadLocation
                    )
                    feedLinkDao.insertFeedLink(feedLink)

                    val feedPhoto = FeedPhotoEntity(
                        id = feed.id,
                        userId = user.id,
                        feedUrlId = feed.user.id,
                        feedLinkId = feed.user.id,
                        createdAt = feed.createdAt,
                        updatedAt = feed.updatedAt,
                        width = feed.width,
                        height = feed.height,
                        color = feed.color,
                        blurHash = feed.blurHash,
                        likes = feed.likes,
                        likedByUser = feed.likedByUser,
                        description = feed.description,
                        lastUpdatedAt = Calendar.getInstance()
                    )
                    feedPhotos.add(feedPhoto)

                    feedCollections = feed.currentUserFeedCollections.map { collection ->
                        FeedCollectionEntity(
                            id = collection.id,
                            feedPhotoId = feedPhoto.id,
                            title = collection.title,
                            publishedAt = collection.publishedAt,
                            lastCollectedAt = collection.lastCollectedAt,
                            updatedAt = collection.updatedAt,
                            userId = user.id,
                            coverPhoto = collection.coverPhoto
                        )
                    }
                }
                feedPhotosDao.insertAllFeedPhotos(feedPhotos)
                feedCollectionDao.insertAllFeedCollections(feedCollections)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: IOException) {
            return MediatorResult.Error(e)
        } catch (e: HttpException) {
            return MediatorResult.Error(e)
        }
    }

    // LoadType.PREPEND
    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, FeedPhotoEntity>): RemoteKey? =
        state.pages.firstOrNull { page ->
            page.data.isNotEmpty()
        }?.data?.firstOrNull()?.let { feedPhoto ->
            remoteKeysDao.getRemoteKeyByFeedPhotoId(feedPhoto.id)
        }

    // LoadType.APPEND
    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, FeedPhotoEntity>): RemoteKey? =
        state.pages.lastOrNull { page ->
            page.data.isNotEmpty()
        }?.data?.lastOrNull()?.let { feedPhoto ->
            remoteKeysDao.getRemoteKeyByFeedPhotoId(feedPhoto.id)
        }

    // LoadType.REFRESH
    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, FeedPhotoEntity>): RemoteKey? =
        state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                remoteKeysDao.getRemoteKeyByFeedPhotoId(id)
            }
        }

    companion object {
        private const val ITEMS_PER_PAGE = 10
        private const val STARTING_PAGE_INDEX = 1
        private const val ORDER_BY = "latest"
        private val CACHE_TIMEOUT = TimeUnit.MILLISECONDS.convert(4, TimeUnit.HOURS)
    }
}