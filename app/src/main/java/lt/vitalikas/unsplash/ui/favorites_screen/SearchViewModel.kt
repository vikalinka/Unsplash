package lt.vitalikas.unsplash.ui.favorites_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import lt.vitalikas.unsplash.data.networking.status_tracker.NetworkStatusTracker
import lt.vitalikas.unsplash.domain.use_cases.SearchPhotosUseCase
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    networkStatusTracker: NetworkStatusTracker,
    private val searchPhotosUseCase: SearchPhotosUseCase
) : ViewModel() {

    private val scope = viewModelScope

    private val _searchState =
        MutableStateFlow<SearchState>(SearchState.Success(PagingData.empty()))
    val searchState = _searchState.asStateFlow()

    val networkStatus = networkStatusTracker.networkStatus

    fun searchPhotos(flowQuery: Flow<String>) {
        flowQuery
            .debounce(1000L)
            .distinctUntilChanged()
            .flatMapLatest { query ->
                searchPhotosUseCase.invoke(query)
            }
            .cachedIn(scope)
            .onEach { pagingData ->
                _searchState.value = SearchState.Success(pagingData)
            }
            .catch { t ->
                Timber.d(t)
                _searchState.value = SearchState.Error(t)
            }

            .launchIn(scope)
    }
}