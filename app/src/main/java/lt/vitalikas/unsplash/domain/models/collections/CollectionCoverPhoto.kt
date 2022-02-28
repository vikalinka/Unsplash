package lt.vitalikas.unsplash.domain.models.collections

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CollectionCoverPhoto(
    @Json(name = "id")
    val id: String?,
    @Json(name = "width")
    val width: Int,
    @Json(name = "height")
    val height: Int,
    @Json(name = "color")
    val color: String?,
    @Json(name = "blur_hash")
    val blurHash: String?,
    @Json(name = "likes")
    val likes: Int,
    @Json(name = "liked_by_user")
    val likedByUser: Boolean,
    @Json(name = "description")
    val description: String?,
    @Json(name = "user")
    val user: CollectionCoverPhotoUser,
    @Json(name = "urls")
    val urls: CollectionCoverPhotoUrls,
    @Json(name = "links")
    val links: CollectionCoverPhotoLinks
)