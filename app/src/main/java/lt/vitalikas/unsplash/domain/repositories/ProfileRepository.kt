package lt.vitalikas.unsplash.domain.repositories

import lt.vitalikas.unsplash.domain.models.profile.Profile

interface ProfileRepository {

    suspend fun getCurrentProfileData(): Profile
}