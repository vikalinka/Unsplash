package lt.vitalikas.unsplash.domain.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Search(
    @Json(name = "total")
    val total: Long,
    @Json(name = "total_pages")
    val totalPages: Int,
    @Json(name = "results")
    val searchResults: List<SearchResult>
)

@JsonClass(generateAdapter = true)
data class SearchResult(
    @Json(name = "id")
    val id: String,
    @Json(name = "created_at")
    val createdAt: String,
    @Json(name = "width")
    val width: Int,
    @Json(name = "height")
    val height: Int,
    @Json(name = "color")
    val color: String,
    @Json(name = "blur_hash")
    val blurHash: String,
    @Json(name = "likes")
    var likes: Int,
    @Json(name = "liked_by_user")
    var likedByUser: Boolean,
    @Json(name = "description")
    val description: String,
    @Json(name = "user")
    val resultUser: ResultUser,
    @Json(name = "urls")
    val urls: ResultUrls,
    @Json(name = "links")
    val links: ResultLinks
)

@JsonClass(generateAdapter = true)
data class ResultUser(
    @Json(name = "id")
    val id: String,
    @Json(name = "username")
    val username: String,
    @Json(name = "name")
    val name: String,
    @Json(name = "first_name")
    val firstName: String,
    @Json(name = "last_name")
    val lastName: String,
    @Json(name = "instagram_username")
    val instagramUsername: String,
    @Json(name = "twitter_username")
    val twitterUsername: String,
    @Json(name = "portfolio_url")
    val portfolioUrl: String,
    @Json(name = "profile_image")
    val resultUserProfileImage: ResultUserProfileImage,
    @Json(name = "links")
    val resultUserLinks: ResultUserLinks
)

data class ResultUserProfileImage(
    @Json(name = "small")
    val small: String,
    @Json(name = "medium")
    val medium: String,
    @Json(name = "large")
    val large: String
)

data class ResultUserLinks(
    @Json(name = "self")
    val self: String,
    @Json(name = "html")
    val html: String,
    @Json(name = "photos")
    val photos: String,
    @Json(name = "likes")
    val likes: String
)

data class ResultUrls(
    @Json(name = "raw")
    val raw: String,
    @Json(name = "full")
    val full: String,
    @Json(name = "regular")
    val regular: String,
    @Json(name = "small")
    val small: String,
    @Json(name = "thumb")
    val thumb: String
)

data class ResultLinks(
    @Json(name = "self")
    val self: String,
    @Json(name = "html")
    val html: String,
    @Json(name = "download")
    val download: String
)