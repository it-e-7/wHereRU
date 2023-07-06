package kosa.hdit5.whereru

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.firebase.storage.FirebaseStorage
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


class MyPagerViewHolder(val binding : MainItemPagerBinding) : RecyclerView.ViewHolder(binding.root){
    val imageView: ImageView = binding.mainImageView
}

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
        val imageView = holder.imageView

        holder.itemView.setOnClickListener {
            val intent = Intent(binding.root.context, DetailActivity::class.java)
            intent.putExtra("missingBoardSeq", myData[position].missingSeq)
            holder.itemView.context.startActivity(intent)
        }
        val info = "${myData[position].missingName+" / " + myData[position].missingAge.toString()+"세 / " + myData[position].missingSex}"
        binding.mainMissingInfo.text = info


        val imageUrl = myData[position].missingImg
        Glide.with(imageView.context).load(imageUrl).transform(RoundedCorners(8)).into(imageView)

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

    }

    private fun getTotalList() {
        myData.clear()
        myDataAdapter.notifyDataSetChanged()
        val call = apiService.getTotalList()
        Log.d("Hong","일단 여기로 들어옴")
        call.enqueue(object : Callback<List<MissingBoardVo>> {
            override fun onResponse(

                call: Call<List<MissingBoardVo>>,
                response: Response<List<MissingBoardVo>>
            ) {
                Log.d("ec2response","$response")
                Log.d("ec2response","${response.body()}")
                Log.d("ec2response","${response.errorBody()}")
                if (response.isSuccessful) {
                    val missingPersonList = response.body()
                    if(missingPersonList !=null){
                        for(missingPerson in missingPersonList){
                            val missing_seq = missingPerson.missingSeq
                            val image = missingPerson.imgUrl1
                            val missingName = missingPerson.missingName
                            val age = missingPerson.missingAge
                            val sex = missingPerson.missingSex

                            val mainMissingBoardVo = MainMissingBoardVo(missing_seq,missingName,age,sex,image)
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