package lt.vitalikas.unsplash.data.databases.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import lt.vitalikas.unsplash.data.databases.entities.FeedCollectionEntity
import lt.vitalikas.unsplash.data.databases.table_contracts.FeedCollectionsContract

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