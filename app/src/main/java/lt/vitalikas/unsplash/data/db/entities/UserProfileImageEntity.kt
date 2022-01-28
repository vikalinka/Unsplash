package lt.vitalikas.unsplash.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import lt.vitalikas.unsplash.data.db.contracts.UserProfileImagesContract

@Entity(tableName = UserProfileImagesContract.TABLE_NAME)
data class UserProfileImageEntity(
    @PrimaryKey
    @ColumnInfo(name = UserProfileImagesContract.Columns.ID)
    val id: String,
    @ColumnInfo(name = UserProfileImagesContract.Columns.SMALL)
    val small: String,
    @ColumnInfo(name = UserProfileImagesContract.Columns.MEDIUM)
    val medium: String,
    @ColumnInfo(name = UserProfileImagesContract.Columns.LARGE)
    val large: String
)