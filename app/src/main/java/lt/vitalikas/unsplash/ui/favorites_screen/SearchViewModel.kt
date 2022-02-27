package lt.vitalikas.unsplash.ui.favorites_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import lt.vitalikas.unsplash.data.networking.status_tracker.NetworkStatusTracker
import lt.vitalikas.unsplash.domain.models.search.SearchPhoto
import lt.vitalikas.unsplash.domain.use_cases.SearchPhotosUseCase
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    networkStatusTracker: NetworkStatusTracker,
    private val searchPhotosUseCase: SearchPhotosUseCase
) : ViewModel() {

    val networkStatus = networkStatusTracker.networkStatus

    @OptIn(ExperimentalCoroutinesApi::class, kotlinx.coroutines.FlowPreview::class)
    fun getSearchData(queryFlow: Flow<String>): Flow<PagingData<SearchPhoto>> {
        return queryFlow
            .debounce(1000L)
            .distinctUntilChanged()
            .flatMapLatest { query ->
                searchPhotosUseCase.invoke(query)
            }
            .cachedIn(viewModelScope)
    }
}