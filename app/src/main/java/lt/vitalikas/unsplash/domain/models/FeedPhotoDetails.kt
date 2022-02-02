package lt.vitalikas.unsplash.domain.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FeedPhotoDetails(
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
    val downloads: Int,
    val likes: Int,
    @Json(name = "liked_by_user")
    val likedByUser: Boolean,
    @Json(name = "public_domain")
    val publicDomain: Boolean,
    val description: String?,
    val exif: Exif,
    val location: Location,
    val tags: List<Tag>,
    @Json(name = "current_user_collections")
    val currentUserCollections: List<Collection>,
    val urls: Url,
    val links: Link,
    val user: User
) {
    @JsonClass(generateAdapter = true)
    data class Exif(
        val make: String?,
        val model: String?,
        val name: String?,
        @Json(name = "exposure_time")
        val exposureTime: String?,
        val aperture: String?,
        @Json(name = "focal_length")
        val focalLength: String?,
        val iso: Int?
    )

    @JsonClass(generateAdapter = true)
    data class Location(
        val city: String?,
        val country: String?,
        val position: Position
    ) {
        @JsonClass(generateAdapter = true)
        data class Position(
            val latitude: Double?,
            val longitude: Double?
        )
    }

    @JsonClass(generateAdapter = true)
    data class Tag(
        val title: String?
    )

    @JsonClass(generateAdapter = true)
    data class Collection(
        val id: Int,
        val title: String?,
        @Json(name = "published_at")
        val publishedAt: String?,
        @Json(name = "last_collected_at")
        val lastCollectedAt: String?,
        @Json(name = "updated_at")
        val updatedAt: String?,
        @Json(name = "cover_photo")
        val coverPhoto: String?,
        val user: String?
    )

    @JsonClass(generateAdapter = true)
    data class Url(
        val raw: String,
        val full: String,
        val regular: String,
        val small: String,
        val thumb: String
    )

    @JsonClass(generateAdapter = true)
    data class Link(
        val self: String,
        val html: String,
        val download: String,
        @Json(name = "download_location")
        val downloadLocation: String
    )

    @JsonClass(generateAdapter = true)
    data class User(
        val id: String,
        @Json(name = "updated_at")
        val updatedAt: String?,
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
        val links: Link,
        @Json(name = "profile_image")
        val profileImage: Image
    ) {
        @JsonClass(generateAdapter = true)
        data class Link(
            val self: String,
            val html: String,
            val photos: String,
            val likes: String,
            val portfolio: String
        )

        @JsonClass(generateAdapter = true)
        data class Image(
            val small: String,
            val medium: String,
            val large: String
        )
    }
}