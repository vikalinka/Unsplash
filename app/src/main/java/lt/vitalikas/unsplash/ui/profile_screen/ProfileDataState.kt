package lt.vitalikas.unsplash.ui.profile_screen

import lt.vitalikas.unsplash.domain.models.Profile
import kotlin.properties.Delegates

sealed class ProfileDataState {
    class Loading(val isLoading: Boolean) : ProfileDataState()
    class Success(val profile: Profile) : ProfileDataState()
    class Error(val error: Throwable) : ProfileDataState()
}