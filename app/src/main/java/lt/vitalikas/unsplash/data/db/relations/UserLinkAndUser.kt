package lt.vitalikas.unsplash.data.db.relations

import androidx.room.Embedded
import androidx.room.Relation
import lt.vitalikas.unsplash.data.db.entities.UserEntity
import lt.vitalikas.unsplash.data.db.entities.UserLinkEntity
import lt.vitalikas.unsplash.data.db.contracts.UserLinksContract
import lt.vitalikas.unsplash.data.db.contracts.UserContract

data class UserLinkAndUser(
    @Embedded
    val userLink: UserLinkEntity,
    @Relation(
        parentColumn = UserLinksContract.Columns.ID,
        entityColumn = UserContract.Columns.USER_LINK_ID
    )
    val user: UserEntity
)