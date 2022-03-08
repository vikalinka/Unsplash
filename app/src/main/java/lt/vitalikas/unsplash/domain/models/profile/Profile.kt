package lt.vitalikas.unsplash.domain.models.profile

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Profile(
    val id: String,
    val name: String,
    val username: String,
    @Json(name = "first_name")
    val firstname: String,
    @Json(name = "last_name")
    val lastname: String?,
    @Json(name = "twitter_username")
    val twitter: String?,
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
    val instagram: String?,
    val email: String,
    @Json(name = "profile_image")
    val image: ProfileImage,
    val photos: List<Photo>
) {
    @JsonClass(generateAdapter = true)
    data class ProfileImage(
        val small: String,
        val medium: String,
        val large: String
    )

    @JsonClass(generateAdapter = true)
    data class Photo(
        val id: String,
        @Json(name = "created_at")
        val created: String,
        val urls: PhotoUrl
    ) {
        @JsonClass(generateAdapter = true)
        data class PhotoUrl(
            val raw: String,
            val full: String,
            val regular: String,
            val small: String,
            val thumb: String
        )
    }
}