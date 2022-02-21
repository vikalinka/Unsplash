package lt.vitalikas.unsplash.data.use_cases

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import lt.vitalikas.unsplash.domain.models.FeedPhoto
import lt.vitalikas.unsplash.domain.repositories.FeedPhotosRepository
import lt.vitalikas.unsplash.domain.use_cases.SearchFeedPhotosUseCase
import javax.inject.Inject

class SearchFeedPhotosUseCaseImpl @Inject constructor(
    private val photosRepository: FeedPhotosRepository
) : SearchFeedPhotosUseCase {
    override suspend fun invoke(query: String): Flow<PagingData<FeedPhoto>> {
        return photosRepository.searchPhotos(query)
    }
}