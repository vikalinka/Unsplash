package lt.vitalikas.unsplash.domain.repositories

import net.openid.appauth.AuthorizationRequest

interface AuthRepository {
    fun getAuthRequest(): AuthorizationRequest
}