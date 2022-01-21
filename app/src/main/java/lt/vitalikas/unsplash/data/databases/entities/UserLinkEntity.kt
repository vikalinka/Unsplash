package lt.vitalikas.unsplash.data.databases.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import lt.vitalikas.unsplash.data.databases.table_contracts.UserLinksContract

@Entity(tableName = UserLinksContract.TABLE_NAME)
data class UserLinkEntity(
    @PrimaryKey
    @ColumnInfo(name = UserLinksContract.Columns.ID)
    val id: String,
    @ColumnInfo(name = UserLinksContract.Columns.SELF)
    val self: String,
    @ColumnInfo(name = UserLinksContract.Columns.HTML)
    val html: String,
    @ColumnInfo(name = UserLinksContract.Columns.PHOTOS)
    val photos: String,
    @ColumnInfo(name = UserLinksContract.Columns.LIKES)
    val likes: String,
    @ColumnInfo(name = UserLinksContract.Columns.PORTFOLIO)
    val portfolio: String
)