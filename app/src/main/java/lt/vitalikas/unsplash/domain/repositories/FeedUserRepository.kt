package lt.vitalikas.unsplash.domain.repositories

import lt.vitalikas.unsplash.data.db.entities.UserEntity

interface FeedUserRepository {

    suspend fun insertFeedUser(feedUser: UserEntity)
}