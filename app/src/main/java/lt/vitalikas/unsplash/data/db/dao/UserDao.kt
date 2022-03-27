package lt.vitalikas.unsplash.data.db.dao

import androidx.room.*
import lt.vitalikas.unsplash.data.db.entities.UserEntity
import lt.vitalikas.unsplash.data.db.relations.UserAndPhotoEntity
import lt.vitalikas.unsplash.data.db.contracts.UserContract

@Dao
interface UserDao {

    @Insert(
        entity = UserEntity::class,
        onConflict = OnConflictStrategy.REPLACE
    )
    suspend fun insertUser(user: UserEntity)

    @Transaction
    @Query("SELECT * FROM ${UserContract.TABLE_NAME} WHERE ${UserContract.Columns.ID} = :id")
    suspend fun getUserAndPhotoWithUserId(id: String): UserAndPhotoEntity?
}