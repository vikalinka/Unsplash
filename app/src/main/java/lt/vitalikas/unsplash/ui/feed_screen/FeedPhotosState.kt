package lt.vitalikas.unsplash.ui.feed_screen

import androidx.paging.PagingData
import lt.vitalikas.unsplash.domain.models.FeedPhoto

sealed class FeedPhotosState {
    class Success(val data: PagingData<FeedPhoto>) : FeedPhotosState()
    class Error(val error: Throwable) : FeedPhotosState()
}