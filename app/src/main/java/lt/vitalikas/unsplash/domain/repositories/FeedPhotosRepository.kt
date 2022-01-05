package lt.vitalikas.unsplash.domain.repositories

import lt.vitalikas.unsplash.domain.models.FeedPhoto
import lt.vitalikas.unsplash.domain.models.FeedPhotoDetails

interface FeedPhotosRepository {

    suspend fun getFeedPhotos(): List<FeedPhoto>
    suspend fun getFeedPhotoDetailsById(id: String): FeedPhotoDetails
}