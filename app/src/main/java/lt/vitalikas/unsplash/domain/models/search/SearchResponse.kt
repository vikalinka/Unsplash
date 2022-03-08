package lt.vitalikas.unsplash.domain.models.search

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import lt.vitalikas.unsplash.domain.models.photo.Photo

@JsonClass(generateAdapter = true)
data class SearchResponse(
    @Json(name = "total") val total: Int,
    @Json(name = "total_pages") val totalPages: Int,
    @Json(name = "results") val results: List<Photo>
)