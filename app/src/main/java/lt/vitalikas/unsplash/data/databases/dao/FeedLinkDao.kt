package lt.vitalikas.unsplash.data.databases.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import lt.vitalikas.unsplash.data.databases.entities.FeedLinkEntity
import lt.vitalikas.unsplash.data.databases.relations.FeedLinkAndFeedPhoto
import lt.vitalikas.unsplash.data.databases.table_contracts.FeedLinksContract

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