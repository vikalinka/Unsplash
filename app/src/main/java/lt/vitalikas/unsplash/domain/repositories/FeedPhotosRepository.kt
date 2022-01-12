package lt.vitalikas.unsplash.domain.repositories

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import lt.vitalikas.unsplash.domain.models.FeedPhoto
import lt.vitalikas.unsplash.domain.models.FeedPhotoDetails

interface FeedPhotosRepository {

    suspend fun getFeedPhotos(): Flow<PagingData<FeedPhoto>>
    suspend fun getFeedPhotoDetailsById(id: String): FeedPhotoDetails
}