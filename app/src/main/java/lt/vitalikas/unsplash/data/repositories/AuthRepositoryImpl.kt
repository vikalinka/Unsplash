package lt.vitalikas.unsplash.data.repositories

import android.net.Uri
import lt.vitalikas.unsplash.data.networking.AuthConfig
import lt.vitalikas.unsplash.domain.repositories.AuthRepository
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationServiceConfiguration
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
}