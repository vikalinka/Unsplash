package lt.vitalikas.unsplash.ui.auth_screen

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import lt.vitalikas.unsplash.domain.use_cases.AuthUserUseCase
import lt.vitalikas.unsplash.utils.SingleLiveEvent
import net.openid.appauth.TokenRequest
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authUserUseCase: AuthUserUseCase
) : ViewModel() {

    private val _authPageIntent = SingleLiveEvent<Intent>()
    val authPageIntent: LiveData<Intent> get() = _authPageIntent

    private val _authState =
        MutableStateFlow<AuthState>(AuthState.NotLoggedIn)
    val authState = _authState.asStateFlow()

    fun authenticateUser() {
        val authPageIntent = authUserUseCase()
        _authPageIntent.postValue(authPageIntent)
    }

    fun getToken(tokenExchangeRequest: TokenRequest) {
        _authState.value = AuthState.Loading
        authUserUseCase.getToken(
            tokenExchangeRequest = tokenExchangeRequest,
            onComplete = { _authState.value = AuthState.LoggedIn },
            onError = { errorMsg -> _authState.value = AuthState.Error(errorMsg) }
        )
    }
}