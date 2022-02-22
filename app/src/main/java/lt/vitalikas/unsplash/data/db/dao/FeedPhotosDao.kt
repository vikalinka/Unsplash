package lt.vitalikas.unsplash.data.db.dao

import androidx.paging.PagingSource
import androidx.room.*
import lt.vitalikas.unsplash.data.db.entities.FeedPhotoEntity
import lt.vitalikas.unsplash.data.db.contracts.FeedPhotosContract

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

    @Query(
        "UPDATE ${FeedPhotosContract.TABLE_NAME} " +
                "SET ${FeedPhotosContract.Columns.LIKED_BY_USER} = :liked, " +
                "${FeedPhotosContract.Columns.LIKES} = :count " +
                "WHERE ${FeedPhotosContract.Columns.ID} = :id"
    )
    suspend fun updatePhoto(id: String, liked: Boolean, count: Int)

    @Query("SELECT COUNT(*) FROM ${FeedPhotosContract.TABLE_NAME}")
    suspend fun getFeedPhotoCount(): Int

    @Query(
        "SELECT EXISTS (SELECT 1 FROM ${FeedPhotosContract.TABLE_NAME} " +
                "WHERE ${FeedPhotosContract.Columns.LAST_UPDATED_AT} <= :timestamp - :cacheTimeout)"
    )
    suspend fun outdated(timestamp: Long, cacheTimeout: Long): Boolean
}