package lt.vitalikas.unsplash.data.use_cases

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import lt.vitalikas.unsplash.domain.models.photo.Photo
import lt.vitalikas.unsplash.domain.models.search.SearchPhoto
import lt.vitalikas.unsplash.domain.repositories.PhotosRepository
import lt.vitalikas.unsplash.domain.use_cases.SearchPhotosUseCase
import javax.inject.Inject

class SearchPhotosUseCaseImpl @Inject constructor(
    private val photosRepository: PhotosRepository
) : SearchPhotosUseCase {
    override suspend operator fun invoke(query: String): Flow<PagingData<Photo>> {
        return photosRepository.getSearchResult(query)
    }
}