package lt.vitalikas.unsplash.data.apis

import lt.vitalikas.unsplash.domain.models.FeedPhoto
import lt.vitalikas.unsplash.domain.models.FeedPhotoDetails
import lt.vitalikas.unsplash.domain.models.Profile
import retrofit2.http.GET
import retrofit2.http.Path

interface UnsplashApi {

    @GET("/me")
    suspend fun getCurrentProfile(): Profile

    @GET("/photos")
    suspend fun getFeedPhotos(): List<FeedPhoto>

    @GET("/photos/{id}")
    suspend fun getFeedPhotoDetails(
        @Path("id") id: String
    ): FeedPhotoDetails
}