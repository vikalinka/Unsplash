package lt.vitalikas.unsplash.domain.use_cases

interface DownloadPhotoUseCase {

    suspend operator fun invoke(url: String)
}