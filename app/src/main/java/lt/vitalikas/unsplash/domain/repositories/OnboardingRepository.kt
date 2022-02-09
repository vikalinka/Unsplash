package lt.vitalikas.unsplash.domain.repositories

import lt.vitalikas.unsplash.domain.models.OnboardingItem
import lt.vitalikas.unsplash.ui.onboarding_screen.OnboardingStatus

interface OnboardingRepository {

    fun createOnboardingItems(): List<OnboardingItem>
    suspend fun getOnboardingStatus(key: String, defaultValue: Boolean): OnboardingStatus
    suspend fun updateOnboardingStatus(key: String, value: Boolean)
}