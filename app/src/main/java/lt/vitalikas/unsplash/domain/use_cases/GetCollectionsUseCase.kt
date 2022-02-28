package lt.vitalikas.unsplash.domain.use_cases

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import lt.vitalikas.unsplash.domain.models.collections.CollectionResponse

interface GetCollectionsUseCase {
    suspend operator fun invoke(): Flow<PagingData<CollectionResponse>>
}