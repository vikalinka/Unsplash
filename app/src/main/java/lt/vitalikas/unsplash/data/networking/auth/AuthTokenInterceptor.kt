package lt.vitalikas.unsplash.data.networking.auth

import okhttp3.Interceptor
import okhttp3.Response

class AuthTokenInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val modifiedRequest = request.newBuilder()
            .addHeader("Authorization", "Bearer ${AuthToken.authToken}")
            .addHeader("Accept-Version", "v1")
            .build()

        return chain.proceed(modifiedRequest)
    }
}