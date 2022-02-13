package lt.vitalikas.unsplash.ui.feed_details_screen

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import lt.vitalikas.unsplash.data.networking.status_tracker.NetworkStatusTracker
import lt.vitalikas.unsplash.data.services.DownloadRequest
import lt.vitalikas.unsplash.data.services.DownloadWorker
import lt.vitalikas.unsplash.domain.use_cases.GetFeedPhotoDetailsUseCase
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class FeedDetailsViewModel @Inject constructor(
    val context: Context,
    private val getFeedPhotoDetailsUseCase: GetFeedPhotoDetailsUseCase,
    networkStatusTracker: NetworkStatusTracker
) : ViewModel() {

    private val scope = viewModelScope

    val networkStatus = networkStatusTracker.networkStatus

    private val _feedDetailsState =
        MutableStateFlow<FeedDetailsState>(FeedDetailsState.Loading)
    val feedDetailsState = _feedDetailsState.asStateFlow()

    fun downloadPhoto(url: String, uri: Uri) {

        val workRequest = DownloadRequest(url, uri).workRequest

        WorkManager.getInstance(context)
            .enqueueUniqueWork(
                DownloadWorker.DOWNLOAD_PHOTO_WORK_UNIQUE_ID,
                ExistingWorkPolicy.KEEP,
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

    fun cancelScopeChildrenJobs() {
        if (!scope.coroutineContext.job.children.none()) {
            scope.coroutineContext.cancelChildren()
            Timber.i("photo details fetching canceled")
        }
    }
}