package lt.vitalikas.unsplash.data.repositories

import lt.vitalikas.unsplash.data.api.UnsplashApi
import lt.vitalikas.unsplash.domain.models.profile.Profile
import lt.vitalikas.unsplash.domain.repositories.ProfileRepository
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val api: UnsplashApi
) : ProfileRepository {
    override suspend fun getCurrentProfileData(): Profile = api.getCurrentProfile()
}