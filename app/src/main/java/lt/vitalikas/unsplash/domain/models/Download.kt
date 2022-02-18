package lt.vitalikas.unsplash.domain.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Download(
    @Json(name = "url")
    val url: String
)