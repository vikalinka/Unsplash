package lt.vitalikas.unsplash.data.use_cases

import lt.vitalikas.unsplash.domain.repositories.FeedPhotosRepository
import lt.vitalikas.unsplash.domain.use_cases.LikePhotoUseCase
import javax.inject.Inject

class LikePhotoUseCaseImpl @Inject constructor(
    private val feedPhotosRepository: FeedPhotosRepository
) : LikePhotoUseCase {

    override suspend fun invoke(id: String) = feedPhotosRepository.likePhoto(id)
}