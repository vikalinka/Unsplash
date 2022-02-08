package lt.vitalikas.unsplash.ui.splash_screen

import android.content.Context
import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import lt.vitalikas.unsplash.R
import lt.vitalikas.unsplash.domain.repositories.OnboardingRepository
import lt.vitalikas.unsplash.ui.feed_screen.FeedState
import javax.inject.Inject
import kotlin.properties.Delegates

@HiltViewModel
class SplashViewModel @Inject constructor(
    repository: OnboardingRepository,
    @ApplicationContext context: Context
) : ViewModel() {

    private val scope = viewModelScope

    var onboardingNotFinished by Delegates.notNull<Boolean>()

    var timer: CountDownTimer? = null

    val timerFlow = (3 downTo 0)
        .asFlow()
        .onEach {
            delay(1000L)
        }

    private val _step = MutableLiveData<Long>()
    val step: LiveData<Long> get() = _step

    private val _onboardingNotFinishedStatus = MutableLiveData<Boolean>()
    val onboardingNotFinishedStatus: LiveData<Boolean> get() = _onboardingNotFinishedStatus

    init {
        scope.launch {
            onboardingNotFinished = repository.getOnboardingSharedPrefsValue(
                context.getString(R.string.onboarding_not_finished),
                true
            )
        }

//        timer = object : CountDownTimer(3000L, 1000L) {
//            override fun onTick(p0: Long) {
//                _step.postValue(p0)
//            }
//
//            override fun onFinish() {
//                _onboardingNotFinishedStatus.postValue(onboardingNotFinished)
//            }
//        }
    }

//    override fun onCleared() {
//        super.onCleared()
//        timer = null
//    }
}