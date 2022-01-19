package lt.vitalikas.unsplash.data.use_cases

import lt.vitalikas.unsplash.domain.models.FeedPhoto
import lt.vitalikas.unsplash.domain.repositories.FeedPhotosRepository
import lt.vitalikas.unsplash.domain.use_cases.InsertFeedPhotosUseCase
import javax.inject.Inject

class InsertFeedPhotosUseCaseImpl @Inject constructor(
    private val photosRepository: FeedPhotosRepository
) : InsertFeedPhotosUseCase {

    override suspend fun invoke(feedPhotos: List<FeedPhoto>) =
        photosRepository.insertFeedPhotos(feedPhotos)
}