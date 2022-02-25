package lt.vitalikas.unsplash.domain.use_cases

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import lt.vitalikas.unsplash.domain.models.search.SearchPhoto

interface SearchPhotosUseCase {
    suspend operator fun invoke(query: String): Flow<PagingData<SearchPhoto>>
}