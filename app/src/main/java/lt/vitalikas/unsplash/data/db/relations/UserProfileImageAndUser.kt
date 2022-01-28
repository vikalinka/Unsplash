package lt.vitalikas.unsplash.data.db.relations

import androidx.room.Embedded
import androidx.room.Relation
import lt.vitalikas.unsplash.data.db.entities.UserEntity
import lt.vitalikas.unsplash.data.db.entities.UserProfileImageEntity
import lt.vitalikas.unsplash.data.db.contracts.UserProfileImagesContract
import lt.vitalikas.unsplash.data.db.contracts.UsersContract

data class UserProfileImageAndUser(
    @Embedded
    val userProfileImage: UserProfileImageEntity,
    @Relation(
        parentColumn = UserProfileImagesContract.Columns.ID,
        entityColumn = UsersContract.Columns.USER_PROFILE_IMAGE_ID
    )
    val user: UserEntity
)