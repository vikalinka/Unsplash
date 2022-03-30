package lt.vitalikas.unsplash.ui.auth_screen

import android.app.Application
import android.content.Intent
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat.getColor
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import lt.vitalikas.unsplash.R
import lt.vitalikas.unsplash.domain.repositories.AuthRepository
import lt.vitalikas.unsplash.utils.SingleLiveEvent
import net.openid.appauth.AuthorizationService
import net.openid.appauth.TokenRequest
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val context: Application
) : ViewModel() {

    private val authService: AuthorizationService = AuthorizationService(context)

    private val _authPageIntent = SingleLiveEvent<Intent>()
    val authPageIntent: LiveData<Intent> get() = _authPageIntent

    private val _authState =
        MutableStateFlow<AuthState>(AuthState.NotLoggedIn)
    val authState = _authState.asStateFlow()

    fun openLoginPage() {
        val params = CustomTabColorSchemeParams.Builder()
            .setToolbarColor(getColor(context, R.color.custom_color_primary))
            .build()

        val customTabsIntent = CustomTabsIntent.Builder()
            .setDefaultColorSchemeParams(params)
            .build()

        val authRequest = authRepository.getAuthRequest()

        val authPageIntent = authService.getAuthorizationRequestIntent(
            authRequest,
            customTabsIntent
        )

        _authPageIntent.postValue(authPageIntent)
    }

    fun performTokenRequest(tokenExchangeRequest: TokenRequest) {
        _authState.value = AuthState.Loading
        authRepository.performTokenRequest(
            authService = authService,
            tokenExchangeRequest = tokenExchangeRequest,
            onComplete = { _authState.value = AuthState.LoggedIn },
            onError = { _authState.value = AuthState.Error }
        )
    }
}