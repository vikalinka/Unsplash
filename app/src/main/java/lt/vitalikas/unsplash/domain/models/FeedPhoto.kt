package lt.vitalikas.unsplash.domain.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FeedPhoto(
    val id: String,
    @Json(name = "created_at")
    val createdAt: String,
    @Json(name = "updated_at")
    val updatedAt: String,
    val width: Int,
    val height: Int,
    val color: String,
    @Json(name = "blur_hash")
    val blurHash: String,
    val likes: Int,
    @Json(name = "liked_by_user")
    val likedByUser: Boolean,
    val description: String?,
    val user: User,
    @Json(name = "current_user_collections")
    val currentUserFeedCollections: List<FeedCollection>,
    val urls: FeedUrl,
    val links: FeedLink
) {

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
        val image: ProfileImage,
        val links: Link
    ) {

        @JsonClass(generateAdapter = true)
        data class ProfileImage(
            val small: String,
            val medium: String,
            val large: String
        )

        @JsonClass(generateAdapter = true)
        data class Link(
            val self: String,
            val html: String,
            val photos: String,
            val likes: String,
            val portfolio: String
        )
    }

    @JsonClass(generateAdapter = true)
    data class FeedCollection(
        val id: Int,
        val title: String,
        @Json(name = "published_at")
        val publishedAt: String,
        @Json(name = "last_collected_at")
        val lastCollectedAt: String,
        @Json(name = "updated_at")
        val updatedAt: String,
        @Json(name = "cover_photo")
        val coverPhoto: String?,
        val user: String?
    )

    @JsonClass(generateAdapter = true)
    data class FeedUrl(
        val raw: String,
        val full: String,
        val regular: String,
        val small: String,
        val thumb: String
    )

    @JsonClass(generateAdapter = true)
    data class FeedLink(
        val self: String,
        val html: String,
        val download: String,
        @Json(name = "download_location")
        val downloadLocation: String
    )
}