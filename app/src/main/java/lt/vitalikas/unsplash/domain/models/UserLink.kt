package lt.vitalikas.unsplash.domain.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserLink(
    val self: String,
    val html: String,
    val photos: String,
    val likes: String,
    val portfolio: String
)