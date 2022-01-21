package lt.vitalikas.unsplash.data.databases.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import lt.vitalikas.unsplash.data.databases.table_contracts.FeedLinksContract

@Entity(tableName = FeedLinksContract.TABLE_NAME)
data class FeedLinkEntity(
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = FeedLinksContract.Columns.SELF)
    val self: String,
    @ColumnInfo(name = FeedLinksContract.Columns.HTML)
    val html: String,
    @ColumnInfo(name = FeedLinksContract.Columns.DOWNLOAD)
    val download: String,
    @ColumnInfo(name = FeedLinksContract.Columns.DOWNLOAD_LOCATION)
    val downloadLocation: String
)