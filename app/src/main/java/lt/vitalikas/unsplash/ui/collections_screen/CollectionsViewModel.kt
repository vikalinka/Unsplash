package lt.vitalikas.unsplash.ui.collections_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import lt.vitalikas.unsplash.data.networking.status_tracker.NetworkStatusTracker
import lt.vitalikas.unsplash.domain.models.collections.CollectionResponse
import lt.vitalikas.unsplash.domain.use_cases.GetCollectionsUseCase
import javax.inject.Inject

@HiltViewModel
class CollectionsViewModel @Inject constructor(
    networkStatusTracker: NetworkStatusTracker,
    private val getCollectionsUseCase: GetCollectionsUseCase
) : ViewModel() {

    val networkStatus = networkStatusTracker.networkStatus

    suspend fun getCollections(): Flow<PagingData<CollectionResponse>> =
        getCollectionsUseCase().cachedIn(viewModelScope)
}