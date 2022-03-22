package lt.vitalikas.unsplash.data.db.relations

import androidx.room.Embedded
import androidx.room.Relation
import lt.vitalikas.unsplash.data.db.entities.PhotoEntity
import lt.vitalikas.unsplash.data.db.entities.UserEntity
import lt.vitalikas.unsplash.data.db.contracts.PhotoContract
import lt.vitalikas.unsplash.data.db.contracts.UserContract

data class UserAndPhotoEntity(
    @Embedded
    val user: UserEntity,
    @Relation(
        parentColumn = UserContract.Columns.ID,
        entityColumn = PhotoContract.Columns.USER_ID
    )
    val photo: PhotoEntity
)