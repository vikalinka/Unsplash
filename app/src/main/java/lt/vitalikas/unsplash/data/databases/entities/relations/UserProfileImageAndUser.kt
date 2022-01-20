package lt.vitalikas.unsplash.data.databases.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import lt.vitalikas.unsplash.data.databases.entities.UserEntity
import lt.vitalikas.unsplash.data.databases.entities.UserProfileImageEntity
import lt.vitalikas.unsplash.data.databases.table_contracts.UserProfileImagesContract
import lt.vitalikas.unsplash.data.databases.table_contracts.UsersContract

data class UserProfileImageAndUser(
    @Embedded
    val userProfileImage: UserProfileImageEntity,
    @Relation(
        parentColumn = UserProfileImagesContract.Columns.ID,
        entityColumn = UsersContract.Columns.USER_PROFILE_IMAGE_ID
    )
    val user: UserEntity
)