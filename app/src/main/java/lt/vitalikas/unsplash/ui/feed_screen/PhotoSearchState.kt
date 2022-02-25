package lt.vitalikas.unsplash.ui.feed_screen

import androidx.paging.PagingData
import lt.vitalikas.unsplash.domain.models.search.SearchResponse

sealed class PhotoSearchState {
    class Success(val data: PagingData<SearchResponse>) : PhotoSearchState()
    class Error(val error: Throwable) : PhotoSearchState()
}