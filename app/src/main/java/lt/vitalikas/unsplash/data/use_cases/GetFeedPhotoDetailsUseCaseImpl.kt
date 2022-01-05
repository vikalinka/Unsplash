package lt.vitalikas.unsplash.data.use_cases

import lt.vitalikas.unsplash.domain.models.FeedPhotoDetails
import lt.vitalikas.unsplash.domain.repositories.FeedPhotosRepository
import lt.vitalikas.unsplash.domain.use_cases.GetFeedPhotoDetailsUseCase
import javax.inject.Inject

class GetFeedPhotoDetailsUseCaseImpl @Inject constructor(
    private val photosRepository: FeedPhotosRepository
) : GetFeedPhotoDetailsUseCase {

    override var feedPhotoDetails: FeedPhotoDetails? = null

    override suspend fun invoke(id: String): FeedPhotoDetails =
        photosRepository.getFeedPhotoDetailsById(id).also {
            feedPhotoDetails = it
        }
}