package lt.vitalikas.unsplash.data.use_cases

import android.net.Uri
import lt.vitalikas.unsplash.domain.repositories.FeedPhotosRepository
import lt.vitalikas.unsplash.domain.use_cases.DownloadPhotoUseCase
import javax.inject.Inject

class DownloadPhotoUseCaseImpl @Inject constructor(
    private val feedPhotosRepository: FeedPhotosRepository
) : DownloadPhotoUseCase {

    override suspend operator fun invoke(url: String, uri: Uri) {
        feedPhotosRepository.downloadPhoto(url, uri)
    }
}