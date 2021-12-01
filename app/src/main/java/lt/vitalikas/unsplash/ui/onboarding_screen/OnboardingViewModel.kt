package lt.vitalikas.unsplash.ui.onboarding_screen

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import lt.vitalikas.unsplash.domain.models.OnboardingItem
import lt.vitalikas.unsplash.domain.repositories.OnboardingRepository
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val repository: OnboardingRepository
) : ViewModel() {

    private val _screens = repository.createOnboardingItems()
    val screens get() = _screens
}