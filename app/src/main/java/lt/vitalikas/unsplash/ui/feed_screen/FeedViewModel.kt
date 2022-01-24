package lt.vitalikas.unsplash.ui.feed_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import lt.vitalikas.unsplash.domain.use_cases.GetFeedPhotosUseCase
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val getFeedPhotosUseCase: GetFeedPhotosUseCase
) : ViewModel() {

    private val scope = viewModelScope

    private val _feedState =
        MutableStateFlow<FeedState>(FeedState.Success(PagingData.empty()))
    val feedState = _feedState.asStateFlow()

    suspend fun getFeedPhotos() {
        val flow = getFeedPhotosUseCase.invoke()
            .cachedIn(scope)
        flow
            .onEach { pagingData ->
                _feedState.value = FeedState.Success(pagingData)
            }
            .catch { t ->
                Timber.d("$t")
                _feedState.value = FeedState.Error(t)
            }
            .launchIn(scope)
    }

    fun cancelScopeChildrenJobs() {
        if (!scope.coroutineContext.job.children.none()) {
            scope.coroutineContext.cancelChildren()
            Timber.i("photos fetching canceled")
        }
    }
}