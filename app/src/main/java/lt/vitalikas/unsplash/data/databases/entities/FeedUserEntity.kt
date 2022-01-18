package lt.vitalikas.unsplash.data.databases.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import lt.vitalikas.unsplash.data.databases.table_contracts.FeedUsersContract

@Entity(
    tableName = FeedUsersContract.TABLE_NAME
)
data class FeedUserEntity(
    @ColumnInfo(name = FeedUsersContract.Columns.FEED_PHOTO_ID)
    val feedPhotoId: String,

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = FeedUsersContract.Columns.ID)
    val id: String,
    @ColumnInfo(name = FeedUsersContract.Columns.USERNAME)
    val username: String,
    @ColumnInfo(name = FeedUsersContract.Columns.NAME)
    val name: String,
    @ColumnInfo(name = FeedUsersContract.Columns.PORTFOLIO_URL)
    val portfolioUrl: String?,
    @ColumnInfo(name = FeedUsersContract.Columns.BIO)
    val bio: String?,
    @ColumnInfo(name = FeedUsersContract.Columns.LOCATION)
    val location: String?,
    @ColumnInfo(name = FeedUsersContract.Columns.TOTAL_LIKES)
    val totalLikes: Long,
    @ColumnInfo(name = FeedUsersContract.Columns.TOTAL_PHOTOS)
    val totalPhotos: Long,
    @ColumnInfo(name = FeedUsersContract.Columns.TOTAL_COLLECTIONS)
    val totalCollections: Long,
    @ColumnInfo(name = FeedUsersContract.Columns.INSTAGRAM_USERNAME)
    val instagram: String?,
    @ColumnInfo(name = FeedUsersContract.Columns.TWITTER_USERNAME)
    val twitter: String?
)