package lt.vitalikas.unsplash.ui.search_screen

import androidx.paging.PagingData
import lt.vitalikas.unsplash.domain.models.photo.Photo

sealed class SearchState {
    object NotLoading : SearchState()
    object Loading : SearchState()
    class Success(val data: PagingData<Photo>) : SearchState()
    class Error(val error: Throwable) : SearchState()
    class Empty(val text: String) : SearchState()
}