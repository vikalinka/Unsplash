package lt.vitalikas.unsplash.ui.start_screen

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import lt.vitalikas.unsplash.domain.repositories.OnboardingRepository
import javax.inject.Inject

@HiltViewModel
class StartViewModel @Inject constructor(
    repository: OnboardingRepository
) : ViewModel() {

    var timer: CountDownTimer? = null

    private val _step = MutableLiveData<Long>()
    val step: LiveData<Long> get() = _step

    private val _onboardingNotFinishedStatus = MutableLiveData<Boolean>()
    val onboardingNotFinishedStatus: LiveData<Boolean> get() = _onboardingNotFinishedStatus

    init {
        val onboardingNotFinished = repository.getOnboardingSharedPrefsValue("onboarding", true)

        timer = object : CountDownTimer(3000L, 1000L) {
            override fun onTick(p0: Long) {
                _step.postValue(p0)
            }

            override fun onFinish() {
                _onboardingNotFinishedStatus.postValue(onboardingNotFinished)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        timer = null
    }
}