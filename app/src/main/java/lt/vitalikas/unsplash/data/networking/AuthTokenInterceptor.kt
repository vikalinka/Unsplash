package lt.vitalikas.unsplash.data.networking

import okhttp3.Interceptor
import okhttp3.Response

class AuthTokenInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val modifiedRequest = request.newBuilder()
            .addHeader("Authorization", "token ${AuthToken.authToken}")
            .build()

        return chain.proceed(modifiedRequest)
    }
}