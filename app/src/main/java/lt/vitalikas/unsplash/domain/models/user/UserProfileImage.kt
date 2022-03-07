package lt.vitalikas.unsplash.domain.models.user

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserProfileImage(
    @Json(name = "small") val small: String,
    @Json(name = "medium") val medium: String,
    @Json(name = "large") val large: String
)