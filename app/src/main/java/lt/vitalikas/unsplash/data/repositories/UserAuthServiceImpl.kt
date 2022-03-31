package lt.vitalikas.unsplash.data.repositories

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import lt.vitalikas.unsplash.R
import lt.vitalikas.unsplash.data.networking.auth.AuthConfig
import lt.vitalikas.unsplash.data.networking.auth.AuthTokenProvider
import lt.vitalikas.unsplash.domain.repositories.UserAuthService
import net.openid.appauth.*
import timber.log.Timber
import javax.inject.Inject

class UserAuthServiceImpl @Inject constructor(
    private val authService: AuthorizationService,
    private val context: Context
) : UserAuthService {

    override fun getAuthRequest(): AuthorizationRequest {
        val serviceConfig = AuthorizationServiceConfiguration(
            Uri.parse(AuthConfig.AUTH_ENDPOINT),
            Uri.parse(AuthConfig.TOKEN_ENDPOINT)
        )

        val redirectUri = Uri.parse(AuthConfig.CALLBACK_ENDPOINT)

        return AuthorizationRequest.Builder(
            serviceConfig,
            AuthConfig.CLIENT_ID,
            AuthConfig.RESPONSE_TYPE,
            redirectUri
        )
            // unsplash doesn't support pkce code verifier
            .setCodeVerifier(null)
            .setScope(AuthConfig.SCOPE)
            .build()
    }

    override fun getCustomTabsIntent(): CustomTabsIntent {
        val params = CustomTabColorSchemeParams.Builder()
            .setToolbarColor(ContextCompat.getColor(context, R.color.custom_color_primary))
            .build()

        return CustomTabsIntent.Builder()
            .setDefaultColorSchemeParams(params)
            .build()
    }

    override fun getAuthRequestIntent(
        authRequest: AuthorizationRequest,
        customTabsIntent: CustomTabsIntent
    ): Intent = authService.getAuthorizationRequestIntent(authRequest, customTabsIntent)

    override fun performTokenRequest(
        tokenExchangeRequest: TokenRequest,
        onComplete: () -> Unit,
        onError: (errorMsg: String) -> Unit
    ) {
        val clientAuth = getClientAuthentication()

        authService.performTokenRequest(
            tokenExchangeRequest,
            clientAuth
        ) { response, exception ->
            if (response != null) {
                AuthTokenProvider.authToken = response.accessToken.orEmpty()
                Timber.d(AuthTokenProvider.authToken)
                onComplete()
            } else {
                exception?.message?.let { errorMsg ->
                    Timber.d(errorMsg)
                    onError(errorMsg)
                }
            }
        }
    }

    private fun getClientAuthentication(): ClientAuthentication =
        ClientSecretPost(AuthConfig.CLIENT_SECRET)
}