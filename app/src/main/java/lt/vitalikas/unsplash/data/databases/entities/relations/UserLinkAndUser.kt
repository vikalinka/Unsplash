package lt.vitalikas.unsplash.data.databases.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import lt.vitalikas.unsplash.data.databases.entities.UserEntity
import lt.vitalikas.unsplash.data.databases.entities.UserLinkEntity
import lt.vitalikas.unsplash.data.databases.table_contracts.UserLinksContract
import lt.vitalikas.unsplash.data.databases.table_contracts.UsersContract

data class UserLinkAndUser(
    @Embedded
    val userLink: UserLinkEntity,
    @Relation(
        parentColumn = UserLinksContract.Columns.ID,
        entityColumn = UsersContract.Columns.USER_LINK_ID
    )
    val user: UserEntity
)