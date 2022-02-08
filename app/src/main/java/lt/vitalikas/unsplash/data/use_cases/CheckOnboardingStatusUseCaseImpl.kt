package lt.vitalikas.unsplash.data.use_cases

import lt.vitalikas.unsplash.domain.repositories.OnboardingRepository
import lt.vitalikas.unsplash.domain.use_cases.CheckOnboardingStatusUseCase
import javax.inject.Inject

class CheckOnboardingStatusUseCaseImpl @Inject constructor(
    private val onboardingRepository: OnboardingRepository
) : CheckOnboardingStatusUseCase {
    override suspend fun invoke(key: String, value: Boolean): Boolean =
        onboardingRepository.getOnboardingSharedPrefsValue(key, value)
}