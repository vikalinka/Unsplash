package lt.vitalikas.unsplash.data.db.relations

import androidx.room.Embedded
import androidx.room.Relation
import lt.vitalikas.unsplash.data.db.entities.LinkEntity
import lt.vitalikas.unsplash.data.db.entities.PhotoEntity
import lt.vitalikas.unsplash.data.db.contracts.LinkContract
import lt.vitalikas.unsplash.data.db.contracts.PhotoContract

data class LinkAndPhoto(
    @Embedded
    val link: LinkEntity,
    @Relation(
        parentColumn = LinkContract.Columns.ID,
        entityColumn = PhotoContract.Columns.LINK_ID
    )
    val photo: PhotoEntity?
)