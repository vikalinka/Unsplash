package lt.vitalikas.unsplash.domain.repositories

import lt.vitalikas.unsplash.data.db.entities.UserLinkEntity

interface FeedUserLinkRepository {

    suspend fun insertFeedUserLink(link: UserLinkEntity)
}