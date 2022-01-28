package lt.vitalikas.unsplash.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import lt.vitalikas.unsplash.data.db.entities.FeedUrlEntity
import lt.vitalikas.unsplash.data.db.relations.FeedUrlAndFeedPhoto
import lt.vitalikas.unsplash.data.db.contracts.FeedUrlsContract

@Dao
interface FeedUrlDao {

    @Insert(
        entity = FeedUrlEntity::class,
        onConflict = OnConflictStrategy.REPLACE
    )
    suspend fun insertFeedUrl(url: FeedUrlEntity)

    @Query("SELECT * FROM ${FeedUrlsContract.TABLE_NAME} WHERE ${FeedUrlsContract.Columns.ID} = :id")
    suspend fun getFeedUrlAndFeedPhotoWithFeedUrlId(id: String): FeedUrlAndFeedPhoto?
}