package lt.vitalikas.unsplash.ui.auth_screen

sealed class AuthState {
    object Loading : AuthState()
    object NotLoggedIn : AuthState()
    object LoggedIn : AuthState()
    class Error(val errorMsg: String) : AuthState()
}