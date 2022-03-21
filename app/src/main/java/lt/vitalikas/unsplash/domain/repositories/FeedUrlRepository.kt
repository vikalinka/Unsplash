package lt.vitalikas.unsplash.domain.repositories

import lt.vitalikas.unsplash.data.db.entities.UrlEntity

interface FeedUrlRepository {

    suspend fun insertFeedUrl(url: UrlEntity)
}