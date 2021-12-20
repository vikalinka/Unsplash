package lt.vitalikas.unsplash.domain.repositories

import lt.vitalikas.unsplash.domain.models.Profile

interface ProfileRepository {

    suspend fun getCurrentProfile(): Profile
}