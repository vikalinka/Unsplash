package lt.vitalikas.unsplash.domain.use_cases

import lt.vitalikas.unsplash.domain.models.FeedPhoto

interface InsertFeedPhotosUseCase {

    suspend fun invoke(feedPhotoEntities: List<FeedPhoto>)
}