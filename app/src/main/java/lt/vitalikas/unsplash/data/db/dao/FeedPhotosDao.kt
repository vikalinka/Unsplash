package lt.vitalikas.unsplash.data.db.dao

import androidx.paging.PagingSource
import androidx.room.*
import lt.vitalikas.unsplash.data.db.entities.PhotoEntity
import lt.vitalikas.unsplash.data.db.contracts.PhotoContract

@Dao
interface FeedPhotosDao {

    @Insert(
        entity = PhotoEntity::class,
        onConflict = OnConflictStrategy.REPLACE
    )
    suspend fun insertAllFeedPhotos(photos: List<PhotoEntity>)

    @Query("SELECT * FROM ${PhotoContract.TABLE_NAME}")
    fun getPagingSource(): PagingSource<Int, PhotoEntity>

    @Query("DELETE FROM ${PhotoContract.TABLE_NAME}")
    suspend fun deleteAllFeedPhotos()

    @Query(
        "UPDATE ${PhotoContract.TABLE_NAME} " +
                "SET ${PhotoContract.Columns.LIKED_BY_USER} = :liked, " +
                "${PhotoContract.Columns.LIKES} = :count " +
                "WHERE ${PhotoContract.Columns.ID} = :id"
    )
    suspend fun updatePhoto(id: String, liked: Boolean, count: Int)

    @Query("SELECT COUNT(*) FROM ${PhotoContract.TABLE_NAME}")
    suspend fun getFeedPhotoCount(): Int

    @Query(
        "SELECT EXISTS (SELECT 1 FROM ${PhotoContract.TABLE_NAME} " +
                "WHERE ${PhotoContract.Columns.LAST_UPDATED_AT} <= :timestamp - :cacheTimeout)"
    )
    suspend fun outdated(timestamp: Long, cacheTimeout: Long): Boolean
}