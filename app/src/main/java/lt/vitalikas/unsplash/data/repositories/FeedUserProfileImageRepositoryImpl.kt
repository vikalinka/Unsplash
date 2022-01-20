package lt.vitalikas.unsplash.data.repositories

import lt.vitalikas.unsplash.data.databases.dao.FeedUserProfileImageDao
import lt.vitalikas.unsplash.data.databases.entities.UserProfileImageEntity
import lt.vitalikas.unsplash.domain.repositories.FeedUserProfileImageRepository
import javax.inject.Inject

class FeedUserProfileImageRepositoryImpl @Inject constructor(
    private val dao: FeedUserProfileImageDao
) : FeedUserProfileImageRepository {
    override suspend fun insertFeedUserProfileImage(image: UserProfileImageEntity) =
        dao.insertFeedUserProfileImage(image)
}