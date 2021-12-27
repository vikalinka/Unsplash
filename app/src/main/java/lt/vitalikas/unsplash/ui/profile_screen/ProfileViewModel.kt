package lt.vitalikas.unsplash.ui.profile_screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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

    private val _dataState = MutableLiveData<ProfileDataState>()
    val dataState: LiveData<ProfileDataState>
        get() = _dataState

    fun getProfileData() {
        if (dataState.value == null) {
            _dataState.postValue(ProfileDataState.Loading(true))
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val profile = getProfileDataUseCase.invoke()
                    Timber.d("Profile fetched from API = $profile")
                    _dataState.postValue(ProfileDataState.Success(profile))
                } catch (t: Throwable) {
                    Timber.d("$t")
                    _dataState.postValue(ProfileDataState.Error(t))
                }
            }.also { job = it }
        } else {
            try {
                val profile = getProfileDataUseCase.profileData
                Timber.d("Profile fetched from memory = $profile")
                _dataState.postValue(
                    profile?.let { ProfileDataState.Success(it) }
                        ?: error("Error retrieving Profile data")
                )
            } catch (t: Throwable) {
                Timber.d("$t")
                _dataState.postValue(ProfileDataState.Error(t))
            }
        }
    }
}