package lt.vitalikas.unsplash.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import lt.vitalikas.unsplash.data.db.entities.UrlEntity
import lt.vitalikas.unsplash.data.db.contracts.UrlContract

@Dao
interface UrlDao {

    @Insert(
        entity = UrlEntity::class,
        onConflict = OnConflictStrategy.REPLACE
    )
    suspend fun insertFeedUrl(url: UrlEntity)

    @Query("SELECT * FROM ${UrlContract.TABLE_NAME} WHERE ${UrlContract.Columns.ID} = :id")
    suspend fun getUrlWithId(id: String): UrlEntity?
}