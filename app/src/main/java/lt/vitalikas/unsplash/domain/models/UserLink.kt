package lt.vitalikas.unsplash.domain.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserLink(
    @Json(name = "self") val self: String,
    @Json(name = "html") val html: String,
    @Json(name = "photos") val photos: String,
    @Json(name = "likes") val likes: String,
    @Json(name = "portfolio") val portfolio: String
)