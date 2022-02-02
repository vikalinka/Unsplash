package lt.vitalikas.unsplash.ui.feed_details_screen

import lt.vitalikas.unsplash.domain.models.FeedPhotoDetails

sealed class FeedDetailsState {
    object Loading : FeedDetailsState()
    class Success(val data: FeedPhotoDetails) : FeedDetailsState()
    class Error(val error: Throwable) : FeedDetailsState()
}