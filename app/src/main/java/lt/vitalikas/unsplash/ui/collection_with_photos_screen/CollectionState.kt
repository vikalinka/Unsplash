package lt.vitalikas.unsplash.ui.collection_with_photos_screen

import lt.vitalikas.unsplash.domain.models.collections.Collection

sealed class CollectionState {
    object Init : CollectionState()
    object Loading : CollectionState()
    class Success(val collection: Collection) : CollectionState()
    class Error(val error: Throwable) : CollectionState()
}