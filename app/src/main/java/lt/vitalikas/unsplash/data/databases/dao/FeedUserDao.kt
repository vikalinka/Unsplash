package lt.vitalikas.unsplash.data.databases.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import lt.vitalikas.unsplash.data.databases.entities.UserEntity

@Dao
interface FeedUserDao {

    @Insert(
        entity = UserEntity::class,
        onConflict = OnConflictStrategy.REPLACE
    )
    suspend fun insertFeedUser(feedUser: UserEntity)
}