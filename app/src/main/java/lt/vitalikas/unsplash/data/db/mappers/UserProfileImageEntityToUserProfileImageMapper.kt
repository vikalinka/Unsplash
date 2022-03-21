package lt.vitalikas.unsplash.data.db.mappers

import lt.vitalikas.unsplash.data.db.entities.UserProfileImageEntity
import lt.vitalikas.unsplash.domain.models.user.UserProfileImage

class UserProfileImageEntityToUserProfileImageMapper :
    Mapper<UserProfileImageEntity, UserProfileImage> {
    override fun map(from: UserProfileImageEntity): UserProfileImage =
        UserProfileImage(
            small = from.small,
            medium = from.medium,
            large = from.large
        )
}