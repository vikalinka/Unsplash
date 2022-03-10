package lt.vitalikas.unsplash.ui.feed_screen

import androidx.paging.PagingData
import lt.vitalikas.unsplash.domain.models.photo.Photo

sealed class FeedState {
    class Success(val data: PagingData<Photo>) : FeedState()
    class Error(val error: Throwable) : FeedState()
}