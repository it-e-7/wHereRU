package kosa.hdit5.whereru.util.retrofit.main.`interface`

import kosa.hdit5.whereru.ChatListVO
import kosa.hdit5.whereru.ChatVO
import kosa.hdit5.whereru.util.retrofit.main.vo.MissingBoardVo
import kosa.hdit5.whereru.util.retrofit.main.vo.UserVO
import kosa.hdit5.whereru.util.retrofit.main.vo.writeMissingBoardVo
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface WhereRUAPI {

    @POST("whereru/main/main")
    fun getTotalList(): Call<List<MissingBoardVo>>

    @POST("whereru/main/writemissingboard")
    fun writeMissingBoard(@Body writeMissingBoardVo: writeMissingBoardVo): Call<String>

    @POST("whereru/user/login")
    fun login(@Body user: UserVO): Call<UserVO>

    @POST("whereru/user/register")
    fun register(@Body user:UserVO): Call<ResponseBody>

    @GET("whereru/chat/list")
    fun getChatRoomList(): Call<List<ChatListVO>>

    @GET("whereru/chat")
    fun getChatList(@Query("roomSeq") roomSeq: Int): Call<List<ChatVO>>

    /*@GET("whreru/user/checkuseridExist")
    fun checkUserIdExist(@Query("userId") userId: String): Call<Boolean>*/

    @POST("whereru/main/detail")
    fun getMissingBoardDetail(@Body params: Int): Call<MissingBoardVo>
}
