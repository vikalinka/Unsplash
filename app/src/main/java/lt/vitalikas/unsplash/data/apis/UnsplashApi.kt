package lt.vitalikas.unsplash.data.apis

import lt.vitalikas.unsplash.domain.models.Profile
import retrofit2.http.GET

interface UnsplashApi {

    @GET("/me")
    suspend fun getCurrentProfile(): Profile
}