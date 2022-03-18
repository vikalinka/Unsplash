package lt.vitalikas.unsplash.ui.feed_screen

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.work.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
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

    val networkStatus = networkStatusTracker.networkStatus

    var currentOrder = DEFAULT_ORDER_BY

    suspend fun getFeedPhotos(order: String) {
        val dataFlow = getFeedPhotosUseCase(order, currentOrder)

        currentOrder = order

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

    fun likePhoto(id: String) {
        val workData = workDataOf(
            LikePhotoWorker.PHOTO_ID_KEY to id
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

    fun updatePhotoInDatabase(id: String, isLiked: Boolean, likeCount: Int) =
        viewModelScope.launch {
            try {
                photosRepository.updatePhoto(id, isLiked, likeCount)
            } catch (t: Throwable) {
                Timber.d(t)
                _feedState.value = FeedState.Error(t)
            }
        }

    companion object {
        private const val DEFAULT_ORDER_BY = "latest"
    }
}