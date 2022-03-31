package lt.vitalikas.unsplash.data.use_cases

import android.content.Intent
import lt.vitalikas.unsplash.domain.repositories.UserAuthService
import lt.vitalikas.unsplash.domain.use_cases.AuthUserUseCase
import net.openid.appauth.TokenRequest
import javax.inject.Inject

class AuthUserUseCaseImpl @Inject constructor(
    private val userAuthService: UserAuthService
) : AuthUserUseCase {

    override fun invoke(): Intent {
        val authRequest = userAuthService.getAuthRequest()
        val customTabsIntent = userAuthService.getCustomTabsIntent()

        return userAuthService.getAuthRequestIntent(
            authRequest,
            customTabsIntent
        )
    }

    override fun getToken(
        tokenExchangeRequest: TokenRequest,
        onComplete: () -> Unit,
        onError: (errorMsg: String) -> Unit
    ) = userAuthService.performTokenRequest(tokenExchangeRequest, onComplete, onError)
}