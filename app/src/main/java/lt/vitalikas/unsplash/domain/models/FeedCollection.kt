package lt.vitalikas.unsplash.domain.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FeedCollection(
    val id: Int,
    val title: String,
    @Json(name = "published_at")
    val publishedAt: String,
    @Json(name = "last_collected_at")
    val lastCollectedAt: String,
    @Json(name = "updated_at")
    val updatedAt: String,
    @Json(name = "cover_photo")
    val coverPhoto: String?,
    val user: User
)