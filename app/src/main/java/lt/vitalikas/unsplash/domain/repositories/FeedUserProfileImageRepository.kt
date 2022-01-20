package lt.vitalikas.unsplash.domain.repositories

import lt.vitalikas.unsplash.data.databases.entities.UserProfileImageEntity

interface FeedUserProfileImageRepository {

    suspend fun insertFeedUserProfileImage(image: UserProfileImageEntity)
}