package lt.vitalikas.unsplash.domain.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class User(
    @Json(name = "id") val id: String,
    @Json(name = "username") val username: String,
    @Json(name = "name") val name: String,
    @Json(name = "first_name") val firstName: String?,
    @Json(name = "last_name") val lastName: String?,
    @Json(name = "instagram_username") val instagramUsername: String?,
    @Json(name = "twitter_username") val twitterUsername: String?,
    @Json(name = "portfolio_url") val portfolioUrl: String?,
    @Json(name = "bio") val bio: String?,
    @Json(name = "location") val location: String?,
    @Json(name = "total_likes") val totalLikes: Long,
    @Json(name = "total_photos") val totalPhotos: Long,
    @Json(name = "total_collections") val totalCollections: Long,
    @Json(name = "profile_image") val userProfileImage: UserProfileImage,
    @Json(name = "links") val link: UserLink
)