package lt.vitalikas.unsplash.data.databases.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import lt.vitalikas.unsplash.data.databases.entities.FeedPhotoEntity
import lt.vitalikas.unsplash.data.databases.entities.FeedUrlEntity
import lt.vitalikas.unsplash.data.databases.table_contracts.FeedPhotosContract
import lt.vitalikas.unsplash.data.databases.table_contracts.FeedUrlsContract

data class FeedUrlAndFeedPhoto(
    @Embedded
    val user: FeedUrlEntity,
    @Relation(
        parentColumn = FeedUrlsContract.Columns.ID,
        entityColumn = FeedPhotosContract.Columns.FEED_URL_ID
    )
    val feedPhoto: FeedPhotoEntity
)