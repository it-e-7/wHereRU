package kosa.hdit5.whereru.util.retrofit.main

import com.google.gson.GsonBuilder
import kosa.hdit5.whereru.MainApplication
import kosa.hdit5.whereru.MyCookieJar
import kosa.hdit5.whereru.util.GlobalState
import kosa.hdit5.whereru.util.retrofit.main.`interface`.WhereRUAPI
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

class CacheInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response: Response = chain.proceed(chain.request())
        return response.newBuilder()
            .header("Cache-Control", "max-age=" + 20)
            .build()
    }
}
object RetrofitBuilder {
    //baseUrl은 오픈 api의 서버 url을 넣는다.
    var baseUrl: String = GlobalState.apiBaseUrl
    var api: WhereRUAPI
    var gson = GsonBuilder().setLenient().create()
    val cookieJar = MyCookieJar()

    val okHttpClient = OkHttpClient.Builder()
        .cache(Cache(File(MainApplication.applicationContext().cacheDir, "http-cache"), 20L * 1024L * 1024L)) // 10 MiB
        .addInterceptor(CacheInterceptor())
        .cookieJar(cookieJar)
        .build()
    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            //json을 gson으로 파싱할거니까 GsonConverterFactory.create()로 gsonconverter를 가져온다.
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build() //Retrofit 객체 생성

        api = retrofit.create(WhereRUAPI::class.java)
    }

}