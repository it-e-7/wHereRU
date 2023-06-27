package kosa.hdit5.whereru.util

import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

object OkHttpClientSingleton {
    val instance: OkHttpClient = OkHttpClient.Builder()
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .pingInterval(30, TimeUnit.SECONDS)
        .build()
}