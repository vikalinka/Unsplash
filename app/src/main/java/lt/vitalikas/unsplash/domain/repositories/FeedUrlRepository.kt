package lt.vitalikas.unsplash.domain.repositories

import lt.vitalikas.unsplash.data.db.entities.FeedUrlEntity

interface FeedUrlRepository {

    suspend fun insertFeedUrl(feedUrl: FeedUrlEntity)
}