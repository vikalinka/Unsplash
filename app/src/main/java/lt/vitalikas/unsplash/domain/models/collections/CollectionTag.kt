package lt.vitalikas.unsplash.domain.models.collections

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CollectionTag(
    @Json(name = "type") val type: String,
    @Json(name = "title") val title: String
)