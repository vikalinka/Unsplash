package lt.vitalikas.unsplash.data.api

import lt.vitalikas.unsplash.domain.models.*
import lt.vitalikas.unsplash.domain.models.collections.Collection
import lt.vitalikas.unsplash.domain.models.collections.CollectionPhoto
import lt.vitalikas.unsplash.domain.models.collections.CollectionResponse
import lt.vitalikas.unsplash.domain.models.search.SearchResponse
import okhttp3.ResponseBody
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

    @GET
    suspend fun trackDownload(
        @Url url: String
    ): Download

    @GET
    suspend fun downloadPhoto(
        @Url url: String
    ): ResponseBody

    @POST("/photos/{id}/like")
    suspend fun likePhoto(
        @Path("id") id: String
    )

    @DELETE("/photos/{id}/like")
    suspend fun dislikePhoto(
        @Path("id") id: String
    )

    @GET("/search/photos")
    suspend fun searchPhotos(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Query("order_by") orderBy: String
    ): SearchResponse

    @GET("/collections")
    suspend fun getCollections(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): List<CollectionResponse>

    @GET("/collections/{id}")
    suspend fun getCollection(
        @Path("id") id: String
    ): Collection

    @GET("/collections/{id}/photos")
    suspend fun getCollectionPhotos(
        @Path("id") id: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): List<CollectionPhoto>
}