package lt.vitalikas.unsplash.domain.models.collections

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import lt.vitalikas.unsplash.domain.models.base.Link
import lt.vitalikas.unsplash.domain.models.base.Url
import lt.vitalikas.unsplash.domain.models.user.User

@JsonClass(generateAdapter = true)
data class CollectionCoverPhoto(
    @Json(name = "id") val id: String,
    @Json(name = "width") val width: Int,
    @Json(name = "height") val height: Int,
    @Json(name = "color") val color: String,
    @Json(name = "blur_hash") val blurHash: String,
    @Json(name = "likes") val likes: Int,
    @Json(name = "liked_by_user") val likedByUser: Boolean,
    @Json(name = "description") val description: String?,
    @Json(name = "user") val user: User,
    @Json(name = "urls") val urls: Url,
    @Json(name = "links") val links: Link
)