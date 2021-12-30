package lt.vitalikas.unsplash.domain.use_cases

import lt.vitalikas.unsplash.domain.models.FeedPhoto

interface GetFeedPhotosUseCase {
    var feedPhotos: List<FeedPhoto>?
    suspend fun invoke(): List<FeedPhoto>
}