package lt.vitalikas.unsplash.domain.use_cases

import kotlinx.coroutines.flow.Flow
import lt.vitalikas.unsplash.domain.models.FeedPhotoDetails

interface GetFeedPhotoDetailsUseCase {
    suspend fun invoke(id: String): Flow<FeedPhotoDetails>
}