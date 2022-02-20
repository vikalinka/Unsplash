package lt.vitalikas.unsplash.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import lt.vitalikas.unsplash.data.db.entities.UserLinkEntity
import lt.vitalikas.unsplash.data.db.relations.UserLinkAndUser
import lt.vitalikas.unsplash.data.db.contracts.UserLinksContract

@Dao
interface FeedUserLinkDao {

    @Insert(
        entity = UserLinkEntity::class,
        onConflict = OnConflictStrategy.REPLACE
    )
    suspend fun insertFeedUserLink(link: UserLinkEntity)

    @Query("SELECT * FROM ${UserLinksContract.TABLE_NAME} WHERE ${UserLinksContract.Columns.ID} = :id")
    suspend fun getFeedUserLinkAndUserWithFeedUserLinkId(id: String): UserLinkAndUser?
}