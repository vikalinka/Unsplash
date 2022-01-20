package lt.vitalikas.unsplash.data.repositories

import lt.vitalikas.unsplash.data.databases.dao.FeedUserLinkDao
import lt.vitalikas.unsplash.data.databases.entities.UserLinkEntity
import lt.vitalikas.unsplash.domain.repositories.FeedUserLinkRepository
import javax.inject.Inject

class FeedUserLinkRepositoryImpl @Inject constructor(
    private val dao: FeedUserLinkDao
) : FeedUserLinkRepository {
    override suspend fun insertFeedUserLink(link: UserLinkEntity) =
        dao.insertFeedUserLink(link)
}