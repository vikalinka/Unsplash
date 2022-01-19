package lt.vitalikas.unsplash.domain.use_cases

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import lt.vitalikas.unsplash.data.databases.entities.FeedPhotoEntity
import lt.vitalikas.unsplash.domain.models.FeedPhoto

interface GetFeedPhotosUseCase {
    var feedPhotos: Flow<PagingData<FeedPhotoEntity>>?
    suspend fun invoke(): Flow<PagingData<FeedPhotoEntity>>
}