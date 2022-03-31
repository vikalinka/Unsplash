package lt.vitalikas.unsplash.domain.use_cases

import android.content.Intent
import net.openid.appauth.TokenRequest

interface AuthUserUseCase {

    operator fun invoke(): Intent

    fun getToken(
        tokenExchangeRequest: TokenRequest,
        onComplete: () -> Unit,
        onError: (errorMsg: String) -> Unit
    )
}