package lt.vitalikas.unsplash.data.db.relations

import androidx.room.Embedded
import androidx.room.Relation
import lt.vitalikas.unsplash.data.db.entities.PhotoEntity
import lt.vitalikas.unsplash.data.db.entities.UrlEntity
import lt.vitalikas.unsplash.data.db.contracts.PhotoContract
import lt.vitalikas.unsplash.data.db.contracts.UrlContract

data class UrlAndPhoto(
    @Embedded
    val url: UrlEntity,
    @Relation(
        parentColumn = UrlContract.Columns.ID,
        entityColumn = PhotoContract.Columns.URL_ID
    )
    val photo: PhotoEntity
)