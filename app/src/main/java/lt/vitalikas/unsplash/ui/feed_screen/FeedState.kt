package lt.vitalikas.unsplash.ui.feed_screen

import androidx.paging.PagingData
import lt.vitalikas.unsplash.domain.models.FeedPhoto

sealed class FeedState {
    object Loading : FeedState()
    class Success(val data: PagingData<FeedPhoto>) : FeedState()
    class Error(val error: Throwable) : FeedState()
}