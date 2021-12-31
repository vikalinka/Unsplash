package lt.vitalikas.unsplash.ui.feed_screen

import lt.vitalikas.unsplash.domain.models.FeedPhoto

sealed class FeedPhotosState {
    class Loading(val isLoading: Boolean) : FeedPhotosState()
    object Cancellation : FeedPhotosState()
    class Success(val photos: List<FeedPhoto>) : FeedPhotosState()
    class Error(val error: Throwable) : FeedPhotosState()
}