package lt.vitalikas.unsplash.domain.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FeedLink(
    val self: String,
    val html: String,
    val download: String,
    @Json(name = "download_location")
    val downloadLocation: String
)