package lt.vitalikas.unsplash.domain.use_cases

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import lt.vitalikas.unsplash.domain.models.SearchResult

interface SearchPhotosUseCase {
    operator fun invoke(query: String): Flow<PagingData<SearchResult>>
}