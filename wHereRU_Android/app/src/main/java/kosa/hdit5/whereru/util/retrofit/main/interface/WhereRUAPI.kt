package kosa.hdit5.whereru.util.retrofit.main.`interface`

import kosa.hdit5.whereru.ChatListVO
import kosa.hdit5.whereru.util.retrofit.main.vo.MissingBoardVo
import kosa.hdit5.whereru.util.retrofit.main.vo.UserVO
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface WhereRUAPI {

    @POST("whereru/main/main")
    fun getTotalList(): Call<List<MissingBoardVo>>


    @POST("whereru/user/login")
    fun login(@Body user: UserVO): Call<ResponseBody>

    @POST("whereru/user/register")
    fun register(@Body user:UserVO): Call<ResponseBody>

    @GET("whereru/chat/list")
    fun getChatRoomList(): Call<List<ChatListVO>>
}