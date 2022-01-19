package lt.vitalikas.unsplash.domain.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class User(
    val id: String,
    val username: String,
    val name: String,
    @Json(name = "portfolio_url")
    val portfolioUrl: String?,
    val bio: String?,
    val location: String?,
    @Json(name = "total_likes")
    val totalLikes: Long,
    @Json(name = "total_photos")
    val totalPhotos: Long,
    @Json(name = "total_collections")
    val totalCollections: Long,
    @Json(name = "instagram_username")
    val instagram: String?,
    @Json(name = "twitter_username")
    val twitter: String?,
    @Json(name = "profile_image")
    val imageUser: UserProfileImage,
    val links: UserLink
)