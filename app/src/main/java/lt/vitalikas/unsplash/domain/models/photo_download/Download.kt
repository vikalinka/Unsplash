package lt.vitalikas.unsplash.domain.models.photo_download

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Download(
    @Json(name = "url")
    val url: String
)