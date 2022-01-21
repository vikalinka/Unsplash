package lt.vitalikas.unsplash.data.databases.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import lt.vitalikas.unsplash.data.databases.entities.UserProfileImageEntity
import lt.vitalikas.unsplash.data.databases.entities.relations.UserProfileImageAndUser
import lt.vitalikas.unsplash.data.databases.table_contracts.UserProfileImagesContract

@Dao
interface FeedUserProfileImageDao {

    @Insert(
        entity = UserProfileImageEntity::class,
        onConflict = OnConflictStrategy.REPLACE
    )
    suspend fun insertFeedUserProfileImage(image: UserProfileImageEntity)

    @Query("SELECT * FROM ${UserProfileImagesContract.TABLE_NAME} WHERE ${UserProfileImagesContract.Columns.ID} = :id")
    suspend fun getFeedUserProfileImageAndUserWithFeedUserProfileImageId(id: Long): UserProfileImageAndUser?
}