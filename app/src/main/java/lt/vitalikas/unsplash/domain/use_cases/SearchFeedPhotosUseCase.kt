package lt.vitalikas.unsplash.domain.use_cases

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import lt.vitalikas.unsplash.domain.models.FeedPhoto
import lt.vitalikas.unsplash.domain.models.Search

interface SearchFeedPhotosUseCase {
    suspend operator fun invoke(query: String): Flow<PagingData<Search>>
}