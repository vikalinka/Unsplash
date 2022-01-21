package lt.vitalikas.unsplash.data.databases.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import lt.vitalikas.unsplash.data.databases.entities.FeedCollectionEntity
import lt.vitalikas.unsplash.data.databases.entities.UserEntity
import lt.vitalikas.unsplash.data.databases.table_contracts.FeedCollectionsContract
import lt.vitalikas.unsplash.data.databases.table_contracts.UsersContract

data class FeedCollectionAndUser(
    @Embedded
    val user: UserEntity,
    @Relation(
        parentColumn = UsersContract.Columns.ID,
        entityColumn = FeedCollectionsContract.Columns.USER_ID
    )
    val feedCollection: FeedCollectionEntity
)