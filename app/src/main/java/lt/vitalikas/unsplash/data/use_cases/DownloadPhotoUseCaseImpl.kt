package lt.vitalikas.unsplash.data.use_cases

import lt.vitalikas.unsplash.domain.repositories.FeedPhotosRepository
import lt.vitalikas.unsplash.domain.use_cases.DownloadPhotoUseCase
import javax.inject.Inject

class DownloadPhotoUseCaseImpl @Inject constructor(
    private val feedPhotosRepository: FeedPhotosRepository
) : DownloadPhotoUseCase {

    override suspend operator fun invoke(url: String) {
        feedPhotosRepository.downloadPhoto(url)
    }
}