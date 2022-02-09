package lt.vitalikas.unsplash.ui.splash_screen

import android.content.Context
import androidx.core.util.rangeTo
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import lt.vitalikas.unsplash.R
import lt.vitalikas.unsplash.domain.repositories.OnboardingRepository
import lt.vitalikas.unsplash.ui.onboarding_screen.OnboardingStatus
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val onboardingRepository: OnboardingRepository,
    @ApplicationContext context: Context
) : ViewModel() {

    private var _timerStateFlow = MutableStateFlow(0)
    val timerStateFlow = _timerStateFlow.asStateFlow()

    lateinit var onboardingStatus: OnboardingStatus

    init {
        runTimer()
        getOnboardingStatus(context)
    }

    private fun runTimer() {
        (0 until totalSeconds + 1)
            .asFlow()
            .onEach { step ->
                delay(1_000L)
                _timerStateFlow.value = step
            }
            .catch { t ->
                Timber.d(t)
            }
            .launchIn(viewModelScope)
    }

    private fun getOnboardingStatus(context: Context) {
        viewModelScope.launch {
            onboardingStatus = onboardingRepository.getOnboardingStatus(
                context.getString(R.string.onboarding_finished),
                false
            )
        }
    }

    companion object {
        const val totalSeconds = 3
    }
}