package locle.android.com.examplesslcertificate.data.remote.middleware

import okhttp3.Interceptor
import okhttp3.Response

/// add some think
class InterceptorImp : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val header = request.newBuilder()
                .header("Content-Type", "application/json")
        return chain.proceed(header.build())
    }
}