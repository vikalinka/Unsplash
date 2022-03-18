package lt.vitalikas.unsplash.ui.feed_details_screen

import android.app.Application
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import lt.vitalikas.unsplash.data.networking.status_tracker.NetworkStatusTracker
import lt.vitalikas.unsplash.data.services.DislikePhotoWorker
import lt.vitalikas.unsplash.data.services.DownloadPhotoWorker
import lt.vitalikas.unsplash.data.services.LikePhotoWorker
import lt.vitalikas.unsplash.domain.repositories.PhotosRepository
import lt.vitalikas.unsplash.domain.use_cases.GetFeedPhotoDetailsUseCase
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class FeedDetailsViewModel @Inject constructor(
    val context: Application,
    private val getFeedPhotoDetailsUseCase: GetFeedPhotoDetailsUseCase,
    networkStatusTracker: NetworkStatusTracker,
    private val photosRepository: PhotosRepository
) : ViewModel() {

    private val scope = viewModelScope

    val networkStatus = networkStatusTracker.networkStatus

    private val _feedDetailsState =
        MutableStateFlow<FeedDetailsState>(FeedDetailsState.Loading)
    val feedDetailsState = _feedDetailsState.asStateFlow()

    fun downloadPhoto(photoId: String, fetchingLocationUri: Uri) = WorkManager.getInstance(context)
        .enqueueUniqueWork(
            DownloadPhotoWorker.DOWNLOAD_PHOTO_WORK_ID,
            ExistingWorkPolicy.REPLACE,
            DownloadPhotoWorker.downloadPhotoRequest(photoId, fetchingLocationUri)
        )

    fun likePhoto(id: String) = WorkManager.getInstance(context)
        .enqueueUniqueWork(
            LikePhotoWorker.LIKE_PHOTO_WORK_ID_DETAILS,
            ExistingWorkPolicy.REPLACE,
            LikePhotoWorker.likePhotoRequest(id)
        )

    fun dislikePhoto(photoId: String) = WorkManager.getInstance(context)
        .enqueueUniqueWork(
            DislikePhotoWorker.DISLIKE_PHOTO_WORK_ID_DETAILS,
            ExistingWorkPolicy.REPLACE,
            DislikePhotoWorker.dislikePhotoRequest(photoId)
        )

    suspend fun getFeedPhotoDetails(id: String) {
        try {
            scope.launch {
                val details = getFeedPhotoDetailsUseCase(id)
                _feedDetailsState.value = FeedDetailsState.Success(details)
            }
        } catch (t: Throwable) {
            _feedDetailsState.value = FeedDetailsState.Error(t)
        }
    }

    fun updatePhotoInDatabase(id: String, isLiked: Boolean, likeCount: Int) =
        viewModelScope.launch {
            try {
                photosRepository.updatePhoto(id, isLiked, likeCount)
            } catch (t: Throwable) {
                Timber.d(t)
                _feedDetailsState.value = FeedDetailsState.Error(t)
            }
        }
}