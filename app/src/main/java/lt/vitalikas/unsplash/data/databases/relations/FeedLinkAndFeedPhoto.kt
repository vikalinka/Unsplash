package lt.vitalikas.unsplash.data.databases.relations

import androidx.room.Embedded
import androidx.room.Relation
import lt.vitalikas.unsplash.data.databases.entities.FeedLinkEntity
import lt.vitalikas.unsplash.data.databases.entities.FeedPhotoEntity
import lt.vitalikas.unsplash.data.databases.table_contracts.FeedLinksContract
import lt.vitalikas.unsplash.data.databases.table_contracts.FeedPhotosContract

data class FeedLinkAndFeedPhoto(
    @Embedded
    val link: FeedLinkEntity,
    @Relation(
        parentColumn = FeedLinksContract.Columns.ID,
        entityColumn = FeedPhotosContract.Columns.FEED_LINK_ID
    )
    val feedPhoto: FeedPhotoEntity
)