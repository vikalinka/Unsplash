package lt.vitalikas.unsplash.domain.models.photo_details

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import lt.vitalikas.unsplash.domain.models.base.Link
import lt.vitalikas.unsplash.domain.models.base.Url
import lt.vitalikas.unsplash.domain.models.UserCollection
import lt.vitalikas.unsplash.domain.models.user.User

@JsonClass(generateAdapter = true)
data class PhotoDetails(
    @Json(name = "id") val id: String,
    @Json(name = "created_at") val createdAt: String,
    @Json(name = "updated_at") val updatedAt: String,
    @Json(name = "width") val width: Int,
    @Json(name = "height") val height: Int,
    @Json(name = "color") val color: String,
    @Json(name = "blur_hash") val blurHash: String,
    @Json(name = "downloads") val downloads: Int,
    @Json(name = "likes") val likes: Int,
    @Json(name = "liked_by_user") val likedByUser: Boolean,
    @Json(name = "public_domain") val publicDomain: Boolean,
    @Json(name = "description") val description: String?,
    @Json(name = "exif") val exif: Exif,
    @Json(name = "location") val location: Location,
    @Json(name = "tags") val tags: List<Tag>,
    @Json(name = "current_user_collections") val currentUserCollections: List<UserCollection>,
    @Json(name = "urls") val url: Url,
    @Json(name = "links") val link: Link,
    @Json(name = "user") val user: User
)