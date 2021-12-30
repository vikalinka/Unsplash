package lt.vitalikas.unsplash.domain.repositories

import lt.vitalikas.unsplash.domain.models.FeedPhoto

interface FeedPhotosRepository {

    suspend fun getFeedPhotos(): List<FeedPhoto>
}