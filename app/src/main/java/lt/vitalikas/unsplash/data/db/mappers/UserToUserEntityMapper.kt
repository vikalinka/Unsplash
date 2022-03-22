package lt.vitalikas.unsplash.data.db.mappers

import lt.vitalikas.unsplash.data.db.entities.UserEntity
import lt.vitalikas.unsplash.domain.models.user.User

class UserToUserEntityMapper : Mapper<User, UserEntity> {
    override fun map(from: User): UserEntity =
        UserEntity(
            id = from.id,
            username = from.username,
            name = from.name,
            firstName = from.firstName,
            lastName = from.lastName ?: "N/A",
            instagramUsername = from.instagramUsername,
            twitterUsername = from.twitterUsername,
            portfolioUrl = from.portfolioUrl,
            bio = from.bio,
            location = from.location,
            totalLikes = from.totalLikes,
            totalPhotos = from.totalPhotos,
            totalCollections = from.totalCollections,
            userProfileImageId = from.id,
            userLinkId = from.id
        )
}