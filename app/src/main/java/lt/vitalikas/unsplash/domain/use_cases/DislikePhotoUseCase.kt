package lt.vitalikas.unsplash.domain.use_cases

interface DislikePhotoUseCase {

    suspend operator fun invoke(id: String)
}