package lt.vitalikas.unsplash.domain.repositories

import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationService
import net.openid.appauth.TokenRequest

interface AuthRepository {

    fun getAuthRequest(): AuthorizationRequest

    fun performTokenRequest(
        authService: AuthorizationService,
        tokenExchangeRequest: TokenRequest,
        onComplete: () -> Unit,
        onError: () -> Unit
    )
}