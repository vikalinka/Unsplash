package lt.vitalikas.unsplash.ui.photo_details_screen

import lt.vitalikas.unsplash.domain.models.photo_details.PhotoDetails

sealed class PhotoDetailsFetchingState {
    object Loading : PhotoDetailsFetchingState()
    class Success(val data: PhotoDetails) : PhotoDetailsFetchingState()
    class Error(val error: Throwable) : PhotoDetailsFetchingState()
}