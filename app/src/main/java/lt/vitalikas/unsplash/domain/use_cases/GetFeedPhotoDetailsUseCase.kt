package lt.vitalikas.unsplash.domain.use_cases

import lt.vitalikas.unsplash.domain.models.photo_details.PhotoDetails

interface GetFeedPhotoDetailsUseCase {
    suspend operator fun invoke(id: String): PhotoDetails
}