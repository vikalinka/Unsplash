package lt.vitalikas.unsplash.data.databases.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import lt.vitalikas.unsplash.data.databases.entities.FeedPhotoEntity
import lt.vitalikas.unsplash.data.databases.table_contracts.FeedPhotosContract

@Dao
interface FeedPhotosDao {

    @Insert(
        entity = FeedPhotoEntity::class,
        onConflict = OnConflictStrategy.REPLACE
    )
    suspend fun insertAllFeedPhotos(feedPhotos: List<FeedPhotoEntity>)

    @Query("SELECT * FROM ${FeedPhotosContract.TABLE_NAME}")
    fun getPagingSource(): PagingSource<Int, FeedPhotoEntity>

    @Query("DELETE FROM ${FeedPhotosContract.TABLE_NAME}")
    suspend fun deleteAllFeedPhotos()
}