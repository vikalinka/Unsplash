package lt.vitalikas.unsplash.data.services.onboarding_service

import lt.vitalikas.unsplash.R
import lt.vitalikas.unsplash.domain.models.onboarding.OnboardingItem

class OnboardingService {

    val onboardings = listOf(
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