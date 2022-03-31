package lt.vitalikas.unsplash.domain.repositories

import android.content.Intent
import androidx.browser.customtabs.CustomTabsIntent
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.TokenRequest

interface UserAuthService {

    fun getAuthRequest(): AuthorizationRequest

    fun getCustomTabsIntent(): CustomTabsIntent

    fun getAuthRequestIntent(
        authRequest: AuthorizationRequest,
        customTabsIntent: CustomTabsIntent
    ): Intent

    fun performTokenRequest(
        tokenExchangeRequest: TokenRequest,
        onComplete: () -> Unit,
        onError: (errorMsg: String) -> Unit
    )
}