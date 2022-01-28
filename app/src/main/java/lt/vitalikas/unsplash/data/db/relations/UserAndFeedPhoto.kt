package lt.vitalikas.unsplash.data.db.relations

import androidx.room.Embedded
import androidx.room.Relation
import lt.vitalikas.unsplash.data.db.entities.FeedPhotoEntity
import lt.vitalikas.unsplash.data.db.entities.UserEntity
import lt.vitalikas.unsplash.data.db.contracts.FeedPhotosContract
import lt.vitalikas.unsplash.data.db.contracts.UsersContract

data class UserAndFeedPhoto(
    @Embedded
    val user: UserEntity,
    @Relation(
        parentColumn = UsersContract.Columns.ID,
        entityColumn = FeedPhotosContract.Columns.USER_ID
    )
    val feedPhoto: FeedPhotoEntity
)