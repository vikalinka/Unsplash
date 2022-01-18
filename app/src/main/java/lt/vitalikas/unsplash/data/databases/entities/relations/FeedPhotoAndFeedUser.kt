package lt.vitalikas.unsplash.data.databases.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import lt.vitalikas.unsplash.data.databases.entities.FeedPhotoEntity
import lt.vitalikas.unsplash.data.databases.entities.FeedUserEntity
import lt.vitalikas.unsplash.data.databases.table_contracts.FeedPhotosContract
import lt.vitalikas.unsplash.data.databases.table_contracts.FeedUsersContract


data class FeedPhotoAndFeedUser(
    @Embedded
    val feedPhoto: FeedPhotoEntity,
    @Relation(
        parentColumn = FeedPhotosContract.Columns.ID,
        entityColumn = FeedUsersContract.Columns.FEED_PHOTO_ID
    )
    val feedUser: FeedUserEntity
)