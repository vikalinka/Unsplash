package lt.vitalikas.unsplash.data.databases.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import lt.vitalikas.unsplash.data.databases.entities.FeedUrlEntity
import lt.vitalikas.unsplash.data.databases.entities.relations.FeedUrlAndFeedPhoto
import lt.vitalikas.unsplash.data.databases.entities.relations.UserAndFeedPhoto
import lt.vitalikas.unsplash.data.databases.table_contracts.FeedUrlsContract
import lt.vitalikas.unsplash.data.databases.table_contracts.UsersContract

@Dao
interface FeedUrlDao {

    @Insert(
        entity = FeedUrlEntity::class,
        onConflict = OnConflictStrategy.REPLACE
    )
    suspend fun insertFeedUrl(url: FeedUrlEntity)

    @Query("SELECT * FROM ${FeedUrlsContract.TABLE_NAME} WHERE ${FeedUrlsContract.Columns.ID} = :id")
    suspend fun getFeedUrlAndFeedPhotoWithFeedUrlId(id: Long): FeedUrlAndFeedPhoto?
}