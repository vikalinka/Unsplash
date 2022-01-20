package lt.vitalikas.unsplash.data.databases.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import lt.vitalikas.unsplash.data.databases.table_contracts.FeedLinksContract
import lt.vitalikas.unsplash.data.databases.table_contracts.FeedPhotosContract
import lt.vitalikas.unsplash.data.databases.table_contracts.FeedUrlsContract
import lt.vitalikas.unsplash.data.databases.table_contracts.UsersContract

@Entity(
    tableName = FeedPhotosContract.TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = [
                UsersContract.Columns.ID
            ],
            childColumns = [
                FeedPhotosContract.Columns.USER_ID
            ],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = FeedUrlEntity::class,
            parentColumns = [
                FeedUrlsContract.Columns.ID
            ],
            childColumns = [
                FeedPhotosContract.Columns.FEED_URL_ID
            ],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = FeedLinkEntity::class,
            parentColumns = [
                FeedLinksContract.Columns.ID
            ],
            childColumns = [
                FeedPhotosContract.Columns.FEED_LINK_ID
            ],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class FeedPhotoEntity(
    @PrimaryKey
    @ColumnInfo(name = FeedPhotosContract.Columns.ID)
    val id: String,

    // fk
    @ColumnInfo(name = FeedPhotosContract.Columns.USER_ID)
    val userId: String,
    // fk
    @ColumnInfo(name = FeedPhotosContract.Columns.FEED_URL_ID)
    val feedUrlId: Long,
    // fk
    @ColumnInfo(name = FeedPhotosContract.Columns.FEED_LINK_ID)
    val feedLinkId: Long,

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