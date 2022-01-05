package lt.vitalikas.unsplash.domain.use_cases

import lt.vitalikas.unsplash.domain.models.FeedPhotoDetails

interface GetFeedPhotoDetailsUseCase {
    var feedPhotoDetails: FeedPhotoDetails?
    suspend fun invoke(id: String): FeedPhotoDetails
}