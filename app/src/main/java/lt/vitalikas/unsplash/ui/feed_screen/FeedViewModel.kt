package lt.vitalikas.unsplash.ui.feed_screen

import android.app.Application
import android.icu.util.Calendar
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.work.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import lt.vitalikas.unsplash.data.db.Database
import lt.vitalikas.unsplash.data.db.entities.*
import lt.vitalikas.unsplash.data.networking.status_tracker.NetworkStatusTracker
import lt.vitalikas.unsplash.data.services.DislikePhotoWorker
import lt.vitalikas.unsplash.data.services.LikePhotoWorker
import lt.vitalikas.unsplash.domain.models.FeedPhoto
import lt.vitalikas.unsplash.domain.repositories.FeedPhotosRepository
import lt.vitalikas.unsplash.domain.use_cases.GetFeedPhotosUseCase
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    val context: Application,
    private val getFeedPhotosUseCase: GetFeedPhotosUseCase,
    networkStatusTracker: NetworkStatusTracker,
    private val feedPhotosRepository: FeedPhotosRepository
) : ViewModel() {

    private val scope = viewModelScope

    private val _feedState =
        MutableStateFlow<FeedState>(FeedState.Success(PagingData.empty()))
    val feedState = _feedState.asStateFlow()

    val networkStatus = networkStatusTracker.networkStatus

    private val feedUserDao = Database.instance.feedUserDao()
    private val feedUserProfileImageDao = Database.instance.feedUserProfileImageDao()
    private val feedUserLinkDao = Database.instance.feedUserLinkDao()
    private val feedUrlDao = Database.instance.feedUrlDao()
    private val feedLinkDao = Database.instance.feedLinkDao()
    private val feedCollectionDao = Database.instance.feedCollectionDao()

    suspend fun getFeedPhotos() {
        val dataFlow = getFeedPhotosUseCase.invoke()
            .cachedIn(scope)
        dataFlow
            .onEach { pagingData ->
                _feedState.value = FeedState.Success(pagingData)
            }
            .catch { t ->
                Timber.d(t)
                _feedState.value = FeedState.Error(t)
            }
            .launchIn(scope)
    }

    fun cancelScopeChildrenJobs() {
        if (!scope.coroutineContext.job.children.none()) {
            scope.coroutineContext.cancelChildren()
            Timber.i("photos fetching canceled")
        }
    }

    fun likePhoto(id: String) {

        val workData = workDataOf(
            LikePhotoWorker.LIKE_PHOTO_ID to id
        )

        val workConstraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresStorageNotLow(false)
            .build()

        val workRequest = OneTimeWorkRequestBuilder<LikePhotoWorker>()
            .setInputData(workData)
            .setConstraints(workConstraints)
            .build()

        WorkManager.getInstance(context)
            .enqueueUniqueWork(
                LikePhotoWorker.LIKE_PHOTO_WORK_ID_FROM_FEED,
                ExistingWorkPolicy.REPLACE,
                workRequest
            )
    }

    fun dislikePhoto(id: String) {

        val workData = workDataOf(
            DislikePhotoWorker.DISLIKE_PHOTO_ID to id
        )

        val workConstraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresStorageNotLow(false)
            .build()

        val workRequest = OneTimeWorkRequestBuilder<DislikePhotoWorker>()
            .setInputData(workData)
            .setConstraints(workConstraints)
            .build()

        WorkManager.getInstance(context)
            .enqueueUniqueWork(
                DislikePhotoWorker.DISLIKE_PHOTO_WORK_ID_FROM_FEED,
                ExistingWorkPolicy.REPLACE,
                workRequest
            )
    }

    fun updatePhoto(photo: FeedPhoto) =
        viewModelScope.launch {
            try {

                val userProfileImage = UserProfileImageEntity(
                    id = photo.user.id,
                    small = photo.user.imageUser.small,
                    medium = photo.user.imageUser.medium,
                    large = photo.user.imageUser.large
                )
                feedUserProfileImageDao.insertFeedUserProfileImage(userProfileImage)

                val userLink = UserLinkEntity(
                    id = photo.user.id,
                    self = photo.user.links.self,
                    html = photo.user.links.html,
                    photos = photo.user.links.photos,
                    likes = photo.user.links.likes,
                    portfolio = photo.user.links.portfolio,
                )
                feedUserLinkDao.insertFeedUserLink(userLink)

                val user = UserEntity(
                    id = photo.user.id,
                    userProfileImageId = photo.user.id,
                    userLinkId = photo.user.id,
                    username = photo.user.username,
                    name = photo.user.name,
                    portfolioUrl = photo.user.portfolioUrl,
                    bio = photo.user.bio,
                    location = photo.user.location,
                    totalLikes = photo.user.totalLikes,
                    totalPhotos = photo.user.totalPhotos,
                    totalCollections = photo.user.totalCollections,
                    instagram = photo.user.instagram,
                    twitter = photo.user.twitter
                )
                feedUserDao.insertFeedUser(user)

                val feedUrl = FeedUrlEntity(
                    id = photo.user.id,
                    raw = photo.urls.raw,
                    full = photo.urls.full,
                    regular = photo.urls.regular,
                    small = photo.urls.small,
                    thumb = photo.urls.thumb
                )
                feedUrlDao.insertFeedUrl(feedUrl)

                val feedLink = FeedLinkEntity(
                    id = photo.user.id,
                    self = photo.links.self,
                    html = photo.links.html,
                    download = photo.links.download,
                    downloadLocation = photo.links.downloadLocation
                )
                feedLinkDao.insertFeedLink(feedLink)

                val feedPhoto = FeedPhotoEntity(
                    id = photo.id,
                    userId = user.id,
                    feedUrlId = photo.user.id,
                    feedLinkId = photo.user.id,
                    createdAt = photo.createdAt,
                    updatedAt = photo.updatedAt,
                    width = photo.width,
                    height = photo.height,
                    color = photo.color,
                    blurHash = photo.blurHash,
                    likes = photo.likes,
                    likedByUser = photo.likedByUser,
                    description = photo.description,
                    lastUpdatedAt = Calendar.getInstance()
                )

                val feedCollections = photo.currentUserFeedCollections.map { collection ->
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

                feedPhotosRepository.updatePhoto(feedPhoto)
                feedCollectionDao.insertAllFeedCollections(feedCollections)

            } catch (t: Throwable) {
                Timber.d(t)
                _feedState.value = FeedState.Error(t)
            }
        }
}