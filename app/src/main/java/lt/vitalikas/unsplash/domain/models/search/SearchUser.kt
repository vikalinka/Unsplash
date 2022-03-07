package lt.vitalikas.unsplash.domain.models.search

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import lt.vitalikas.unsplash.domain.models.user.UserProfileImage

@JsonClass(generateAdapter = true)
data class SearchUser(
    @Json(name = "id")
    val id: String,
    @Json(name = "username")
    val username: String?,
    @Json(name = "name")
    val name: String?,
    @Json(name = "first_name")
    val firstName: String?,
    @Json(name = "last_name")
    val lastName: String?,
    @Json(name = "instagram_username")
    val instagramUsername: String?,
    @Json(name = "twitter_username")
    val twitterUsername: String?,
    @Json(name = "portfolio_url")
    val portfolioUrl: String?,
    @Json(name = "profile_image")
    val userProfileImage: UserProfileImage,
    @Json(name = "links")
    val searchUserLinks: SearchUserLinks
)