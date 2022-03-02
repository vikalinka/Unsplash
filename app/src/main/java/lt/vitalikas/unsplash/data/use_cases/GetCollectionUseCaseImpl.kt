package lt.vitalikas.unsplash.data.use_cases

import lt.vitalikas.unsplash.domain.models.collections.Collection
import lt.vitalikas.unsplash.domain.repositories.PhotosRepository
import lt.vitalikas.unsplash.domain.use_cases.GetCollectionUseCase
import javax.inject.Inject

class GetCollectionUseCaseImpl @Inject constructor(
    private val repository: PhotosRepository
) : GetCollectionUseCase {
    override suspend fun invoke(id: String): Collection = repository.getCollection(id)
}