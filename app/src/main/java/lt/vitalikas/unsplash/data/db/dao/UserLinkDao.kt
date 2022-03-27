package lt.vitalikas.unsplash.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import lt.vitalikas.unsplash.data.db.contracts.UserLinkContract
import lt.vitalikas.unsplash.data.db.entities.UserLinkEntity

@Dao
interface UserLinkDao {

    @Insert(
        entity = UserLinkEntity::class,
        onConflict = OnConflictStrategy.REPLACE
    )
    suspend fun insertUserLink(link: UserLinkEntity)

    @Query("SELECT * FROM ${UserLinkContract.TABLE_NAME} WHERE ${UserLinkContract.Columns.ID} = :id")
    suspend fun getUserLinkWithId(id: String): UserLinkEntity?
}