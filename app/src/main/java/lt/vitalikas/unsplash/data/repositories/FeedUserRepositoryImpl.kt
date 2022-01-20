package lt.vitalikas.unsplash.data.repositories

import lt.vitalikas.unsplash.data.apis.UnsplashApi
import lt.vitalikas.unsplash.data.databases.dao.FeedUserDao
import lt.vitalikas.unsplash.data.databases.entities.UserEntity
import lt.vitalikas.unsplash.domain.repositories.FeedUserRepository
import javax.inject.Inject

class FeedUserRepositoryImpl @Inject constructor(
    private val dao: FeedUserDao
) : FeedUserRepository {
    override suspend fun insertFeedUser(feedUser: UserEntity) = dao.insertFeedUser(feedUser)
}