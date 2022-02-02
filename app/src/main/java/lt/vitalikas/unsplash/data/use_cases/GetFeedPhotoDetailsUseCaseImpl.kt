package lt.vitalikas.unsplash.data.use_cases

import kotlinx.coroutines.flow.Flow
import lt.vitalikas.unsplash.domain.models.FeedPhotoDetails
import lt.vitalikas.unsplash.domain.repositories.FeedPhotosRepository
import lt.vitalikas.unsplash.domain.use_cases.GetFeedPhotoDetailsUseCase
import javax.inject.Inject

class GetFeedPhotoDetailsUseCaseImpl @Inject constructor(
    private val photosRepository: FeedPhotosRepository
) : GetFeedPhotoDetailsUseCase {
    override suspend fun invoke(id: String): Flow<FeedPhotoDetails> =
        photosRepository.getFeedPhotoDetailsById(id)
}