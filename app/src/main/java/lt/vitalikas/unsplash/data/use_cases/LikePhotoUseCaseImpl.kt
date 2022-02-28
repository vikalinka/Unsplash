package lt.vitalikas.unsplash.data.use_cases

import lt.vitalikas.unsplash.domain.repositories.PhotosRepository
import lt.vitalikas.unsplash.domain.use_cases.LikePhotoUseCase
import javax.inject.Inject

class LikePhotoUseCaseImpl @Inject constructor(
    private val photosRepository: PhotosRepository
) : LikePhotoUseCase {

    override suspend fun invoke(id: String) = photosRepository.likePhoto(id)
}