package lt.vitalikas.unsplash.data.repositories

import lt.vitalikas.unsplash.data.databases.dao.FeedUrlDao
import lt.vitalikas.unsplash.data.databases.dao.FeedUserLinkDao
import lt.vitalikas.unsplash.data.databases.entities.FeedUrlEntity
import lt.vitalikas.unsplash.domain.repositories.FeedUrlRepository
import javax.inject.Inject

class FeedUrlRepositoryImpl @Inject constructor(
    private val dao: FeedUrlDao
) : FeedUrlRepository {
    override suspend fun insertFeedUrl(feedUrl: FeedUrlEntity) = dao.insertFeedUrl(feedUrl)
}