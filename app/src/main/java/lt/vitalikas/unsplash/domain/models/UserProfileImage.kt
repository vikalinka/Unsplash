package lt.vitalikas.unsplash.domain.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserProfileImage(
    val small: String,
    val medium: String,
    val large: String
)