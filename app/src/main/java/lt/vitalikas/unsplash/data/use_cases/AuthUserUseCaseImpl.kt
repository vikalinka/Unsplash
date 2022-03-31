package lt.vitalikas.unsplash.data.use_cases

import lt.vitalikas.unsplash.domain.repositories.PhotosRepository
import lt.vitalikas.unsplash.domain.use_cases.AuthUserUseCase
import javax.inject.Inject

class AuthUserUseCaseImpl @Inject constructor(
    private val photosRepository: PhotosRepository
) : AuthUserUseCase {

    override suspend fun invoke() {
        TODO("Not yet implemented")
    }
}