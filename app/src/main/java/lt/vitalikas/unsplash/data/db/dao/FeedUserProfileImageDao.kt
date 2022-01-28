package lt.vitalikas.unsplash.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import lt.vitalikas.unsplash.data.db.entities.UserProfileImageEntity
import lt.vitalikas.unsplash.data.db.relations.UserProfileImageAndUser
import lt.vitalikas.unsplash.data.db.contracts.UserProfileImagesContract

@Dao
interface FeedUserProfileImageDao {

    @Insert(
        entity = UserProfileImageEntity::class,
        onConflict = OnConflictStrategy.REPLACE
    )
    suspend fun insertFeedUserProfileImage(image: UserProfileImageEntity)

    @Query("SELECT * FROM ${UserProfileImagesContract.TABLE_NAME} WHERE ${UserProfileImagesContract.Columns.ID} = :id")
    suspend fun getFeedUserProfileImageAndUserWithFeedUserProfileImageId(id: String): UserProfileImageAndUser?
}