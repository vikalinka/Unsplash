package lt.vitalikas.unsplash.ui.profile_screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import lt.vitalikas.unsplash.domain.use_cases.GetProfileDataUseCase
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getProfileDataUseCase: GetProfileDataUseCase
) : ViewModel() {

    private var job: Job? = null

    var position: Int? = null

    private val _dataState = MutableLiveData<ProfileDataState>()
    val dataState: LiveData<ProfileDataState>
        get() = _dataState

    fun getProfileData() {
        when (dataState.value) {
            is ProfileDataState.Error, null -> {
                _dataState.postValue(ProfileDataState.Loading(true))
                // Retrofit launches coroutine on it`s background thread pool
                viewModelScope.launch(CoroutineExceptionHandler { _, t ->
                    Timber.d("$t")
                    _dataState.value = ProfileDataState.Loading(false)
                    _dataState.value = ProfileDataState.Error(t)
                }) {
                    val profile = getProfileDataUseCase.invoke()
                    Timber.d("Profile data fetched from API = $profile")
                    _dataState.value = ProfileDataState.Loading(false)
                    _dataState.value = ProfileDataState.Success(profile)
                }.also { job = it }
            }
            else -> {
                try {
                    val profile =
                        getProfileDataUseCase.profileData ?: error("Error retrieving profile data")
                    Timber.d("Profile data fetched from memory = $profile")
                    _dataState.value = ProfileDataState.Success(profile)
                } catch (t: Throwable) {
                    Timber.d("$t")
                    _dataState.postValue(ProfileDataState.Error(t))
                }
            }
        }
    }

    fun cancelJob() {
        job?.cancel()
    }
}