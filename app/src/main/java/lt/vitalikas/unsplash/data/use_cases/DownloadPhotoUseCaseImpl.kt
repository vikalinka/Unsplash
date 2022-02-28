package lt.vitalikas.unsplash.data.use_cases

import android.net.Uri
import lt.vitalikas.unsplash.domain.repositories.PhotosRepository
import lt.vitalikas.unsplash.domain.use_cases.DownloadPhotoUseCase
import javax.inject.Inject

class DownloadPhotoUseCaseImpl @Inject constructor(
    private val photosRepository: PhotosRepository
) : DownloadPhotoUseCase {

    override suspend operator fun invoke(url: String, uri: Uri) {
        photosRepository.downloadPhoto(url, uri)
    }
}