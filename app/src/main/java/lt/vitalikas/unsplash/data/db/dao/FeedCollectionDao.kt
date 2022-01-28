package lt.vitalikas.unsplash.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import lt.vitalikas.unsplash.data.db.entities.FeedCollectionEntity
import lt.vitalikas.unsplash.data.db.contracts.FeedCollectionsContract

@Dao
interface FeedCollectionDao {

    @Insert(
        entity = FeedCollectionEntity::class,
        onConflict = OnConflictStrategy.REPLACE
    )
    suspend fun insertAllFeedCollections(feedCollections: List<FeedCollectionEntity>)

    @Query("SELECT * FROM ${FeedCollectionsContract.TABLE_NAME}")
    suspend fun getAllFeedCollections(): List<FeedCollectionEntity>?
}