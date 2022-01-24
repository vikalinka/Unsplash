package lt.vitalikas.unsplash.data.repositories

import android.net.Uri
import lt.vitalikas.unsplash.data.networking.auth.AuthConfig
import lt.vitalikas.unsplash.data.networking.auth.AuthToken
import lt.vitalikas.unsplash.domain.repositories.AuthRepository
import net.openid.appauth.*
import timber.log.Timber
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor() : AuthRepository {

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

    override fun performTokenRequest(
        authService: AuthorizationService,
        tokenExchangeRequest: TokenRequest,
        onComplete: () -> Unit,
        onError: () -> Unit
    ) {
        val clientAuth = getClientAuthentication()

        authService.performTokenRequest(
            tokenExchangeRequest,
            clientAuth
        ) { response, _ ->
            when {
                response != null -> {
                    AuthToken.authToken = response.accessToken.orEmpty()
                    Timber.d(AuthToken.authToken)
                    onComplete()
                }
                else -> onError()
            }
        }
    }

    private fun getClientAuthentication(): ClientAuthentication =
        ClientSecretPost(AuthConfig.CLIENT_SECRET)
}