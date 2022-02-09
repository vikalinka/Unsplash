package lt.vitalikas.unsplash.ui.onboarding_screen

sealed class OnboardingStatus {
    object Finished : OnboardingStatus()
    object NotFinished : OnboardingStatus()
}