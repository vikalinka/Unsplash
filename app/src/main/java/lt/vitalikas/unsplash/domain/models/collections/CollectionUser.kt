package lt.vitalikas.unsplash.domain.models.collections

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CollectionUser(
    @Json(name = "id")
    val id: String,
    @Json(name = "updated_at")
    val updatedAt: String?,
    @Json(name = "username")
    val username: String?,
    @Json(name = "name")
    val name: String?,
    @Json(name = "portfolio_url")
    val portfolioUrl: String?,
    @Json(name = "bio")
    val bio: String?,
    @Json(name = "location")
    val location: String?,
    @Json(name = "total_likes")
    val totalLikes: Int,
    @Json(name = "total_photos")
    val totalPhotos: Int,
    @Json(name = "total_collections")
    val totalCollections: Int,
    @Json(name = "instagram_username")
    val instagramUsername: String?,
    @Json(name = "twitter_username")
    val twitterUsername: String?,
    @Json(name = "profile_image")
    val profileImage: CollectionCoverPhotoUserProfileImage,
    @Json(name = "links")
    val userLinks: CollectionCoverPhotoUserLinks
)