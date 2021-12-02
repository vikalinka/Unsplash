package lt.vitalikas.unsplash.data.repositories

import lt.vitalikas.unsplash.R
import lt.vitalikas.unsplash.domain.models.OnboardingItem
import lt.vitalikas.unsplash.domain.repositories.OnboardingRepository
import javax.inject.Inject

class OnboardingRepositoryImpl @Inject constructor() : OnboardingRepository {

    override fun createOnboardingItems(): List<OnboardingItem> =
        listOf(
            OnboardingItem(
                0L,
                R.drawable.onboarding1,
                R.string.onboarding_title_1,
                R.string.onboarding_text_1,
                R.string.onboarding_next_button_text
            ),
            OnboardingItem(
                1L,
                R.drawable.onboarding2,
                R.string.onboarding_title_2,
                R.string.onboarding_text_2,
                R.string.onboarding_next_button_text
            ),
            OnboardingItem(
                2L,
                R.drawable.onboarding3,
                R.string.onboarding_title_3,
                R.string.onboarding_text_3,
                R.string.onboarding_get_started_button_text
            )
        )
}