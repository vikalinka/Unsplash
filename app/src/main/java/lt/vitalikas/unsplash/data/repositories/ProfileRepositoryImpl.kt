package lt.vitalikas.unsplash.data.repositories

import lt.vitalikas.unsplash.data.apis.UnsplashApi
import lt.vitalikas.unsplash.domain.models.User
import lt.vitalikas.unsplash.domain.repositories.ProfileRepository
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val api: UnsplashApi
) : ProfileRepository {

    override suspend fun getUser(): User = api.getUser()
}