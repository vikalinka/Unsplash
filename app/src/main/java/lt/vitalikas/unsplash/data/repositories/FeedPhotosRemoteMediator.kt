package lt.vitalikas.unsplash.data.repositories

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import lt.vitalikas.unsplash.data.apis.UnsplashApi
import lt.vitalikas.unsplash.data.databases.Database
import lt.vitalikas.unsplash.data.databases.entities.*
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class FeedPhotosRemoteMediator @Inject constructor(
    private val api: UnsplashApi
) : RemoteMediator<Int, FeedPhotoEntity>() {

    private val feedPhotosDao = Database.instance.feedPhotosDao()
    private val feedUserDao = Database.instance.feedUserDao()
    private val feedUserProfileImageDao = Database.instance.feedUserProfileImageDao()
    private val feedUserLinkDao = Database.instance.feedUserLinkDao()
    private val remoteKeysDao = Database.instance.remoteKeysDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, FeedPhotoEntity>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKey = getRemoteKeyClosestToCurrentPosition(state)
                remoteKey?.nextKey?.minus(1) ?: STARTING_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKey = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKey?.prevKey ?: return MediatorResult.Success(
                    endOfPaginationReached = true
                )
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKey = getRemoteKeyForLastItem(state)
                val nextKey = remoteKey?.nextKey ?: return MediatorResult.Success(
                    endOfPaginationReached = true
                )
                nextKey
            }
        }

        try {
            val photos = api.getFeedPhotos(
                page,
                ITEMS_PER_PAGE,
                ORDER_BY
            )
            val endOfPagination = photos.isEmpty()

            Database.instance.withTransaction {
                // clear all tables in the database
                if (loadType == LoadType.REFRESH) {
                    remoteKeysDao.deleteAllRemoteKeys()
                    feedPhotosDao.deleteAllFeedPhotos()
                }

                val prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1
                val nextKey = if (endOfPagination) null else page + 1
                val keys = photos.map { feedPhoto ->
                    RemoteKey(
                        feedPhotoId = feedPhoto.id,
                        prevKey = prevKey,
                        nextKey = nextKey
                    )
                }
                val feedPhotos = mutableListOf<FeedPhotoEntity>()
                photos.map { feed ->
                    val userProfileImage = UserProfileImageEntity(
                        id = 0L,
                        small = feed.user.imageUser.small,
                        medium = feed.user.imageUser.medium,
                        large = feed.user.imageUser.large
                    )
                    feedUserProfileImageDao.insertFeedUserProfileImage(userProfileImage)

                    val userLink = UserLinkEntity(
                        id = 0L,
                        self = feed.user.links.self,
                        html = feed.user.links.html,
                        photos = feed.user.links.photos,
                        likes = feed.user.links.likes,
                        portfolio = feed.user.links.portfolio,
                    )
                    feedUserLinkDao.insertFeedUserLink(userLink)

                    val user = UserEntity(
                        id = feed.user.id,
                        userProfileImageId = userProfileImage.id,
                        userLinkId = userLink.id,
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
                        id = 0L,
                        raw = feed.urls.raw,
                        full = feed.urls.full,
                        regular = feed.urls.regular,
                        small = feed.urls.small,
                        thumb = feed.urls.thumb
                    )

                    val feedLink = FeedLinkEntity(
                        id = 0L,
                        self = feed.links.self,
                        html = feed.links.html,
                        download = feed.links.download,
                        downloadLocation = feed.links.downloadLocation
                    )

                    val feedPhoto = FeedPhotoEntity(
                        id = feed.id,
                        userId = user.id,
                        feedUrlId = feedUrl.id,
                        feedLinkId = feedLink.id,
                        createdAt = feed.createdAt,
                        updatedAt = feed.updatedAt,
                        width = feed.width,
                        height = feed.height,
                        color = feed.color,
                        blurHash = feed.blurHash,
                        likes = feed.likes,
                        likedByUser = feed.likedByUser,
                        description = feed.description
                    )
                    feedPhotosDao.insertAllFeedPhotos(listOf(feedPhoto))
                }

                remoteKeysDao.insertAllKeys(keys)

            }
            return MediatorResult.Success(endOfPaginationReached = endOfPagination)
        } catch (e: IOException) {
            return MediatorResult.Error(e)
        } catch (e: HttpException) {
            return MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, FeedPhotoEntity>): RemoteKey? =
        state.pages.firstOrNull { page ->
            page.data.isNotEmpty()
        }?.data?.firstOrNull()?.let { feedPhoto ->
            remoteKeysDao.getRemoteKeyByFeedPhotoId(feedPhoto.id)
        }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, FeedPhotoEntity>): RemoteKey? =
        state.pages.lastOrNull { page ->
            page.data.isNotEmpty()
        }?.data?.lastOrNull()?.let { feedPhoto ->
            remoteKeysDao.getRemoteKeyByFeedPhotoId(feedPhoto.id)
        }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, FeedPhotoEntity>): RemoteKey? =
        state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                remoteKeysDao.getRemoteKeyByFeedPhotoId(id)
            }
        }

    companion object {
        private const val ITEMS_PER_PAGE = 12
        private const val STARTING_PAGE_INDEX = 1
        private const val ORDER_BY = "popular"
    }
}