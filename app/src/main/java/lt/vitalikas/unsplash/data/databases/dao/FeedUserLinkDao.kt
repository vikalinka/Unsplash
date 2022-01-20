package lt.vitalikas.unsplash.data.databases.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import lt.vitalikas.unsplash.data.databases.entities.UserLinkEntity

@Dao
interface FeedUserLinkDao {

    @Insert(
        entity = UserLinkEntity::class,
        onConflict = OnConflictStrategy.REPLACE
    )
    suspend fun insertFeedUserLink(link: UserLinkEntity)
}