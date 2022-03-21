package lt.vitalikas.unsplash.data.repositories

import lt.vitalikas.unsplash.data.db.dao.UserDao
import lt.vitalikas.unsplash.data.db.entities.UserEntity
import lt.vitalikas.unsplash.domain.repositories.FeedUserRepository
import javax.inject.Inject

class FeedUserRepositoryImpl @Inject constructor(
    private val dao: UserDao
) : FeedUserRepository {
    override suspend fun insertFeedUser(feedUser: UserEntity) = dao.insertUser(feedUser)
}