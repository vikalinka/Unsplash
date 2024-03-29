package lt.vitalikas.unsplash.domain.models.search

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import lt.vitalikas.unsplash.domain.models.photo.Link
import lt.vitalikas.unsplash.domain.models.photo.Url
import lt.vitalikas.unsplash.domain.models.user.UserCollection
import lt.vitalikas.unsplash.domain.models.user.User

@JsonClass(generateAdapter = true)
data class SearchPhoto(
    @Json(name = "id") val id: String,
    @Json(name = "created_at") val createdAt: String,
    @Json(name = "updated_at") val updatedAt: String,
    @Json(name = "width") val width: Int,
    @Json(name = "height") val height: Int,
    @Json(name = "color") val color: String,
    @Json(name = "blur_hash") val blurHash: String,
    @Json(name = "likes") var likes: Int,
    @Json(name = "liked_by_user") var likedByUser: Boolean,
    @Json(name = "description") val description: String?,
    @Json(name = "user") val user: User,
    @Json(name = "current_user_collections") val userCollections: List<UserCollection>,
    @Json(name = "urls") val urls: Url,
    @Json(name = "links") val link: Link
)