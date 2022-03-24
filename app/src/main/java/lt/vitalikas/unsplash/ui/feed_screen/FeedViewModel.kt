package lt.vitalikas.unsplash.ui.feed_screen

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import androidx.work.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import lt.vitalikas.unsplash.data.helpers.LocalChanges
import lt.vitalikas.unsplash.data.helpers.OnChange
import lt.vitalikas.unsplash.data.networking.status_tracker.NetworkStatusTracker
import lt.vitalikas.unsplash.data.services.DislikePhotoWorker
import lt.vitalikas.unsplash.data.services.LikePhotoWorker
import lt.vitalikas.unsplash.domain.repositories.PhotosRepository
import lt.vitalikas.unsplash.domain.use_cases.GetFeedPhotosUseCase
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val context: Application,
    networkStatusTracker: NetworkStatusTracker,
    private val getFeedPhotosUseCase: GetFeedPhotosUseCase,
    private val photosRepository: PhotosRepository
) : ViewModel() {

    private val _feedState =
        MutableStateFlow<FeedState>(FeedState.Success(PagingData.empty()))
    val feedState = _feedState.asStateFlow()

    private val localChanges = LocalChanges()
    private val localChangesFlow =
        MutableStateFlow(OnChange(localChanges))

    val networkStatus = networkStatusTracker.networkStatus

    var prevOrderBy = DEFAULT_ORDER_BY
        private set

    suspend fun getFeedPhotos(order: String) {
        val dataFlow = getFeedPhotosUseCase(order, prevOrderBy)

        dataFlow.combine(localChangesFlow) { pagingData, localChanges ->
            pagingData.map { photo ->

                val reactionFlag = localChanges.value.reactionFlags[photo.id]
                val reactionTotalCount = localChanges.value.reactionTotalCounts[photo.id]

                val photoWithLocalChanges =
                    if (reactionFlag == null || reactionTotalCount == null) {
                        photo
                    } else {
                        photo.copy(likedByUser = reactionFlag, likes = reactionTotalCount)
                    }

                photoWithLocalChanges
            }
        }

        prevOrderBy = order

        dataFlow
            .cachedIn(viewModelScope)
            .onEach { pagingData ->
                _feedState.value = FeedState.Success(pagingData)
            }
            .catch { t ->
                Timber.d(t)
                _feedState.value = FeedState.Error(t)
            }
            .launchIn(viewModelScope)
    }

    fun likePhoto(photoId: String) = WorkManager.getInstance(context)
        .enqueueUniqueWork(
            LikePhotoWorker.LIKE_PHOTO_WORK_ID_FEED,
            ExistingWorkPolicy.REPLACE,
            LikePhotoWorker.likePhotoRequest(photoId)
        )

    fun dislikePhoto(photoId: String) = WorkManager.getInstance(context)
        .enqueueUniqueWork(
            DislikePhotoWorker.DISLIKE_PHOTO_WORK_ID_FEED,
            ExistingWorkPolicy.REPLACE,
            DislikePhotoWorker.dislikePhotoRequest(photoId)
        )

    fun updatePhotoInDatabase(id: String, isLiked: Boolean, likeCount: Int) =
        viewModelScope.launch {
            try {
                photosRepository.updatePhoto(id, isLiked, likeCount)
            } catch (t: Throwable) {
                Timber.d(t)
                _feedState.value = FeedState.Error(t)
            }
        }

    fun updateLocalChanges(id: String, isLiked: Boolean, likeCount: Int) {
        localChanges.reactionFlags[id] = isLiked
        localChanges.reactionTotalCounts[id] = likeCount
        localChangesFlow.value = OnChange(localChanges)
        Timber.d("ID = $id, REACTION = $isLiked, LIKE_COUNT = $likeCount")
    }

    companion object {
        private const val DEFAULT_ORDER_BY = "latest"
    }
}