package lt.vitalikas.unsplash.data.db.relations

import androidx.room.Embedded
import androidx.room.Relation
import lt.vitalikas.unsplash.data.db.entities.FeedCollectionEntity
import lt.vitalikas.unsplash.data.db.entities.PhotoEntity
import lt.vitalikas.unsplash.data.db.contracts.FeedCollectionsContract
import lt.vitalikas.unsplash.data.db.contracts.PhotoContract

data class PhotoWithCurrentUserCollections(
    @Embedded
    val photo: PhotoEntity,
    @Relation(
        parentColumn = PhotoContract.Columns.ID,
        entityColumn = FeedCollectionsContract.Columns.FEED_PHOTO_ID
    )
    val collections: List<FeedCollectionEntity>
)