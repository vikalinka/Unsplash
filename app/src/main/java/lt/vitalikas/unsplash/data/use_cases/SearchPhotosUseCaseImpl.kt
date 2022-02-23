package lt.vitalikas.unsplash.data.use_cases

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import lt.vitalikas.unsplash.domain.models.SearchResult
import lt.vitalikas.unsplash.domain.repositories.FeedPhotosRepository
import lt.vitalikas.unsplash.domain.use_cases.SearchPhotosUseCase
import javax.inject.Inject

class SearchPhotosUseCaseImpl @Inject constructor(
    private val photosRepository: FeedPhotosRepository
) : SearchPhotosUseCase {
    override fun invoke(query: String): Flow<PagingData<SearchResult>> {
        return photosRepository.getSearchResult(query)
    }
}