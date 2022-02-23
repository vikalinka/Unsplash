package lt.vitalikas.unsplash.ui.favorites_screen

import androidx.paging.PagingData
import lt.vitalikas.unsplash.domain.models.SearchResult

sealed class SearchState {
    class Success(val data: PagingData<SearchResult>) : SearchState()
    class Error(val error: Throwable) : SearchState()
}