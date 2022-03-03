package lt.vitalikas.unsplash.domain.use_cases

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import lt.vitalikas.unsplash.domain.models.collections.CollectionPhoto

interface GetCollectionPhotosUseCase {
    suspend operator fun invoke(id: String): Flow<PagingData<CollectionPhoto>>
}