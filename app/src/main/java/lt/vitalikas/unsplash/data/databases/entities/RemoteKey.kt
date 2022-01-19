package lt.vitalikas.unsplash.data.databases.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import lt.vitalikas.unsplash.data.databases.table_contracts.RemoteKeysContract

@Entity(tableName = RemoteKeysContract.TABLE_NAME)
data class RemoteKey(
    @PrimaryKey
    @ColumnInfo(name = RemoteKeysContract.Columns.FEED_PHOTO_ID)
    val feedPhotoId: String,
    @ColumnInfo(name = RemoteKeysContract.Columns.PREV_KEY)
    val prevKey: Int?,
    @ColumnInfo(name = RemoteKeysContract.Columns.NEXT_KEY)
    val nextKey: Int?
)