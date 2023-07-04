package kosa.hdit5.whereru.util.retrofit.main.`interface`

import kosa.hdit5.whereru.ChatListVO
import kosa.hdit5.whereru.ChatVO
import kosa.hdit5.whereru.util.retrofit.main.vo.DetailMissingBoardVo
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

    @POST("whereru/notice/getnoticelist")
    fun getNoticeList(@Body userToken: String?): Call<List<NoticeVO>>
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

    @GET("whereru/chat/user")
    fun getChatListByReceiverSeq(@Query("receiverSeq") receiverSeq: Int, @Query("missingSeq") missingSeq: Int): Call<List<ChatVO>>

    /*@GET("whreru/user/checkuseridExist")
    fun checkUserIdExist(@Query("userId") userId: String): Call<Boolean>*/

    @POST("whereru/main/detail")
    fun getMissingBoardDetail(@Body params: Int): Call<DetailMissingBoardVo>

    @POST("whereru/main/deletemissingboard")
    fun deleteMissingBoard(@Body params: Int): Call<DetailMissingBoardVo>

    @GET("whereru/main/openchat/{missingBoardSeq}")
    fun openChatActivity(@Path("missingBoardSeq") missingBoardSeq: Int): Call<DetailMissingBoardVo>

    @GET("whereru/main/summary")
    fun getMissingBoardSummary(@Query("roomSeq") roomSeq: Int, @Query("missingSeq") missingSeq: Int): Call<MissingBoardVo>
}
