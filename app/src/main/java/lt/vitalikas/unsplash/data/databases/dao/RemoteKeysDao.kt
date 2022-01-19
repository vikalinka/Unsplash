package lt.vitalikas.unsplash.data.databases.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import lt.vitalikas.unsplash.data.databases.entities.RemoteKey
import lt.vitalikas.unsplash.data.databases.table_contracts.RemoteKeysContract

@Dao
interface RemoteKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllKeys(remoteKeys: List<RemoteKey>)

    @Query("SELECT * FROM ${RemoteKeysContract.TABLE_NAME} WHERE ${RemoteKeysContract.Columns.FEED_PHOTO_ID} = :id")
    suspend fun getRemoteKeyByFeedPhotoId(id: String): RemoteKey?

    @Query("DELETE FROM ${RemoteKeysContract.TABLE_NAME}")
    suspend fun deleteAllRemoteKeys()
}