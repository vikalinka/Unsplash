package lt.vitalikas.unsplash.data.db.mappers

import lt.vitalikas.unsplash.data.db.entities.UserProfileImageEntity
import lt.vitalikas.unsplash.domain.models.user.User

class UserProfileImageToUserProfileImageEntityMapper :
    Mapper<User, UserProfileImageEntity> {
    override fun map(from: User): UserProfileImageEntity =
        UserProfileImageEntity(
            id = from.id,
            small = from.profileImage.small,
            medium = from.profileImage.medium,
            large = from.profileImage.large
        )
}