package lt.vitalikas.unsplash.domain.repositories

import lt.vitalikas.unsplash.data.databases.entities.UserLinkEntity

interface FeedUserLinkRepository {

    suspend fun insertFeedUserLink(link: UserLinkEntity)
}