package lt.vitalikas.unsplash.data.use_cases

import lt.vitalikas.unsplash.domain.repositories.FeedPhotosRepository
import lt.vitalikas.unsplash.domain.use_cases.DislikePhotoUseCase
import javax.inject.Inject

class DislikePhotoUseCaseImpl @Inject constructor(
    private val feedPhotosRepository: FeedPhotosRepository
) : DislikePhotoUseCase {

    override suspend fun invoke(id: String) = feedPhotosRepository.dislikePhoto(id)
}