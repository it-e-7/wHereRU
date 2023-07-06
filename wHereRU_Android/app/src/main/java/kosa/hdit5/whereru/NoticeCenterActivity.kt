package kosa.hdit5.whereru

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kosa.hdit5.whereru.databinding.ActivityNoticeCenterBinding
import kosa.hdit5.whereru.databinding.NoticeItemBinding
import kosa.hdit5.whereru.util.GlobalState
import kosa.hdit5.whereru.util.retrofit.main.RetrofitBuilder
import kosa.hdit5.whereru.util.retrofit.main.`interface`.WhereRUAPI
import kosa.hdit5.whereru.util.retrofit.main.vo.NoticeVO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyViewHolder(var binding: NoticeItemBinding): RecyclerView.ViewHolder(binding.root)

class MyAdapter(var noticeList : MutableList<NoticeVO>): RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    // 반드시 오버라이딩해야하는 3가지
    override fun getItemCount(): Int {
        //자동으로 호출
        //항목의 갯수를 판단해서 리턴
        //그래야 몇개를 만들지 판단하고 그만큼 홀더가 호출된다.
        return noticeList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        //얘도 자동으로 호출
        //항목을 표현하는 객체생성
        return MyViewHolder(NoticeItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
        //요거 주의
    }
    //ViewHolder에 data binding !!
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        //이것도 자동호출
        var binding = (holder as MyViewHolder).binding//casting해서 실제 binding객체를 담는다
       //textview에 list에 있는 data를 가져와서 적용

            binding.noticeTime.text = noticeList[position].notiTime
            if(position % 2 == 0) {
                holder.itemView.setBackgroundColor(Color.parseColor("#f3f3f3"))
            }
            if(noticeList[position].notiType == "system") {
                binding.noticeContent.text = noticeList[position].notiMessage
                binding.noticeSender.text = "도움이 필요한 사람이 생겼어요!"
                binding.noticeImage.setImageResource(R.drawable.icon)
            } else {
                binding.noticeSender.text = noticeList[position].notiSender
                binding.noticeImage.setImageResource(R.drawable.chat_icon)
                if (noticeList[position].msgType == "img"){
                    binding.noticeContent.text = "사진을 보냈습니다."
                } else {
                    binding.noticeContent.text = noticeList[position].notiMessage
                }
            }
    }
}

class NoticeCenterActivity : AppCompatActivity() {

     val noticeItemList = mutableListOf<NoticeVO>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityNoticeCenterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.noticeList.layoutManager = LinearLayoutManager(this)
        //이게 레이아웃 매니저
        var myAdapter = MyAdapter(noticeItemList)
        binding.noticeList.adapter = myAdapter

        val Service: WhereRUAPI = RetrofitBuilder.api
        Log.d("요청 토큰","${GlobalState.userToken}")
        if(GlobalState.isLogin==true){
            val call = Service.getNoticeListLogin(GlobalState.userToken)
            call.enqueue(object : Callback<List<NoticeVO>> {
                override fun onResponse(
                    call: Call<List<NoticeVO>>,
                    response: Response<List<NoticeVO>>
                ) {
                    Log.d("callcheck", response.toString())
                    if (response.isSuccessful && response.body() != null) {
                        noticeItemList.clear() // 기존 데이터 초기화
                        noticeItemList.addAll(response.body()!!) // 새로운 데이터 추가
                        myAdapter.notifyDataSetChanged() // 어댑터 갱신
                    }
                }
                override fun onFailure(call: Call<List<NoticeVO>>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
        } else {
            val call = Service.getNoticeListLogout(GlobalState.userToken)
            call.enqueue(object : Callback<List<NoticeVO>> {
                override fun onResponse(
                    call: Call<List<NoticeVO>>,
                    response: Response<List<NoticeVO>>
                ) {
                    Log.d("callcheck", response.toString())
                    if (response.isSuccessful && response.body() != null) {
                        noticeItemList.clear() // 기존 데이터 초기화
                        noticeItemList.addAll(response.body()!!) // 새로운 데이터 추가
                        myAdapter.notifyDataSetChanged() // 어댑터 갱신
                        if(!noticeItemList.isEmpty()){
                            binding.noticeEmpty.visibility = View.GONE
                        }
                    }
                }
                override fun onFailure(call: Call<List<NoticeVO>>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
        }

        binding.gobackButton.setOnClickListener {
            onBackPressed()
        }
    }
}