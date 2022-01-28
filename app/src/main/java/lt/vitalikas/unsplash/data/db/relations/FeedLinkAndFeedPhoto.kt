package lt.vitalikas.unsplash.data.db.relations

import androidx.room.Embedded
import androidx.room.Relation
import lt.vitalikas.unsplash.data.db.entities.FeedLinkEntity
import lt.vitalikas.unsplash.data.db.entities.FeedPhotoEntity
import lt.vitalikas.unsplash.data.db.contracts.FeedLinksContract
import lt.vitalikas.unsplash.data.db.contracts.FeedPhotosContract

data class FeedLinkAndFeedPhoto(
    @Embedded
    val link: FeedLinkEntity,
    @Relation(
        parentColumn = FeedLinksContract.Columns.ID,
        entityColumn = FeedPhotosContract.Columns.FEED_LINK_ID
    )
    val feedPhoto: FeedPhotoEntity
)