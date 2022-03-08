package lt.vitalikas.unsplash.domain.models.collections

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import lt.vitalikas.unsplash.domain.models.user.User

@JsonClass(generateAdapter = true)
data class Collection(
    @Json(name = "id") val id: String,
    @Json(name = "title") val title: String,
    @Json(name = "description") val description: String?,
    @Json(name = "published_at") val publishedAt: String,
    @Json(name = "last_collected_at") val lastCollectedAt: String,
    @Json(name = "updated_at") val updatedAt: String,
    @Json(name = "featured") val featured: Boolean,
    @Json(name = "total_photos") val totalPhotos: Int,
    @Json(name = "private") val private: Boolean,
    @Json(name = "share_key") val shareKey: String,
    @Json(name = "cover_photo") val coverPhoto: CollectionCoverPhoto?,
    @Json(name = "user") val user: User?
)