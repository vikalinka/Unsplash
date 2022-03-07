package lt.vitalikas.unsplash.domain.models.photo_details

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Tag(
    @Json(name = "title") val title: String?
)