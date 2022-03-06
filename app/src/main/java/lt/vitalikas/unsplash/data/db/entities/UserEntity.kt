package lt.vitalikas.unsplash.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import lt.vitalikas.unsplash.data.db.contracts.UsersContract

@Entity(
    tableName = UsersContract.TABLE_NAME
//    foreignKeys = [
//        ForeignKey(
//            entity = UserProfileImageEntity::class,
//            parentColumns = [
//                UserProfileImagesContract.Columns.ID
//            ],
//            childColumns = [
//                UsersContract.Columns.USER_PROFILE_IMAGE_ID
//            ],
//            onDelete = ForeignKey.CASCADE
//        ),
//        ForeignKey(
//            entity = UserLinkEntity::class,
//            parentColumns = [
//                UserLinksContract.Columns.ID
//            ],
//            childColumns = [
//                UsersContract.Columns.USER_LINK_ID
//            ],
//            onDelete = ForeignKey.CASCADE
//        )
//    ]
)
data class UserEntity(
    @PrimaryKey
    @ColumnInfo(name = UsersContract.Columns.ID)
    val id: String,

    // fk
    @ColumnInfo(name = UsersContract.Columns.USER_PROFILE_IMAGE_ID)
    val userProfileImageId: String,
    // fk
    @ColumnInfo(name = UsersContract.Columns.USER_LINK_ID)
    val userLinkId: String,

    @ColumnInfo(name = UsersContract.Columns.USERNAME)
    val username: String,
    @ColumnInfo(name = UsersContract.Columns.NAME)
    val name: String,
    @ColumnInfo(name = UsersContract.Columns.FIRST_NAME)
    val firstName: String,
    @ColumnInfo(name = UsersContract.Columns.LAST_NAME)
    val lastName: String,
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
    val twitter: String?
)