package lt.vitalikas.unsplash.domain.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FeedUrl(
    val raw: String,
    val full: String,
    val regular: String,
    val small: String,
    val thumb: String
)