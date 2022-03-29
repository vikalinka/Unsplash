package lt.vitalikas.unsplash.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import lt.vitalikas.unsplash.data.db.entities.RemoteKey
import lt.vitalikas.unsplash.data.db.contracts.RemoteKeyContract

@Dao
interface RemoteKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllKeys(remoteKeys: List<RemoteKey>)

    @Query("SELECT * FROM ${RemoteKeyContract.TABLE_NAME} WHERE ${RemoteKeyContract.Columns.PHOTO_ID} = :id")
    suspend fun getRemoteKeyByPhotoId(id: String): RemoteKey?

    @Query("DELETE FROM ${RemoteKeyContract.TABLE_NAME}")
    suspend fun deleteAllRemoteKeys()
}