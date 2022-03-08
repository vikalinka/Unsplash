package lt.vitalikas.unsplash.ui.search_screen

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.work.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import lt.vitalikas.unsplash.data.networking.status_tracker.NetworkStatusTracker
import lt.vitalikas.unsplash.data.services.DislikePhotoWorker
import lt.vitalikas.unsplash.data.services.LikePhotoWorker
import lt.vitalikas.unsplash.domain.models.photo.Photo
import lt.vitalikas.unsplash.domain.models.search.SearchPhoto
import lt.vitalikas.unsplash.domain.use_cases.SearchPhotosUseCase
import lt.vitalikas.unsplash.ui.feed_screen.FeedState
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val context: Application,
    networkStatusTracker: NetworkStatusTracker,
    private val searchPhotosUseCase: SearchPhotosUseCase
) : ViewModel() {

    val networkStatus = networkStatusTracker.networkStatus

    private val _searchState =
        MutableStateFlow<SearchState>(SearchState.NotLoading)
    val searchState = _searchState.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class, kotlinx.coroutines.FlowPreview::class)
    fun searchData(queryFlow: Flow<String>) {
        queryFlow
            .debounce(1000L)
            .distinctUntilChanged()
            .flatMapLatest { query ->
                searchPhotosUseCase.invoke(query)
            }
            .cachedIn(viewModelScope)
            .onEach { pagingData ->
                _searchState.value = SearchState.Success(pagingData)
            }
            .launchIn(viewModelScope)
    }

    @OptIn(ExperimentalCoroutinesApi::class, kotlinx.coroutines.FlowPreview::class)
    fun getSearchData(queryFlow: Flow<String>): Flow<PagingData<Photo>> {
        return queryFlow
            .debounce(1000L)
            .distinctUntilChanged()
            .flatMapLatest { query ->
                searchPhotosUseCase.invoke(query)
            }
            .cachedIn(viewModelScope)
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
}