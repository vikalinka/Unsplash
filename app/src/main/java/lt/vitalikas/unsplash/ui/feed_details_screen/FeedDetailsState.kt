package lt.vitalikas.unsplash.ui.feed_details_screen

import lt.vitalikas.unsplash.domain.models.photo_details.PhotoDetails

sealed class FeedDetailsState {
    object Loading : FeedDetailsState()
    class Success(val data: PhotoDetails) : FeedDetailsState()
    class Error(val error: Throwable) : FeedDetailsState()
}