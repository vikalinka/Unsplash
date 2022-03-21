package lt.vitalikas.unsplash.data.db.entities

import android.icu.util.Calendar
import androidx.room.*
import lt.vitalikas.unsplash.data.db.contracts.PhotoContract
import lt.vitalikas.unsplash.data.db.converters.CalendarConverter

@Entity(
    tableName = PhotoContract.TABLE_NAME
//    foreignKeys = [
//        ForeignKey(
//            entity = UserEntity::class,
//            parentColumns = [
//                UsersContract.Columns.ID
//            ],
//            childColumns = [
//                FeedPhotosContract.Columns.USER_ID
//            ],
//            onDelete = ForeignKey.CASCADE
//        ),
//        ForeignKey(
//            entity = FeedUrlEntity::class,
//            parentColumns = [
//                FeedUrlsContract.Columns.ID
//            ],
//            childColumns = [
//                FeedPhotosContract.Columns.FEED_URL_ID
//            ],
//            onDelete = ForeignKey.CASCADE
//        ),
//        ForeignKey(
//            entity = FeedLinkEntity::class,
//            parentColumns = [
//                FeedLinksContract.Columns.ID
//            ],
//            childColumns = [
//                FeedPhotosContract.Columns.FEED_LINK_ID
//            ],
//            onDelete = ForeignKey.CASCADE
//        )
//    ]
)
@TypeConverters(CalendarConverter::class)
data class PhotoEntity(
    @PrimaryKey
    @ColumnInfo(name = PhotoContract.Columns.ID) val id: String,

    // fk
    @ColumnInfo(name = PhotoContract.Columns.USER_ID) val userId: String,
    // fk
    @ColumnInfo(name = PhotoContract.Columns.URL_ID) val feedUrlId: String,
    // fk
    @ColumnInfo(name = PhotoContract.Columns.LINK_ID) val feedLinkId: String,

    @ColumnInfo(name = PhotoContract.Columns.CREATED_AT) val createdAt: String,
    @ColumnInfo(name = PhotoContract.Columns.UPDATED_AT) val updatedAt: String,
    @ColumnInfo(name = PhotoContract.Columns.WIDTH) val width: Int,
    @ColumnInfo(name = PhotoContract.Columns.HEIGHT) val height: Int,
    @ColumnInfo(name = PhotoContract.Columns.COLOR) val color: String,
    @ColumnInfo(name = PhotoContract.Columns.BLUR_HASH) val blurHash: String,
    @ColumnInfo(name = PhotoContract.Columns.LIKES) val likes: Int,
    @ColumnInfo(name = PhotoContract.Columns.LIKED_BY_USER) val likedByUser: Boolean,
    @ColumnInfo(name = PhotoContract.Columns.DESCRIPTION) val description: String?,
    @ColumnInfo(name = PhotoContract.Columns.LAST_UPDATED_AT) val lastUpdatedAt: Calendar
)