package lt.vitalikas.unsplash.ui.collection_with_photos_screen

import androidx.paging.PagingData
import lt.vitalikas.unsplash.domain.models.collections.CollectionPhoto

sealed class CollectionPhotosState {
    object Init : CollectionPhotosState()
    object Loading : CollectionPhotosState()
    class Success(val photos: PagingData<CollectionPhoto>) : CollectionPhotosState()
    class Error(val error: Throwable) : CollectionPhotosState()
}