package lt.vitalikas.unsplash.data.databases.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import lt.vitalikas.unsplash.data.databases.entities.FeedCollectionEntity
import lt.vitalikas.unsplash.data.databases.entities.FeedPhotoEntity
import lt.vitalikas.unsplash.data.databases.table_contracts.FeedCollectionsContract
import lt.vitalikas.unsplash.data.databases.table_contracts.FeedPhotosContract

data class FeedPhotoWithFeedCollections(
    @Embedded
    val feedPhoto: FeedPhotoEntity,
    @Relation(
        parentColumn = FeedPhotosContract.Columns.ID,
        entityColumn = FeedCollectionsContract.Columns.FEED_PHOTO_ID
    )
    val collections: List<FeedCollectionEntity>
)
