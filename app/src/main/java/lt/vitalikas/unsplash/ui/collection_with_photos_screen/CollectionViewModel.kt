package lt.vitalikas.unsplash.ui.collection_with_photos_screen

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import lt.vitalikas.unsplash.data.networking.status_tracker.NetworkStatusTracker
import lt.vitalikas.unsplash.domain.models.collections.Collection
import lt.vitalikas.unsplash.domain.use_cases.GetCollectionUseCase
import javax.inject.Inject

@HiltViewModel
class CollectionViewModel @Inject constructor(
    networkStatusTracker: NetworkStatusTracker,
    private val getCollectionUseCase: GetCollectionUseCase
) : ViewModel() {

    val networkStatus = networkStatusTracker.networkStatus

    suspend fun getCollection(id: String): Collection = getCollectionUseCase(id)
}