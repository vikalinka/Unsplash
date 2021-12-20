package lt.vitalikas.unsplash.domain.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Profile(
    val id: String,
    val username: String,
    @Json(name = "first_name")
    val firstname: String,
    @Json(name = "last_name")
    val lastname: String,
    @Json(name = "twitter_username")
    val twitter: String,
    @Json(name = "portfolio_url")
    val portfolioUrl: String?,
    val bio: String,
    val location: String,
    @Json(name = "total_likes")
    val totalLikes: Long,
    @Json(name = "total_photos")
    val totalPhotos: Long,
    @Json(name = "total_collections")
    val totalCollections: Long,
    @Json(name = "followed_by_user")
    val followedByUser: Boolean,
    val downloads: Long,
    @Json(name = "instagram_username")
    val instagram: String,
    val email: String
)
