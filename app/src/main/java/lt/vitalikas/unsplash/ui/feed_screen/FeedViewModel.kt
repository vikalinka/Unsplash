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

    private val _feedState =
        MutableStateFlow<FeedState>(FeedState.Success(PagingData.empty()))
    val feedState = _feedState.asStateFlow()

    fun getFeedPhotos() {
        scope.launch(
            CoroutineExceptionHandler { _, t ->
                Timber.d("$t")
                _feedState.value = FeedState.Error(t)
            }
        ) {
            getFeedPhotosUseCase.invoke().apply {
                collectLatest { pagingData ->
                    _feedState.value = FeedState.Success(pagingData)
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