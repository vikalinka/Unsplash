package lt.vitalikas.unsplash.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import lt.vitalikas.unsplash.data.db.contracts.UserContract

@Entity(
    tableName = UserContract.TABLE_NAME
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
    @ColumnInfo(name = UserContract.Columns.ID) val id: String,

    // fk
    @ColumnInfo(name = UserContract.Columns.USER_PROFILE_IMAGE_ID)
    val userProfileImageId: String,
    // fk
    @ColumnInfo(name = UserContract.Columns.USER_LINK_ID)
    val userLinkId: String,

    @ColumnInfo(name = UserContract.Columns.USERNAME) val username: String,
    @ColumnInfo(name = UserContract.Columns.NAME) val name: String,
    @ColumnInfo(name = UserContract.Columns.FIRST_NAME) val firstName: String,
    @ColumnInfo(name = UserContract.Columns.LAST_NAME) val lastName: String?,
    @ColumnInfo(name = UserContract.Columns.INSTAGRAM_USERNAME) val instagramUsername: String?,
    @ColumnInfo(name = UserContract.Columns.TWITTER_USERNAME) val twitterUsername: String?,
    @ColumnInfo(name = UserContract.Columns.PORTFOLIO_URL) val portfolioUrl: String?,
    @ColumnInfo(name = UserContract.Columns.BIO) val bio: String?,
    @ColumnInfo(name = UserContract.Columns.LOCATION) val location: String?,
    @ColumnInfo(name = UserContract.Columns.TOTAL_LIKES) val totalLikes: Int,
    @ColumnInfo(name = UserContract.Columns.TOTAL_PHOTOS) val totalPhotos: Int,
    @ColumnInfo(name = UserContract.Columns.TOTAL_COLLECTIONS) val totalCollections: Int
)