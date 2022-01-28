package lt.vitalikas.unsplash.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import lt.vitalikas.unsplash.data.db.contracts.FeedUrlsContract

@Entity(tableName = FeedUrlsContract.TABLE_NAME)
data class FeedUrlEntity(
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = FeedUrlsContract.Columns.RAW)
    val raw: String,
    @ColumnInfo(name = FeedUrlsContract.Columns.FULL)
    val full: String,
    @ColumnInfo(name = FeedUrlsContract.Columns.REGULAR)
    val regular: String,
    @ColumnInfo(name = FeedUrlsContract.Columns.SMALL)
    val small: String,
    @ColumnInfo(name = FeedUrlsContract.Columns.THUMB)
    val thumb: String
)