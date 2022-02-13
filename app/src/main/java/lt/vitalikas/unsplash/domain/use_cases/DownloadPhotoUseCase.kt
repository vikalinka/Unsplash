package lt.vitalikas.unsplash.domain.use_cases

import android.net.Uri

interface DownloadPhotoUseCase {

    suspend operator fun invoke(url: String, uri: Uri)
}