package lt.vitalikas.unsplash.domain.use_cases

import lt.vitalikas.unsplash.domain.models.profile.Profile

interface GetProfileDataUseCase {
    var profileData: Profile?
    suspend fun invoke(): Profile
}