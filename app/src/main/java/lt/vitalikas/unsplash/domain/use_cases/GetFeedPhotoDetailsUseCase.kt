package lt.vitalikas.unsplash.domain.use_cases

import lt.vitalikas.unsplash.domain.models.FeedPhotoDetails

interface GetFeedPhotoDetailsUseCase {
    suspend operator fun invoke(id: String): FeedPhotoDetails
}