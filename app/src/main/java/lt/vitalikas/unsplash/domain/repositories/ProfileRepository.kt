package lt.vitalikas.unsplash.domain.repositories

import lt.vitalikas.unsplash.domain.models.User

interface ProfileRepository {

    suspend fun getUser(): User
}