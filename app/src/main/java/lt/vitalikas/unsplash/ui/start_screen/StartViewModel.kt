package lt.vitalikas.unsplash.ui.start_screen

import android.content.SharedPreferences
import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import lt.vitalikas.unsplash.domain.repositories.AppBootSharedPrefsRepository
import javax.inject.Inject

@HiltViewModel
class StartViewModel @Inject constructor(
    private val repository: AppBootSharedPrefsRepository
) : ViewModel() {

    var timer: CountDownTimer? = null

    var sharedPrefs: SharedPreferences = repository.createSharedPrefs()

    private val _step = MutableLiveData<Long>()
    val step: LiveData<Long> get() = _step

    private val _sharedPrefsStatus = MutableLiveData<Boolean>()
    val sharedPrefsStatus: LiveData<Boolean> get() = _sharedPrefsStatus

    init {
        val isFirstBoot = repository.createValue(sharedPrefs, "isFirstBoot", true)

        timer = object : CountDownTimer(3000L, 1000L) {
            override fun onTick(p0: Long) {
                _step.postValue(p0)
            }

            override fun onFinish() {
                _sharedPrefsStatus.postValue(isFirstBoot)
            }
        }
    }

    fun updateSharedPrefsStatus(status: Boolean) {
        repository.updateValue(sharedPrefs, "isFirstBoot", status)
    }

    override fun onCleared() {
        super.onCleared()
        timer = null
    }
}