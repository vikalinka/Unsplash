package lt.vitalikas.unsplash.domain.use_cases

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import lt.vitalikas.unsplash.domain.models.photo.Photo

interface SearchPhotosUseCase {
    suspend operator fun invoke(query: String): Flow<PagingData<Photo>>
}