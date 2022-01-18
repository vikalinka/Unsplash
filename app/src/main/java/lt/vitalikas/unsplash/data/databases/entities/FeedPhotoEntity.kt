package lt.vitalikas.unsplash.data.databases.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import lt.vitalikas.unsplash.data.databases.table_contracts.FeedPhotosContract

@Entity(tableName = FeedPhotosContract.TABLE_NAME)
data class FeedPhotoEntity(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = FeedPhotosContract.Columns.ID)
    val id: String,
    @ColumnInfo(name = FeedPhotosContract.Columns.CREATED_AT)
    val createdAt: String,
    @ColumnInfo(name = FeedPhotosContract.Columns.UPDATED_AT)
    val updatedAt: String,
    @ColumnInfo(name = FeedPhotosContract.Columns.WIDTH)
    val width: Int,
    @ColumnInfo(name = FeedPhotosContract.Columns.HEIGHT)
    val height: Int,
    @ColumnInfo(name = FeedPhotosContract.Columns.COLOR)
    val color: String,
    @ColumnInfo(name = FeedPhotosContract.Columns.BLUR_HASH)
    val blurHash: String,
    @ColumnInfo(name = FeedPhotosContract.Columns.LIKES)
    val likes: Int,
    @ColumnInfo(name = FeedPhotosContract.Columns.LIKED_BY_USER)
    val likedByUser: Boolean,
    @ColumnInfo(name = FeedPhotosContract.Columns.DESCRIPTION)
    val description: String?
)