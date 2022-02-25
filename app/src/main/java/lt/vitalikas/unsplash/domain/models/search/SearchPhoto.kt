package lt.vitalikas.unsplash.domain.models.search

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchPhoto(
    @Json(name = "id")
    val id: String,
    @Json(name = "created_at")
    val createdAt: String,
    @Json(name = "width")
    val width: Int,
    @Json(name = "height")
    val height: Int,
    @Json(name = "color")
    val color: String,
    @Json(name = "blur_hash")
    val blurHash: String,
    @Json(name = "likes")
    var likes: Int,
    @Json(name = "liked_by_user")
    var likedByUser: Boolean,
    @Json(name = "description")
    val description: String?,
    @Json(name = "user")
    val user: SearchUser,
    @Json(name = "urls")
    val urls: SearchUrls,
    @Json(name = "links")
    val links: SearchLinks
)