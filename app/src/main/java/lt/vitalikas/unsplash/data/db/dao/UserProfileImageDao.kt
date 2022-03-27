package lt.vitalikas.unsplash.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import lt.vitalikas.unsplash.data.db.entities.UserProfileImageEntity
import lt.vitalikas.unsplash.data.db.contracts.UserProfileImageContract

@Dao
interface UserProfileImageDao {

    @Insert(
        entity = UserProfileImageEntity::class,
        onConflict = OnConflictStrategy.REPLACE
    )
    suspend fun insertUserProfileImage(image: UserProfileImageEntity)

    @Query("SELECT * FROM ${UserProfileImageContract.TABLE_NAME} WHERE ${UserProfileImageContract.Columns.ID} = :id")
    suspend fun getUserProfileImageWithId(id: String): UserProfileImageEntity?
}