package lt.vitalikas.unsplash.domain.repositories

import lt.vitalikas.unsplash.domain.models.OnboardingItem

interface OnboardingRepository {

    fun createOnboardingItems(): List<OnboardingItem>
    fun getOnboardingSharedPrefsValue(key: String, value: Boolean): Boolean
    fun updateOnboardingSharedPrefsValue(key: String, value: Boolean)
}