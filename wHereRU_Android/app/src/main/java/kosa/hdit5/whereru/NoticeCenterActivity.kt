package kosa.hdit5.whereru

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kosa.hdit5.whereru.databinding.ActivityNoticeCenterBinding
import kosa.hdit5.whereru.databinding.NoticeItemBinding

class MyViewHolder(var binding: NoticeItemBinding): RecyclerView.ViewHolder(binding.root)

class MyAdapter(var noticeList : MutableList<String>): RecyclerView.Adapter<RecyclerView.ViewHolder>(){

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
        binding.noticeContent.text = noticeList[position]//textview에 list에 있는 data를 가져와서 적용
        if(position % 2 == 0) {
            holder.itemView.setBackgroundColor(Color.parseColor("#f3f3f3"))
        }
    }
}

class NoticeCenterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityNoticeCenterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var noticeList = mutableListOf<String>()

        for(i in 1..3) {
            //여기에 sts에서 받아온 내용을 입력
            noticeList.add("여기는 알림메시자나 채팅내용이 출력되는 곳 입니다.")

        }
        binding.noticeList.layoutManager = LinearLayoutManager(this)
        //이게 레이아웃 매니저
        var myAdapter = MyAdapter(noticeList)

        binding.noticeList.adapter = myAdapter

        binding.gobackButton.setOnClickListener {
            onBackPressed()
        }
    }
}