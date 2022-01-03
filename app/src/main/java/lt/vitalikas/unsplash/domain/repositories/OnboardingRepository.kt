package lt.vitalikas.unsplash.domain.repositories

import lt.vitalikas.unsplash.domain.models.OnboardingItem

interface OnboardingRepository {

    fun createOnboardingItems(): List<OnboardingItem>
    suspend fun getOnboardingSharedPrefsValue(key: String, value: Boolean): Boolean
    suspend fun updateOnboardingSharedPrefsValue(key: String, value: Boolean)
}