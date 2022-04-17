package lt.vitalikas.unsplash.ui.onboarding_screen

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import lt.vitalikas.unsplash.data.prefsstore.PrefsStore
import lt.vitalikas.unsplash.domain.repositories.OnboardingRepository
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    repository: OnboardingRepository,
    private val prefsStore: PrefsStore
) : ViewModel() {

    val screens = repository.onboardingItems

    suspend fun finishOnboardings() = prefsStore.finishOnboardings()
}
