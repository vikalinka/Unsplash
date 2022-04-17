package lt.vitalikas.unsplash.data.repositories

import lt.vitalikas.unsplash.data.services.onboarding_service.OnboardingService
import lt.vitalikas.unsplash.domain.models.onboarding.OnboardingItem
import lt.vitalikas.unsplash.domain.repositories.OnboardingRepository
import javax.inject.Inject

class OnboardingRepositoryImpl @Inject constructor(
    private val onboardingService: OnboardingService
) : OnboardingRepository {

    override val onboardingItems: List<OnboardingItem>
        get() = onboardingService.onboardings
}
