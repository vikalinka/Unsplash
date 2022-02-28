package lt.vitalikas.unsplash.data.use_cases

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import lt.vitalikas.unsplash.domain.models.collections.CollectionResponse
import lt.vitalikas.unsplash.domain.repositories.PhotosRepository
import lt.vitalikas.unsplash.domain.use_cases.GetCollectionsUseCase
import javax.inject.Inject

class GetCollectionsUseCaseImpl @Inject constructor(
    private val photosRepository: PhotosRepository
) : GetCollectionsUseCase {
    override suspend fun invoke(): Flow<PagingData<CollectionResponse>> =
        photosRepository.getCollections()
}