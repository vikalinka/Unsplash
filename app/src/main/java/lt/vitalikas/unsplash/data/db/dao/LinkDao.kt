package lt.vitalikas.unsplash.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import lt.vitalikas.unsplash.data.db.entities.LinkEntity
import lt.vitalikas.unsplash.data.db.contracts.LinkContract

@Dao
interface LinkDao {

    @Insert(
        entity = LinkEntity::class,
        onConflict = OnConflictStrategy.REPLACE
    )
    suspend fun insertFeedLink(link: LinkEntity)

    @Query("SELECT * FROM ${LinkContract.TABLE_NAME} WHERE ${LinkContract.Columns.ID} = :id")
    suspend fun getLinkWithId(id: String): LinkEntity?
}