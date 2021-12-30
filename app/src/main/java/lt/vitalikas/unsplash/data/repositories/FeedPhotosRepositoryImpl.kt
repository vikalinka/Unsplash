package lt.vitalikas.unsplash.data.repositories

import lt.vitalikas.unsplash.data.apis.UnsplashApi
import lt.vitalikas.unsplash.domain.models.FeedPhoto
import lt.vitalikas.unsplash.domain.repositories.FeedPhotosRepository
import javax.inject.Inject

class FeedPhotosRepositoryImpl @Inject constructor(
    private val api: UnsplashApi
) : FeedPhotosRepository {

    override suspend fun getFeedPhotos(): List<FeedPhoto> = api.getFeedPhotos()
}