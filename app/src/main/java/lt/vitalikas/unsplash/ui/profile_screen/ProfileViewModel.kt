package lt.vitalikas.unsplash.ui.profile_screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import lt.vitalikas.unsplash.domain.models.Profile
import lt.vitalikas.unsplash.domain.repositories.ProfileRepository
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: ProfileRepository
) : ViewModel() {

    private val _profile = MutableLiveData<Profile>()
    val profile: LiveData<Profile>
        get() = _profile

    fun getProfile() {
        val job = viewModelScope.launch {
            try {
                val profile = repository.getCurrentProfile()
                Timber.d("$profile")
                _profile.postValue(profile)
            } catch (t: Throwable) {
                Timber.d("${t}")
            }
        }
    }
}