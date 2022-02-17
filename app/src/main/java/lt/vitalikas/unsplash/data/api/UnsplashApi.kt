package lt.vitalikas.unsplash.data.api

import lt.vitalikas.unsplash.domain.models.FeedPhoto
import lt.vitalikas.unsplash.domain.models.FeedPhotoDetails
import lt.vitalikas.unsplash.domain.models.Profile
import retrofit2.http.*

interface UnsplashApi {

    @GET("/me")
    suspend fun getCurrentProfile(): Profile

    @GET("/photos")
    suspend fun getFeedPhotos(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Query("order_by") orderBy: String
    ): List<FeedPhoto>

    @GET("/photos/{id}")
    suspend fun getFeedPhotoDetails(
        @Path("id") id: String
    ): FeedPhotoDetails

    @GET("/photos/{id}/download")
    suspend fun trackDownload(
        @Path("id") id: String
    )

    @POST("/photos/{id}/like")
    suspend fun likePhoto(
        @Path("id") id: String
    )

    @DELETE("/photos/{id}/like")
    suspend fun dislikePhoto(
        @Path("id") id: String
    )
}