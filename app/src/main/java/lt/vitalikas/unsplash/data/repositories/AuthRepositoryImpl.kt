package lt.vitalikas.unsplash.data.repositories

import android.content.Intent
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import lt.vitalikas.unsplash.data.networking.auth.AuthConfig
import lt.vitalikas.unsplash.data.networking.auth.AuthTokenProvider
import lt.vitalikas.unsplash.domain.repositories.AuthRepository
import net.openid.appauth.*
import timber.log.Timber
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authService: AuthorizationService
) : AuthRepository {

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
            // unsplash doesn't support pkce
            .setCodeVerifier(null)
            .setScope(AuthConfig.SCOPE)
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
        ) { response, e ->
            if (response != null) {
                AuthTokenProvider.authToken = response.accessToken.orEmpty()
                Timber.d(AuthTokenProvider.authToken)
                onComplete()
            }
            if (e != null) {
                e.message?.let { onError(it) }
            }
        }
    }

    private fun getClientAuthentication(): ClientAuthentication =
        ClientSecretPost(AuthConfig.CLIENT_SECRET)
}