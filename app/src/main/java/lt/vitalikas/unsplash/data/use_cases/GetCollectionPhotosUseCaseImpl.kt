package lt.vitalikas.unsplash.data.use_cases

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import lt.vitalikas.unsplash.domain.models.collections.CollectionPhoto
import lt.vitalikas.unsplash.domain.repositories.PhotosRepository
import lt.vitalikas.unsplash.domain.use_cases.GetCollectionPhotosUseCase
import javax.inject.Inject

class GetCollectionPhotosUseCaseImpl @Inject constructor(
    private val photosRepository: PhotosRepository
) : GetCollectionPhotosUseCase {
    override suspend fun invoke(id: String): Flow<PagingData<CollectionPhoto>> =
        photosRepository.getCollectionPhotos(id)
}