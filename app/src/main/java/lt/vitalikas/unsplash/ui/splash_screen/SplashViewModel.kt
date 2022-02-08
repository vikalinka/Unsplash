package lt.vitalikas.unsplash.ui.splash_screen

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import lt.vitalikas.unsplash.R
import lt.vitalikas.unsplash.domain.use_cases.CheckOnboardingStatusUseCase
import javax.inject.Inject
import kotlin.properties.Delegates

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val checkOnboardingStatusUseCase: CheckOnboardingStatusUseCase,
    @ApplicationContext context: Context
) : ViewModel() {

    var onboardingNotFinished by Delegates.notNull<Boolean>()

    val timerFlow = (3 downTo 0)
        .asFlow()
        .onEach {
            delay(1000L)
        }

    init {
        viewModelScope.launch {
            onboardingNotFinished = checkOnboardingStatusUseCase(
                context.getString(R.string.onboarding_not_finished),
                true
            )
        }
    }
}