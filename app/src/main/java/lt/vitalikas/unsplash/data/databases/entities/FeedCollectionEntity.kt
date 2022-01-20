package lt.vitalikas.unsplash.data.databases.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import lt.vitalikas.unsplash.data.databases.table_contracts.FeedCollectionsContract
import lt.vitalikas.unsplash.data.databases.table_contracts.FeedPhotosContract

@Entity(
    tableName = FeedCollectionsContract.TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = FeedPhotoEntity::class,
            parentColumns = [
                FeedPhotosContract.Columns.ID
            ],
            childColumns = [
                FeedCollectionsContract.Columns.FEED_PHOTO_ID
            ],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class FeedCollectionEntity(
    @PrimaryKey
    @ColumnInfo(name = FeedCollectionsContract.Columns.ID)
    val id: Int,

    //fk
    @ColumnInfo(name = FeedCollectionsContract.Columns.ID)
    val feedPhotoId: String,

    @ColumnInfo(name = FeedCollectionsContract.Columns.TITLE)
    val title: String,
    @ColumnInfo(name = FeedCollectionsContract.Columns.PUBLISHED_AT)
    val publishedAt: String,
    @ColumnInfo(name = FeedCollectionsContract.Columns.LAST_COLLECTED_AT)
    val lastCollectedAt: String,
    @ColumnInfo(name = FeedCollectionsContract.Columns.UPDATED_AT)
    val updatedAt: String,
    @ColumnInfo(name = FeedCollectionsContract.Columns.COVER_PHOTO)
    val coverPhoto: String?,
    @ColumnInfo(name = FeedCollectionsContract.Columns.USER)
    val user: String?
)