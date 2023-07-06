package kosa.hdit5.whereru.util.retrofit.main.`interface`

import kosa.hdit5.whereru.ChatListVO
import kosa.hdit5.whereru.ChatVO
import kosa.hdit5.whereru.util.retrofit.main.vo.DetailMissingBoardVo
import kosa.hdit5.whereru.util.retrofit.main.vo.LocationVO
import kosa.hdit5.whereru.util.retrofit.main.vo.MissingBoardVo
import kosa.hdit5.whereru.util.retrofit.main.vo.NoticeVO
import kosa.hdit5.whereru.util.retrofit.main.vo.UserVO
import kosa.hdit5.whereru.util.retrofit.main.vo.writeMissingBoardVo
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface WhereRUAPI {

    @POST("notice/sendLoc")
    fun sendLoc(@Body locationvo: LocationVO): Call<Boolean>

    @POST("notice/getnoticelistLogin")
    fun getNoticeListLogin(@Body userToken: String?): Call<List<NoticeVO>>

    @POST("notice/getnoticelistLogout")
    fun getNoticeListLogout(@Body userToken: String?): Call<List<NoticeVO>>


    @POST("main/main")
    fun getTotalList(): Call<List<MissingBoardVo>>

    @POST("main/writemissingboard")
    fun writeMissingBoard(@Body writeMissingBoardVo: writeMissingBoardVo): Call<String>

    @POST("user/login")
    fun login(@Body user: UserVO): Call<UserVO>

    @POST("user/register")
    fun register(@Body user:UserVO): Call<ResponseBody>

    @GET("chat/list")
    fun getChatRoomList(): Call<List<ChatListVO>>

    @GET("chat")
    fun getChatList(@Query("roomSeq") roomSeq: Int): Call<List<ChatVO>>

    @GET("chat/user")
    fun getChatListByReceiverSeq(@Query("receiverSeq") receiverSeq: Int, @Query("missingSeq") missingSeq: Int): Call<List<ChatVO>>

    /*@GET("whreru/user/checkuseridExist")
    fun checkUserIdExist(@Query("userId") userId: String): Call<Boolean>*/

    @POST("main/detail")
    fun getMissingBoardDetail(@Body params: Int): Call<DetailMissingBoardVo>

    @POST("main/deletemissingboard")
    fun deleteMissingBoard(@Body params: Int): Call<DetailMissingBoardVo>

    @GET("main/openchat/{missingBoardSeq}")
    fun openChatActivity(@Path("missingBoardSeq") missingBoardSeq: Int): Call<DetailMissingBoardVo>

    @GET("main/summary")
    fun getMissingBoardSummary(@Query("roomSeq") roomSeq: Int, @Query("missingSeq") missingSeq: Int): Call<MissingBoardVo>
}
