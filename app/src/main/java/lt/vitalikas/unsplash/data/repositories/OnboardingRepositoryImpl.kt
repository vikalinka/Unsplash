package lt.vitalikas.unsplash.data.repositories

import lt.vitalikas.unsplash.R
import lt.vitalikas.unsplash.domain.models.OnboardingItem
import lt.vitalikas.unsplash.domain.repositories.OnboardingRepository
import javax.inject.Inject

class OnboardingRepositoryImpl @Inject constructor() : OnboardingRepository {

    override fun createOnboardingItems(): List<OnboardingItem> =
        listOf(
            OnboardingItem(
                R.drawable.onboarding1,
                R.string.onboarding_title_1,
                R.string.onboarding_text_1
            ),
            OnboardingItem(
                R.drawable.onboarding2,
                R.string.onboarding_title_2,
                R.string.onboarding_text_2
            ),
            OnboardingItem(
                R.drawable.onboarding3,
                R.string.onboarding_title_3,
                R.string.onboarding_text_3
            )
        )
}