package lt.vitalikas.unsplash.ui.feed_screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import lt.vitalikas.unsplash.domain.models.FeedPhoto
import lt.vitalikas.unsplash.domain.use_cases.GetFeedPhotoDetailsUseCase
import lt.vitalikas.unsplash.domain.use_cases.GetFeedPhotosUseCase
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
                    _feedState.postValue(FeedPhotosState.Loading(true))
                    // Retrofit launches coroutine on it`s background thread pool
                    scope.launch(CoroutineExceptionHandler { _, t ->
                        Timber.d("$t")
                        _feedState.value = FeedPhotosState.Loading(false)
                        _feedState.value = FeedPhotosState.Error(t)
                    }) {
                        getFeedPhotosUseCase.invoke().apply {
                            collect { pagingData ->
                                _feedState.value = FeedPhotosState.Loading(false)
                                _feedState.value = FeedPhotosState.Success(pagingData)
                            }
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
}