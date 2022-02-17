package lt.vitalikas.unsplash.domain.use_cases

interface LikePhotoUseCase {

    suspend operator fun invoke(id: String)
}