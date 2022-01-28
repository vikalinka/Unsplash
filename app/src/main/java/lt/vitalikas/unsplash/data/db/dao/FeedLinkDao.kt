package lt.vitalikas.unsplash.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import lt.vitalikas.unsplash.data.db.entities.FeedLinkEntity
import lt.vitalikas.unsplash.data.db.relations.FeedLinkAndFeedPhoto
import lt.vitalikas.unsplash.data.db.contracts.FeedLinksContract

@Dao
interface FeedLinkDao {

    @Insert(
        entity = FeedLinkEntity::class,
        onConflict = OnConflictStrategy.REPLACE
    )
    suspend fun insertFeedLink(feedLink: FeedLinkEntity)

    @Query("SELECT * FROM ${FeedLinksContract.TABLE_NAME} WHERE ${FeedLinksContract.Columns.ID} = :id")
    suspend fun getFeedLinkAndFeedPhotoWithFeedLinkId(id: String): FeedLinkAndFeedPhoto?
}