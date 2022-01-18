package lt.vitalikas.unsplash.domain.use_cases

import lt.vitalikas.unsplash.data.databases.entities.FeedPhotoEntity

interface InsertFeedPhotosUseCase {

    suspend fun invoke(feedPhotos: List<FeedPhotoEntity>)
}