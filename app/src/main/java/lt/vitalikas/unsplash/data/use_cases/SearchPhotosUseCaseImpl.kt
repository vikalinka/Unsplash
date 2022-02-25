package lt.vitalikas.unsplash.data.use_cases

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import lt.vitalikas.unsplash.domain.models.search.SearchPhoto
import lt.vitalikas.unsplash.domain.repositories.FeedPhotosRepository
import lt.vitalikas.unsplash.domain.use_cases.SearchPhotosUseCase
import javax.inject.Inject

class SearchPhotosUseCaseImpl @Inject constructor(
    private val photosRepository: FeedPhotosRepository
) : SearchPhotosUseCase {
    override suspend operator fun invoke(query: String): Flow<PagingData<SearchPhoto>> {
        return photosRepository.getSearchResult(query)
    }
}