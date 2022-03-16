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
import lt.vitalikas.unsplash.domain.use_cases.GetFeedPhotoDetailsUseCase
import javax.inject.Inject

@HiltViewModel
class FeedDetailsViewModel @Inject constructor(
    val context: Application,
    private val getFeedPhotoDetailsUseCase: GetFeedPhotoDetailsUseCase,
    networkStatusTracker: NetworkStatusTracker
) : ViewModel() {

    private val scope = viewModelScope

    val networkStatus = networkStatusTracker.networkStatus

    private val _feedDetailsState =
        MutableStateFlow<FeedDetailsState>(FeedDetailsState.Loading)
    val feedDetailsState = _feedDetailsState.asStateFlow()

    fun downloadPhoto(id: String, uri: Uri) {
        val workData = workDataOf(
            DownloadPhotoWorker.PHOTO_ID to id,
            DownloadPhotoWorker.PHOTO_URI to uri.toString()
        )

        val workConstraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresStorageNotLow(true)
            .build()

        val workRequest = OneTimeWorkRequestBuilder<DownloadPhotoWorker>()
            .setInputData(workData)
            .setConstraints(workConstraints)
            .build()

        WorkManager.getInstance(context)
            .enqueueUniqueWork(
                DownloadPhotoWorker.DOWNLOAD_PHOTO_WORK_ID,
                ExistingWorkPolicy.REPLACE,
                workRequest
            )
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
                LikePhotoWorker.LIKE_PHOTO_WORK_ID_FROM_DETAILS,
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
                DislikePhotoWorker.DISLIKE_PHOTO_WORK_ID_FROM_DETAILS,
                ExistingWorkPolicy.REPLACE,
                workRequest
            )
    }

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
}