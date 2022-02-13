package lt.vitalikas.unsplash.domain.models

import android.net.Uri

data class Photo(
    val id: Long,
    val uri: Uri,
    val name: String,
    val size: Int
)