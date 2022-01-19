package lt.vitalikas.unsplash.data.databases.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import lt.vitalikas.unsplash.data.databases.table_contracts.FeedPhotosContract
import lt.vitalikas.unsplash.data.databases.table_contracts.UsersContract
import lt.vitalikas.unsplash.domain.models.FeedCollection
import lt.vitalikas.unsplash.domain.models.FeedLink
import lt.vitalikas.unsplash.domain.models.FeedUrl
import lt.vitalikas.unsplash.domain.models.User

@JsonClass(generateAdapter = true)
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
        )
    ]
)
data class FeedPhotoEntity(
    @PrimaryKey
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

    @Json(name = "liked_by_user")
    @ColumnInfo(name = FeedPhotosContract.Columns.LIKED_BY_USER)
    val likedByUser: Boolean,

    @ColumnInfo(name = FeedPhotosContract.Columns.DESCRIPTION)
    val description: String?,

    @ColumnInfo(name = FeedPhotosContract.Columns.USER_ID)
    val userId: String

//    val currentUserFeedCollections: List<FeedCollection>,
//    val urls: FeedUrl,
//    val links: FeedLink
)