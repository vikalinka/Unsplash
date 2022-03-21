package lt.vitalikas.unsplash.data.repositories

import lt.vitalikas.unsplash.data.db.dao.FeedUrlDao
import lt.vitalikas.unsplash.data.db.entities.UrlEntity
import lt.vitalikas.unsplash.domain.repositories.FeedUrlRepository
import javax.inject.Inject

class FeedUrlRepositoryImpl @Inject constructor(
    private val dao: FeedUrlDao
) : FeedUrlRepository {
    override suspend fun insertFeedUrl(url: UrlEntity) = dao.insertFeedUrl(url)
}