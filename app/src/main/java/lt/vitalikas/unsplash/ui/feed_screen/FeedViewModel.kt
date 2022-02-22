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
import lt.vitalikas.unsplash.domain.repositories.FeedPhotosRepository
import lt.vitalikas.unsplash.domain.use_cases.GetFeedPhotosUseCase
import lt.vitalikas.unsplash.domain.use_cases.SearchFeedPhotosUseCase
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    val context: Application,
    private val getFeedPhotosUseCase: GetFeedPhotosUseCase,
    private val searchFeedPhotosUseCase: SearchFeedPhotosUseCase,
    networkStatusTracker: NetworkStatusTracker,
    private val feedPhotosRepository: FeedPhotosRepository
) : ViewModel() {

    private val scope = viewModelScope

    private val _feedState =
        MutableStateFlow<FeedState>(FeedState.Success(PagingData.empty()))
    val feedState = _feedState.asStateFlow()

    private val _searchState =
        MutableStateFlow<PhotoSearchState>(PhotoSearchState.Success(PagingData.empty()))
    val searchState = _searchState.asStateFlow()

    val networkStatus = networkStatusTracker.networkStatus

    suspend fun getFeedPhotos() {
        val dataFlow = getFeedPhotosUseCase()
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

    fun searchFeedPhotos(query: Flow<String>) {
        val dataFlow =
            query
                .debounce(1000L)
                .distinctUntilChanged()
                .flatMapLatest { query ->
                    Timber.d("VIEW MODEL QUERY = $query")
                    searchFeedPhotosUseCase.invoke(query)
                }
                .cachedIn(scope)
        dataFlow
            .onEach { pagingData ->
                _searchState.value = PhotoSearchState.Success(pagingData)
            }
            .catch { t ->
                Timber.d(t)
                _searchState.value = PhotoSearchState.Error(t)
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

    fun updatePhotoInDatabase(id: String, isLiked: Boolean, likeCount: Int) =
        viewModelScope.launch {
            try {
                feedPhotosRepository.updatePhoto(id, isLiked, likeCount)
            } catch (t: Throwable) {
                Timber.d(t)
                _feedState.value = FeedState.Error(t)
            }
        }
}