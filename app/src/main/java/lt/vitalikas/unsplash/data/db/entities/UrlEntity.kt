package lt.vitalikas.unsplash.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import lt.vitalikas.unsplash.data.db.contracts.UrlContract

@Entity(tableName = UrlContract.TABLE_NAME)
data class UrlEntity(
    @PrimaryKey
    @ColumnInfo(name = UrlContract.Columns.ID) val id: String,
    @ColumnInfo(name = UrlContract.Columns.RAW) val raw: String,
    @ColumnInfo(name = UrlContract.Columns.FULL) val full: String,
    @ColumnInfo(name = UrlContract.Columns.REGULAR) val regular: String,
    @ColumnInfo(name = UrlContract.Columns.SMALL) val small: String,
    @ColumnInfo(name = UrlContract.Columns.THUMB) val thumb: String
)