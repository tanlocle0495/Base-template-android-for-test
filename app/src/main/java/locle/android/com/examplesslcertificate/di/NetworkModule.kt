package locle.android.com.examplesslcertificate.di

import android.app.Application
import android.content.Context
import com.readystatesoftware.chuck.ChuckInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import locle.android.com.examplesslcertificate.data.remote.api.ApiService
import locle.android.com.examplesslcertificate.data.remote.middleware.ConnectInterceptor
import locle.android.com.examplesslcertificate.data.remote.middleware.InterceptorImp
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {
    @Singleton
    @Provides
    fun providerInterceptor(
            @ApplicationContext context: Context,
            cache: Cache,
    ) = OkHttpClient.Builder().apply {

        cache(cache)
        addInterceptor(InterceptorImp())
        addInterceptor(ChuckInterceptor(context))
        readTimeout(30, TimeUnit.SECONDS)
        writeTimeout(30, TimeUnit.SECONDS)
        connectTimeout(60, TimeUnit.SECONDS)
        addInterceptor(ConnectInterceptor(context))
        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            addInterceptor(logging)
        }
    }.build()

    @Singleton
    @Provides
    fun providerCache(app: Application): Cache = Cache(app.cacheDir, 10 * 1024 * 1024) // 10mib
    fun providerApiService(
            okHttpClient: OkHttpClient
    ): ApiService {
        val retrofit = Retrofit.Builder()
                .baseUrl(" ")
                .addConverterFactory(MoshiConverterFactory.create())
                .client(okHttpClient)
                .build()
        return retrofit.create(ApiService::class.java)
    }

}