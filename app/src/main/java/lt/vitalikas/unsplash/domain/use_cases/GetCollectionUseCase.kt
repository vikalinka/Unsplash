package lt.vitalikas.unsplash.domain.use_cases

import lt.vitalikas.unsplash.domain.models.collections.Collection

interface GetCollectionUseCase {
    suspend operator fun invoke(id: String): Collection
}