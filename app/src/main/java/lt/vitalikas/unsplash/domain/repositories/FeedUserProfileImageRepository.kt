package lt.vitalikas.unsplash.domain.repositories

import lt.vitalikas.unsplash.data.db.entities.UserProfileImageEntity

interface FeedUserProfileImageRepository {

    suspend fun insertFeedUserProfileImage(image: UserProfileImageEntity)
}