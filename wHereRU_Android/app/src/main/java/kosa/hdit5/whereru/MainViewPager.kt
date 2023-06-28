package kosa.hdit5.whereru

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import kosa.hdit5.whereru.databinding.ActivityMainViewPagerBinding
import kosa.hdit5.whereru.databinding.MainItemPagerBinding
import kosa.hdit5.whereru.util.retrofit.main.RetrofitBuilder
import kosa.hdit5.whereru.util.retrofit.main.`interface`.WhereRUAPI
import kosa.hdit5.whereru.util.retrofit.main.vo.MainMissingBoardVo
import kosa.hdit5.whereru.util.retrofit.main.vo.MissingBoardVo
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MyPagerViewHolder(val binding : MainItemPagerBinding) : RecyclerView.ViewHolder(binding.root)

class MyPagerAdapter(private var myData: MutableList<MainMissingBoardVo>): RecyclerView.Adapter<MyPagerViewHolder>() {
    override fun getItemCount(): Int {
        return myData.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPagerViewHolder {
        return MyPagerViewHolder(
            MainItemPagerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyPagerViewHolder, position: Int) {
        val binding = holder.binding

        Log.d("bindviewholder", myData[position].toString())

        binding.mainMissingName.text = myData[position].missingName
        binding.mainMissingAge.text = myData[position].missingAge.toString()
        binding.mainMissingSex.text = myData[position].missingSex
    }

    fun setItem(data: MutableList<MainMissingBoardVo>) {
        Log.d("setItem", data.toString())
        myData = data
        notifyDataSetChanged()
    }

}
class MainViewPager : Fragment() {
    private lateinit var binding: ActivityMainViewPagerBinding
    private var apiService: WhereRUAPI = RetrofitBuilder.api
    private lateinit var myDataAdapter: MyPagerAdapter
    private val myData = mutableListOf<MainMissingBoardVo>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivityMainViewPagerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        myDataAdapter = MyPagerAdapter(myData)
        binding.mainviewpager.adapter = myDataAdapter

        getTotalList()

//        val testList = mutableListOf<MainMissingBoardVo>()
//        val test = MainMissingBoardVo("홍길동", 20, "남자")
//        Log.d("hong", "여기 실행 됐니?")
//        testList.add(test)
//        myDataAdapter.setItem(testList)
//        Log.d("hong", "여기 실행 됐니?22")
    }

    private fun getTotalList() {
        val call = apiService.getTotalList()
        Log.d("Hong","일단 여기로 들어옴")
        call.enqueue(object : Callback<List<MissingBoardVo>> {
            override fun onResponse(

                call: Call<List<MissingBoardVo>>,
                response: Response<List<MissingBoardVo>>
            ) {
                Log.d("Hong","onResponse까지 옴")
                Log.d("Hong","$response")
                if (response.isSuccessful) {
                    Log.d("Hong","response.isSuccessful 까지 들어옴")
                    val missingPersonList = response.body()
                    if(missingPersonList !=null){
                        for(missingPerson in missingPersonList){
                            val missingName = missingPerson.missingName
                            val age = missingPerson.missingAge
                            val sex = missingPerson.missingSex

                            val mainMissingBoardVo = MainMissingBoardVo(missingName,age,sex)
                            myData.add(mainMissingBoardVo)

                        }
                        myDataAdapter.setItem(myData)
                    }
                } else {
                    // 서버로부터 응답을 받지 못한 경우 처리
                    Log.d("Hong","실패")
                }
            }

            override fun onFailure(call: Call<List<MissingBoardVo>>, t: Throwable) {
                // 요청 자체가 실패한 경우 처리
                Log.d("Hong","ERROR")
            }
        })
    }
}