package lt.vitalikas.unsplash.domain.models.user

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import lt.vitalikas.unsplash.domain.models.collections.CollectionCoverPhoto

@JsonClass(generateAdapter = true)
data class UserCollection(
    @Json(name = "id") val id: String,
    @Json(name = "title") val title: String,
    @Json(name = "published_at") val publishedAt: String,
    @Json(name = "last_collected_at") val lastCollectedAt: String,
    @Json(name = "updated_at") val updatedAt: String,
    @Json(name = "cover_photo") val coverPhoto: CollectionCoverPhoto?,
    @Json(name = "user") val user: User?
)