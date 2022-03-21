package lt.vitalikas.unsplash.data.db.relations

import androidx.room.Embedded
import androidx.room.Relation
import lt.vitalikas.unsplash.data.db.entities.UserEntity
import lt.vitalikas.unsplash.data.db.entities.UserProfileImageEntity
import lt.vitalikas.unsplash.data.db.contracts.UserProfileImageContract
import lt.vitalikas.unsplash.data.db.contracts.UserContract

data class UserProfileImageAndUser(
    @Embedded
    val userProfileImage: UserProfileImageEntity,
    @Relation(
        parentColumn = UserProfileImageContract.Columns.ID,
        entityColumn = UserContract.Columns.USER_PROFILE_IMAGE_ID
    )
    val user: UserEntity
)