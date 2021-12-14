package lt.vitalikas.unsplash.ui.auth_screen

import android.content.Context
import android.content.Intent
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat.getColor
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import lt.vitalikas.unsplash.R
import lt.vitalikas.unsplash.domain.repositories.AuthRepository
import lt.vitalikas.unsplash.utils.SingleLiveEvent
import net.openid.appauth.AuthorizationService
import net.openid.appauth.TokenRequest
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val authService: AuthorizationService = AuthorizationService(context)

    private val _authPageIntent = SingleLiveEvent<Intent>()
    val authPageIntent: LiveData<Intent> get() = _authPageIntent

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _authFailed = SingleLiveEvent<Int>()
    val authFailed: LiveData<Int> get() = _authFailed

    private val _authSuccess = SingleLiveEvent<Unit>()
    val authSuccess: LiveData<Unit> get() = _authSuccess

    fun openLoginPage() {
        val params = CustomTabColorSchemeParams.Builder()
            .setToolbarColor(getColor(context, R.color.custom_color_primary))
            .build()

        val customTabsIntent = CustomTabsIntent.Builder()
            .setDefaultColorSchemeParams(params)
            .build()

        val authPageIntent = authService.getAuthorizationRequestIntent(
            authRepository.getAuthRequest(),
            customTabsIntent
        )

        _authPageIntent.postValue(authPageIntent)
    }

    fun onAuthFailed() {
        _authFailed.postValue(R.string.auth_failed)
    }

    fun performTokenRequest(tokenExchangeRequest: TokenRequest) {
        _isLoading.postValue(true)
        authRepository.performTokenRequest(
            authService = authService,
            tokenExchangeRequest = tokenExchangeRequest,
            onComplete = {
                _isLoading.postValue(false)
                _authSuccess.postValue(Unit)
            },
            onError = {
                _isLoading.postValue(false)
                onAuthFailed()
            }
        )
    }
}