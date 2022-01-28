package lt.vitalikas.unsplash.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import lt.vitalikas.unsplash.data.db.entities.UserEntity
import lt.vitalikas.unsplash.data.db.relations.UserAndFeedPhoto
import lt.vitalikas.unsplash.data.db.contracts.UsersContract

@Dao
interface FeedUserDao {

    @Insert(
        entity = UserEntity::class,
        onConflict = OnConflictStrategy.REPLACE
    )
    suspend fun insertFeedUser(feedUser: UserEntity)

    @Query("SELECT * FROM ${UsersContract.TABLE_NAME} WHERE ${UsersContract.Columns.ID} = :id")
    suspend fun getUserAndFeedPhotoWithUserId(id: String): UserAndFeedPhoto?
}