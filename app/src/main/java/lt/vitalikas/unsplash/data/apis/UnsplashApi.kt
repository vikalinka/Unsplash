package lt.vitalikas.unsplash.data.apis

import lt.vitalikas.unsplash.domain.models.User
import retrofit2.http.GET

interface UnsplashApi {

    @GET("/me")
    suspend fun getUser(): User
}