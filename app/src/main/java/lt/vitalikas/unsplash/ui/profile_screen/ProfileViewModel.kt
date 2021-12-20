package lt.vitalikas.unsplash.ui.profile_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import lt.vitalikas.unsplash.domain.repositories.ProfileRepository
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: ProfileRepository
) : ViewModel() {

    fun getUser() {
        viewModelScope.launch {
            try {
                val user = repository.getCurrentProfile()
                Timber.d("$user")
            } catch (t: Throwable) {
                Timber.d("${t}")
            }
        }
    }
}