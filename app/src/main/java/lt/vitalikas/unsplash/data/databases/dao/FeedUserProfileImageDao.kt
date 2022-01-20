package lt.vitalikas.unsplash.data.databases.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import lt.vitalikas.unsplash.data.databases.entities.UserProfileImageEntity

@Dao
interface FeedUserProfileImageDao {

    @Insert(
        entity = UserProfileImageEntity::class,
        onConflict = OnConflictStrategy.REPLACE
    )
    suspend fun insertFeedUserProfileImage(image: UserProfileImageEntity)
}