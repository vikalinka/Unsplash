package lt.vitalikas.unsplash.data.databases.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import lt.vitalikas.unsplash.data.databases.table_contracts.UsersContract
import lt.vitalikas.unsplash.domain.models.UserLink
import lt.vitalikas.unsplash.domain.models.UserProfileImage

@Entity(tableName = UsersContract.TABLE_NAME)
data class UserEntity(
    @PrimaryKey
    @ColumnInfo(name = UsersContract.Columns.ID)
    val id: String,
    @ColumnInfo(name = UsersContract.Columns.USERNAME)
    val username: String,
    @ColumnInfo(name = UsersContract.Columns.NAME)
    val name: String,
    @ColumnInfo(name = UsersContract.Columns.PORTFOLIO_URL)
    val portfolioUrl: String?,
    @ColumnInfo(name = UsersContract.Columns.BIO)
    val bio: String?,
    @ColumnInfo(name = UsersContract.Columns.LOCATION)
    val location: String?,
    @ColumnInfo(name = UsersContract.Columns.TOTAL_LIKES)
    val totalLikes: Long,
    @ColumnInfo(name = UsersContract.Columns.TOTAL_PHOTOS)
    val totalPhotos: Long,
    @ColumnInfo(name = UsersContract.Columns.TOTAL_COLLECTIONS)
    val totalCollections: Long,
    @ColumnInfo(name = UsersContract.Columns.INSTAGRAM_USERNAME)
    val instagram: String?,
    @ColumnInfo(name = UsersContract.Columns.TWITTER_USERNAME)
    val twitter: String?,
//    @ColumnInfo(name = UsersContract.Columns.PROFILE_IMAGE)
//    val imageUser: UserProfileImage,
//    @ColumnInfo(name = UsersContract.Columns.LINKS)
//    val links: UserLink
)