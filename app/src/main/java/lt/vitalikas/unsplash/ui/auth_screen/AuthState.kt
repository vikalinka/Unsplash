package lt.vitalikas.unsplash.ui.auth_screen

import androidx.annotation.StringRes

sealed class AuthState {
    object Loading : AuthState()
    object NotLoggedIn : AuthState()
    object LoggedIn : AuthState()
    class Error(@StringRes val id: Int) : AuthState()
}