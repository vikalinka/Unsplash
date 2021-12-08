package lt.vitalikas.unsplash.data.networking

import net.openid.appauth.ResponseTypeValues

object AuthConfig {
    const val AUTH_ENDPOINT = "https://unsplash.com/oauth/authorize"
    const val TOKEN_ENDPOINT = "https://unsplash.com/oauth/token"
    const val CALLBACK_ENDPOINT = "vitalikas://unsplash/callback"
    const val CLIENT_ID = "7u9GNvRcnfMyoJOLbOuQ8tlOX_pWgys1OKu-XsWSJi8"
    const val CLIENT_SECRET = "I-nDzF_BjSpC23gxlr174kdso_B2Vi40GhAUiXLNdME"
    const val REDIRECT_URI = "urn:ietf:wg:oauth:2.0:oob"
    const val RESPONSE_TYPE = ResponseTypeValues.CODE
    const val SCOPE = "public"
}