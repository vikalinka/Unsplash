package lt.vitalikas.unsplash.data.use_cases

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import lt.vitalikas.unsplash.domain.models.photo.Photo
import lt.vitalikas.unsplash.domain.repositories.PhotosRepository
import lt.vitalikas.unsplash.domain.use_cases.GetFeedPhotosUseCase
import javax.inject.Inject

class GetFeedPhotosUseCaseImpl @Inject constructor(
    private val photosRepository: PhotosRepository
) : GetFeedPhotosUseCase {
    override suspend fun invoke(order: String): Flow<PagingData<Photo>> =
        photosRepository.getFeedPhotos(order)
}