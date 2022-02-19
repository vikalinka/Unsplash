package lt.vitalikas.unsplash.domain.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FeedPhoto(
    val id: String,
    @Json(name = "created_at")
    val createdAt: String,
    @Json(name = "updated_at")
    val updatedAt: String,
    val width: Int,
    val height: Int,
    val color: String,
    @Json(name = "blur_hash")
    val blurHash: String,
    var likes: Int,
    @Json(name = "liked_by_user")
    var likedByUser: Boolean,
    val description: String?,
    val user: User,
    @Json(name = "current_user_collections")
    val currentUserFeedCollections: List<FeedCollection>,
    val urls: FeedUrl,
    val links: FeedLink
)