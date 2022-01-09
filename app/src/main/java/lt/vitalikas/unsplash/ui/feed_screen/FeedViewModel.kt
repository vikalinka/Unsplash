package lt.vitalikas.unsplash.ui.feed_screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import lt.vitalikas.unsplash.domain.models.FeedPhoto
import lt.vitalikas.unsplash.domain.use_cases.GetFeedPhotoDetailsUseCase
import lt.vitalikas.unsplash.domain.use_cases.GetFeedPhotosUseCase
import lt.vitalikas.unsplash.ui.profile_screen.ProfileDataState
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val getFeedPhotosUseCase: GetFeedPhotosUseCase,
    private val getFeedPhotoDetailsUseCase: GetFeedPhotoDetailsUseCase
) : ViewModel() {

    private val scope = viewModelScope

    private val _feedState = MutableLiveData<FeedPhotosState>()
    val feedState: LiveData<FeedPhotosState>
        get() = _feedState

    fun getFeedPhotos() {
        scope.launch {
            when (feedState.value) {
                is FeedPhotosState.Error, FeedPhotosState.Cancellation, null -> {

                    // Retrofit launches coroutines on it`s background thread pool
                    scope.launch(CoroutineExceptionHandler { _, t ->
                        Timber.d("$t")
                        _feedState.value = FeedPhotosState.Loading(false)
                        _feedState.value = FeedPhotosState.Error(t)
                    }) {
                        val data = getFeedPhotosUseCase.invoke()
                        data.collectLatest { pagingData ->
                            _feedState.value = FeedPhotosState.Success(pagingData)
                        }
                    }
                }
                else -> {
                    try {
                        getFeedPhotosUseCase.feedPhotos?.collectLatest { pagingData ->
                            Timber.d("Photos fetched from memory")
                            _feedState.value = FeedPhotosState.Success(pagingData)
                        } ?: error("Error retrieving photos")
                    } catch (t: Throwable) {
                        Timber.d("$t")
                        _feedState.postValue(FeedPhotosState.Error(t))
                    }
                }
            }
        }
    }

    fun cancelScopeChildrenJobs() {
        if (!scope.coroutineContext.job.children.none()) {
            scope.coroutineContext.cancelChildren()
            _feedState.value = FeedPhotosState.Cancellation
            Timber.i("photos fetching canceled")
        }
    }
}