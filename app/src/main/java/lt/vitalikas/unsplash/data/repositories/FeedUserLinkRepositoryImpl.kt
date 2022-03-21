package lt.vitalikas.unsplash.data.repositories

import lt.vitalikas.unsplash.data.db.dao.UserLinkDao
import lt.vitalikas.unsplash.data.db.entities.UserLinkEntity
import lt.vitalikas.unsplash.domain.repositories.FeedUserLinkRepository
import javax.inject.Inject

class FeedUserLinkRepositoryImpl @Inject constructor(
    private val dao: UserLinkDao
) : FeedUserLinkRepository {
    override suspend fun insertFeedUserLink(link: UserLinkEntity) =
        dao.insertUserLink(link)
}