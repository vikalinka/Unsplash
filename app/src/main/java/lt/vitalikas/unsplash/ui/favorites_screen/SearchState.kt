package lt.vitalikas.unsplash.ui.favorites_screen

import androidx.paging.PagingData
import lt.vitalikas.unsplash.domain.models.search.SearchPhoto

sealed class SearchState {
    class Success(val data: PagingData<SearchPhoto>) : SearchState()
    class Error(val error: Throwable) : SearchState()
}