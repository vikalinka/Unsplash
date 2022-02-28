package lt.vitalikas.unsplash.data.use_cases

import lt.vitalikas.unsplash.domain.repositories.PhotosRepository
import lt.vitalikas.unsplash.domain.use_cases.DislikePhotoUseCase
import javax.inject.Inject

class DislikePhotoUseCaseImpl @Inject constructor(
    private val photosRepository: PhotosRepository
) : DislikePhotoUseCase {

    override suspend fun invoke(id: String) = photosRepository.dislikePhoto(id)
}