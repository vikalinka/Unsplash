package lt.vitalikas.unsplash.domain.repositories

import lt.vitalikas.unsplash.domain.models.onboarding.OnboardingItem

interface OnboardingRepository {

    val onboardingItems: List<OnboardingItem>
}
