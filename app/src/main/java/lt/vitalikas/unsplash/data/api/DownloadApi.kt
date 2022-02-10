package lt.vitalikas.unsplash.data.api

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Url

interface DownloadApi {

    @GET
    suspend fun downloadFile(
        @Url url: String
    ): ResponseBody
}