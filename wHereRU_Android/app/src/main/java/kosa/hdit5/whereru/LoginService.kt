package kosa.hdit5.whereru

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface LoginService {
    @FormUrlEncoded
    @POST("/user/login")
    fun login(@Body user: UserVO): Call<ResponseBody>
}