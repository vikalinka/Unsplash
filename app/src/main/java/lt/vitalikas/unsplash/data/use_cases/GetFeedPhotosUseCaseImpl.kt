package lt.vitalikas.unsplash.data.use_cases

import lt.vitalikas.unsplash.domain.models.FeedPhoto
import lt.vitalikas.unsplash.domain.repositories.FeedPhotosRepository
import lt.vitalikas.unsplash.domain.use_cases.GetFeedPhotosUseCase
import javax.inject.Inject

class GetFeedPhotosUseCaseImpl @Inject constructor(
    private val photosRepository: FeedPhotosRepository
) : GetFeedPhotosUseCase {
    override var feedPhotos: List<FeedPhoto>? = null

    override suspend fun invoke(): List<FeedPhoto> = photosRepository.getFeedPhotos().also {
        feedPhotos = it
    }
}