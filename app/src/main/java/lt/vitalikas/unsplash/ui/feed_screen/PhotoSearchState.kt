package lt.vitalikas.unsplash.ui.feed_screen

import androidx.paging.PagingData
import lt.vitalikas.unsplash.domain.models.Search

sealed class PhotoSearchState {
    class Success(val data: PagingData<Search>) : PhotoSearchState()
    class Error(val error: Throwable) : PhotoSearchState()
}