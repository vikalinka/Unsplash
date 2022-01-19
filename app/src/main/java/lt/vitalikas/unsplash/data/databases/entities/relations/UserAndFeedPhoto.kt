package lt.vitalikas.unsplash.data.databases.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import lt.vitalikas.unsplash.data.databases.entities.FeedPhotoEntity
import lt.vitalikas.unsplash.data.databases.entities.UserEntity
import lt.vitalikas.unsplash.data.databases.table_contracts.FeedPhotosContract
import lt.vitalikas.unsplash.data.databases.table_contracts.UsersContract
import lt.vitalikas.unsplash.domain.models.FeedPhoto
import lt.vitalikas.unsplash.domain.models.User

data class UserAndFeedPhoto(
    @Embedded
    val user: UserEntity,
    @Relation(
        parentColumn = UsersContract.Columns.ID,
        entityColumn = FeedPhotosContract.Columns.USER_ID
    )
    val feedPhoto: FeedPhotoEntity
)