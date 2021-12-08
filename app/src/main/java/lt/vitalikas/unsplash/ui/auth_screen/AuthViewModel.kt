package lt.vitalikas.unsplash.ui.auth_screen

import android.content.Context
import android.content.Intent
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat.getColor
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import lt.vitalikas.unsplash.R
import lt.vitalikas.unsplash.domain.repositories.AuthRepository
import lt.vitalikas.unsplash.utils.SingleLiveEvent
import net.openid.appauth.AuthorizationService
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val authService: AuthorizationService = AuthorizationService(context)

    private val _openAuthPage = SingleLiveEvent<Intent>()
    val openAuthPage: LiveData<Intent> get() = _openAuthPage

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

        _openAuthPage.postValue(authPageIntent)
    }
}