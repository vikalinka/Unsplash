package lt.vitalikas.unsplash.data.repositories

import android.content.Context
import android.net.Uri
import androidx.paging.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import lt.vitalikas.unsplash.data.api.UnsplashApi
import lt.vitalikas.unsplash.data.db.Database
import lt.vitalikas.unsplash.data.db.entities.FeedPhotoEntity
import lt.vitalikas.unsplash.domain.models.*
import lt.vitalikas.unsplash.domain.repositories.FeedPhotosRepository
import javax.inject.Inject

class FeedPhotosRepositoryImpl @Inject constructor(
    private val unsplashApi: UnsplashApi,
    private val context: Context,
    private val dispatcherIo: CoroutineDispatcher
) : FeedPhotosRepository {

    @OptIn(ExperimentalPagingApi::class)
    override suspend fun getFeedPhotos(): Flow<PagingData<FeedPhoto>> {
        val pagingSourceFactory = {
            Database.instance.feedPhotosDao().getPagingSource()
        }
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false
            ),
            remoteMediator = FeedPhotosRemoteMediator(unsplashApi),
            pagingSourceFactory = pagingSourceFactory
        ).flow.map { pagingData ->
            pagingData.map { entity ->

                val feedUser =
                    Database.instance.feedUserDao().getUserAndFeedPhotoWithUserId(entity.userId)
                        ?: error("user with id = ${entity.userId} not found")
                val userProfileImageId = feedUser.user.userProfileImageId
                val userLinkId = feedUser.user.userLinkId

                val feedUserProfileImage = Database.instance.feedUserProfileImageDao()
                    .getFeedUserProfileImageAndUserWithFeedUserProfileImageId(
                        userProfileImageId
                    ) ?: error("profile image with id = $userProfileImageId not found")
                val userProfileImage = UserProfileImage(
                    feedUserProfileImage.userProfileImage.small,
                    feedUserProfileImage.userProfileImage.medium,
                    feedUserProfileImage.userProfileImage.large
                )

                val feedUserLink = Database.instance.feedUserLinkDao()
                    .getFeedUserLinkAndUserWithFeedUserLinkId(userLinkId)
                    ?: error("user link with id = $userLinkId not found")
                val userLink = UserLink(
                    feedUserLink.userLink.self,
                    feedUserLink.userLink.html,
                    feedUserLink.userLink.photos,
                    feedUserLink.userLink.likes,
                    feedUserLink.userLink.portfolio
                )

                val user = User(
                    id = feedUser.user.id,
                    username = feedUser.user.username,
                    name = feedUser.user.name,
                    portfolioUrl = feedUser.user.portfolioUrl,
                    bio = feedUser.user.bio,
                    location = feedUser.user.location,
                    totalLikes = feedUser.user.totalLikes,
                    totalPhotos = feedUser.user.totalPhotos,
                    totalCollections = feedUser.user.totalCollections,
                    instagram = feedUser.user.instagram,
                    twitter = feedUser.user.twitter,
                    imageUser = userProfileImage,
                    links = userLink
                )

                val feedCollections = Database.instance.feedCollectionDao().getAllFeedCollections()
                    ?: error("collections not found")

                val feedUrl = Database.instance.feedUrlDao()
                    .getFeedUrlAndFeedPhotoWithFeedUrlId(entity.feedUrlId)
                    ?: error("url with id = ${entity.feedUrlId} not found")
                val url = FeedUrl(
                    raw = feedUrl.url.raw,
                    full = feedUrl.url.full,
                    regular = feedUrl.url.regular,
                    small = feedUrl.url.small,
                    thumb = feedUrl.url.thumb
                )

                val feedLink = Database.instance.feedLinkDao()
                    .getFeedLinkAndFeedPhotoWithFeedLinkId(entity.feedLinkId)
                    ?: error("link with id = ${entity.feedLinkId} not found")
                val link = FeedLink(
                    self = feedLink.link.self,
                    html = feedLink.link.html,
                    download = feedLink.link.download,
                    downloadLocation = feedLink.link.downloadLocation
                )

                FeedPhoto(
                    id = entity.id,
                    createdAt = entity.createdAt,
                    updatedAt = entity.updatedAt,
                    width = entity.width,
                    height = entity.height,
                    color = entity.color,
                    blurHash = entity.blurHash,
                    likes = entity.likes,
                    likedByUser = entity.likedByUser,
                    description = entity.description,
                    user = user,
                    currentUserFeedCollections = feedCollections.map { collection ->
                        FeedCollection(
                            id = collection.id,
                            title = collection.title,
                            publishedAt = collection.publishedAt,
                            lastCollectedAt = collection.lastCollectedAt,
                            updatedAt = collection.updatedAt,
                            coverPhoto = collection.coverPhoto,
                            user = user
                        )
                    },
                    urls = url,
                    links = link
                )
            }
        }
    }

    override suspend fun getFeedPhotoDetailsById(id: String) =
        unsplashApi.getFeedPhotoDetails(id)

    override suspend fun insertFeedPhotos(feedPhotos: List<FeedPhotoEntity>) =
        withContext(dispatcherIo) {
            Database.instance.feedPhotosDao().insertAllFeedPhotos(feedPhotos)
        }

    override suspend fun downloadPhoto(url: String, uri: Uri) {
        context.contentResolver.openOutputStream(uri)?.use { outputStream ->
            val download = unsplashApi.trackDownload(url)
            unsplashApi.downloadPhoto(download.url)
                .byteStream().use { inputStream ->
                    inputStream.copyTo(outputStream)
                }
        }
    }

    override suspend fun likePhoto(id: String) = unsplashApi.likePhoto(id)

    override suspend fun dislikePhoto(id: String) = unsplashApi.dislikePhoto(id)

    override suspend fun updatePhoto(id: String, isLiked: Boolean, likeCount: Int) =
        withContext(dispatcherIo) {
            Database.instance.feedPhotosDao().updatePhoto(id, isLiked, likeCount)
        }

    @OptIn(ExperimentalPagingApi::class)
    override fun searchPhotos(query: String): Flow<PagingData<Search>> {
        val pagingSourceFactory = {
            UnsplashPagingSource(
                query = query,
                api = unsplashApi,
                pageSize = PAGE_SIZE
            )
        }
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    companion object {
        private const val PAGE_SIZE = 10
    }
}