package lt.vitalikas.unsplash.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import lt.vitalikas.unsplash.data.db.contracts.FeedCollectionsContract
import lt.vitalikas.unsplash.data.db.contracts.PhotoContract
import lt.vitalikas.unsplash.data.db.contracts.UserContract

@Entity(
    tableName = FeedCollectionsContract.TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = PhotoEntity::class,
            parentColumns = [
                PhotoContract.Columns.ID
            ],
            childColumns = [
                FeedCollectionsContract.Columns.FEED_PHOTO_ID
            ],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = [
                UserContract.Columns.ID
            ],
            childColumns = [
                FeedCollectionsContract.Columns.USER_ID
            ],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class FeedCollectionEntity(
    @PrimaryKey
    @ColumnInfo(name = FeedCollectionsContract.Columns.ID)
    val id: String,

    // fk
    @ColumnInfo(name = FeedCollectionsContract.Columns.FEED_PHOTO_ID)
    val feedPhotoId: String,
    // fk
    @ColumnInfo(name = FeedCollectionsContract.Columns.USER_ID)
    val userId: String,

    @ColumnInfo(name = FeedCollectionsContract.Columns.TITLE)
    val title: String,
    @ColumnInfo(name = FeedCollectionsContract.Columns.PUBLISHED_AT)
    val publishedAt: String,
    @ColumnInfo(name = FeedCollectionsContract.Columns.LAST_COLLECTED_AT)
    val lastCollectedAt: String,
    @ColumnInfo(name = FeedCollectionsContract.Columns.UPDATED_AT)
    val updatedAt: String,
    @ColumnInfo(name = FeedCollectionsContract.Columns.COVER_PHOTO)
    val coverPhoto: String?
)