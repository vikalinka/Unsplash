package lt.vitalikas.unsplash.ui.splash_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import lt.vitalikas.unsplash.data.prefsstore.PrefsStore
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    prefsStore: PrefsStore
) : ViewModel() {

    private var _timerStateFlow = MutableStateFlow(0)
    val timerStateFlow = _timerStateFlow.asStateFlow()

    val onboardingsStatus = prefsStore.isOnboardingsFinished()

    init {
        runTimer()
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

    companion object {
        const val totalSeconds = 3
    }
}
