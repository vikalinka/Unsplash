package lt.vitalikas.unsplash.data.db.relations

import androidx.room.Embedded
import androidx.room.Relation
import lt.vitalikas.unsplash.data.db.entities.FeedCollectionEntity
import lt.vitalikas.unsplash.data.db.entities.UserEntity
import lt.vitalikas.unsplash.data.db.contracts.FeedCollectionsContract
import lt.vitalikas.unsplash.data.db.contracts.UserContract

data class FeedCollectionAndUser(
    @Embedded
    val user: UserEntity,
    @Relation(
        parentColumn = UserContract.Columns.ID,
        entityColumn = FeedCollectionsContract.Columns.USER_ID
    )
    val feedCollection: FeedCollectionEntity
)