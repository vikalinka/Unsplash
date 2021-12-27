package lt.vitalikas.unsplash.data.use_cases

import lt.vitalikas.unsplash.domain.models.Profile
import lt.vitalikas.unsplash.domain.repositories.ProfileRepository
import lt.vitalikas.unsplash.domain.use_cases.GetProfileDataUseCase
import javax.inject.Inject

class GetProfileDataUseCaseImpl @Inject constructor(
    private val repository: ProfileRepository
) : GetProfileDataUseCase {

    override var profileData: Profile? = null

    override suspend fun invoke(): Profile {
        profileData = repository.getCurrentProfileData()
        return profileData ?: error("Error retrieving profile data")
    }
}