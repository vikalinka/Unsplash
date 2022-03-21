package lt.vitalikas.unsplash.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import lt.vitalikas.unsplash.data.db.contracts.UserLinkContract

@Entity(tableName = UserLinkContract.TABLE_NAME)
data class UserLinkEntity(
    @PrimaryKey
    @ColumnInfo(name = UserLinkContract.Columns.ID) val id: String,
    @ColumnInfo(name = UserLinkContract.Columns.SELF) val self: String,
    @ColumnInfo(name = UserLinkContract.Columns.HTML) val html: String,
    @ColumnInfo(name = UserLinkContract.Columns.PHOTOS) val photos: String,
    @ColumnInfo(name = UserLinkContract.Columns.LIKES) val likes: String,
    @ColumnInfo(name = UserLinkContract.Columns.PORTFOLIO) val portfolio: String
)