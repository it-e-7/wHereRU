package kosa.hdit5.whereru.util.retrofit.main.`interface`

import kosa.hdit5.whereru.util.retrofit.main.vo.MissingBoardVo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST

interface WhereRUAPI {

    @POST("whereru/main/main")
    fun getTotalList(): Call<List<MissingBoardVo>>
}