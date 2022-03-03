package lt.vitalikas.unsplash.data.db.relations

import androidx.room.Embedded
import androidx.room.Relation
import lt.vitalikas.unsplash.data.db.entities.FeedPhotoEntity
import lt.vitalikas.unsplash.data.db.entities.FeedUrlEntity
import lt.vitalikas.unsplash.data.db.contracts.FeedPhotosContract
import lt.vitalikas.unsplash.data.db.contracts.FeedUrlsContract

data class FeedUrlAndFeedPhoto(
    @Embedded
    val url: FeedUrlEntity,
    @Relation(
        parentColumn = FeedUrlsContract.Columns.ID,
        entityColumn = FeedPhotosContract.Columns.FEED_URL_ID
    )
    val feedPhoto: FeedPhotoEntity?
)