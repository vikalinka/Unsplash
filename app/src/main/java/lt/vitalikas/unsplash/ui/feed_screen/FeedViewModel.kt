package lt.vitalikas.unsplash.ui.feed_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
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

    private val _fState =
        MutableStateFlow<FeedPhotosState>(FeedPhotosState.Success(PagingData.empty()))
    val fState = _fState.asStateFlow()

    fun getFeedPhotos() {
        scope.launch(
            CoroutineExceptionHandler { _, t ->
                Timber.d("$t")
                _fState.value = FeedPhotosState.Error(t)
            }
        ) {
            getFeedPhotosUseCase.feedPhotos?.collectLatest { pagingData ->
                Timber.d("Photos fetched from memory")
                _fState.value = FeedPhotosState.Success(pagingData)
            } ?: run {
                // Retrofit launches coroutines on it`s background thread pool
                val data = getFeedPhotosUseCase.invoke()
                data.collectLatest { pagingData ->
                    _fState.value = FeedPhotosState.Success(pagingData)
                }
            }
        }
    }

    fun cancelScopeChildrenJobs() {
        if (!scope.coroutineContext.job.children.none()) {
            scope.coroutineContext.cancelChildren()
            Timber.i("photos fetching canceled")
        }
    }
}