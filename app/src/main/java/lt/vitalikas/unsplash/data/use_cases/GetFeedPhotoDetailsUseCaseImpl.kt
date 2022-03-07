package lt.vitalikas.unsplash.data.use_cases

import lt.vitalikas.unsplash.domain.models.photo_details.PhotoDetails
import lt.vitalikas.unsplash.domain.repositories.PhotosRepository
import lt.vitalikas.unsplash.domain.use_cases.GetFeedPhotoDetailsUseCase
import javax.inject.Inject

class GetFeedPhotoDetailsUseCaseImpl @Inject constructor(
    private val photosRepository: PhotosRepository
) : GetFeedPhotoDetailsUseCase {
    override suspend operator fun invoke(id: String): PhotoDetails =
        photosRepository.getFeedPhotoDetailsById(id)
}