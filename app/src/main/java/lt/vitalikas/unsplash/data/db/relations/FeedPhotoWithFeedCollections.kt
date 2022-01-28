package lt.vitalikas.unsplash.data.db.relations

import androidx.room.Embedded
import androidx.room.Relation
import lt.vitalikas.unsplash.data.db.entities.FeedCollectionEntity
import lt.vitalikas.unsplash.data.db.entities.FeedPhotoEntity
import lt.vitalikas.unsplash.data.db.contracts.FeedCollectionsContract
import lt.vitalikas.unsplash.data.db.contracts.FeedPhotosContract

data class FeedPhotoWithFeedCollections(
    @Embedded
    val feedPhoto: FeedPhotoEntity,
    @Relation(
        parentColumn = FeedPhotosContract.Columns.ID,
        entityColumn = FeedCollectionsContract.Columns.FEED_PHOTO_ID
    )
    val collections: List<FeedCollectionEntity>
)