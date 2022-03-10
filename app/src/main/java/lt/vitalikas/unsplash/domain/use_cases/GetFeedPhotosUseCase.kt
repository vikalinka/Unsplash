package lt.vitalikas.unsplash.domain.use_cases

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import lt.vitalikas.unsplash.domain.models.photo.Photo

interface GetFeedPhotosUseCase {
    suspend operator fun invoke(order: String): Flow<PagingData<Photo>>
}