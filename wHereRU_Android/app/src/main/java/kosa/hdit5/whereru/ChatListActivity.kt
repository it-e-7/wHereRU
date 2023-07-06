package kosa.hdit5.whereru

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kosa.hdit5.whereru.databinding.ActivityChatListBinding
import kosa.hdit5.whereru.databinding.ChatListItemBinding
import kosa.hdit5.whereru.databinding.ChatListZeroCountItemBinding
import kosa.hdit5.whereru.util.GlobalState
import kosa.hdit5.whereru.util.retrofit.main.RetrofitBuilder
import kosa.hdit5.whereru.util.retrofit.main.`interface`.WhereRUAPI
import kosa.hdit5.whereru.util.retrofit.main.vo.MainMissingBoardVo
import kosa.hdit5.whereru.util.retrofit.main.vo.MissingBoardVo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

data class ChatListVO (
    val roomSeq: Int,
    val senderId: String,
    val senderName: String,
    val senderImg: String,
    val lastChatType: String,
    val lastChatContent: String,
    val lastChatDate: String,
    val chatCount: Int
)

object ChatListViewType {
    val ZERO_COUNT = 0
    val NUM_COUNT = 1
}
class ChatListViewHolder(val binding: ChatListItemBinding): RecyclerView.ViewHolder(binding.root)
class ZeroCountChatListViewHolder(val binding: ChatListZeroCountItemBinding): RecyclerView.ViewHolder(binding.root)

class ChatListAdapter(var data: MutableList<ChatListVO>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun getItemCount(): Int {
        return data.size;
    }

    override fun getItemViewType(position: Int): Int {
        if(data[position].chatCount == 0) {
            return ChatListViewType.ZERO_COUNT
        } else {
            return ChatListViewType.NUM_COUNT
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if(viewType == ChatListViewType.ZERO_COUNT) {
            ZeroCountChatListViewHolder(ChatListZeroCountItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        } else {
            ChatListViewHolder(ChatListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var ampm = "오전"
        var hour = 0
        var min = 0
        var time = data[position].lastChatDate.substring(11)
        hour = time.substring(0,2).toInt()
        min = time.substring(3).toInt()
        if(hour >= 12) {
            ampm = "오후"
            if(hour > 12) {
                hour -= 12
            }
        }

        if(holder is ChatListViewHolder) {
            var binding = holder.binding
            binding.chatListItemName.text = data[position].senderName
            binding.chatListItemChat.text = data[position].lastChatContent
            if(data[position].lastChatType == "img") {
                binding.chatListItemChat.text = "사진을 보냈습니다."
            }
            binding.chatListItemDate.text = "$ampm $hour:${min.toString().padStart(2, '0')}"
            binding.chatListItemCount.text = data[position].chatCount.toString()
            binding.profile.text = data[position].senderName.substring(0, 1)

            holder.itemView.setOnClickListener {
                val chatIntent = Intent(binding.root.context, ChatActivity::class.java)
                chatIntent.putExtra("sender", data[position].senderId)
                chatIntent.putExtra("senderName", data[position].senderName)
                chatIntent.putExtra("roomSeq", data[position].roomSeq)
                chatIntent.run { binding.root.context.startActivity(chatIntent) }
            }
        } else if(holder is ZeroCountChatListViewHolder) {
            var binding = holder.binding
            binding.chatListItemName.text = data[position].senderName
            binding.chatListItemChat.text = data[position].lastChatContent
            if(data[position].lastChatType == "img") {
                binding.chatListItemChat.text = "사진을 보냈습니다."
            }
            binding.chatListItemDate.text = "$ampm $hour:${min.toString().padStart(2, '0')}"
            binding.profile.text = data[position].senderName.substring(0, 1)

            holder.itemView.setOnClickListener {
                val chatIntent = Intent(binding.root.context, ChatActivity::class.java)
                chatIntent.putExtra("sender", data[position].senderId)
                chatIntent.putExtra("senderName", data[position].senderName)
                chatIntent.putExtra("roomSeq", data[position].roomSeq)
                chatIntent.run { binding.root.context.startActivity(chatIntent) }
            }
        }
    }

    fun setItem(newData: MutableList<ChatListVO>) {
        data = newData;
        notifyDataSetChanged();
    }
}

class ChatListActivity : AppCompatActivity() {

    var chatListData = mutableListOf<ChatListVO>()
    lateinit var chatListAdapter: ChatListAdapter;
    private var apiService: WhereRUAPI = RetrofitBuilder.api

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityChatListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getChatRoomList()

        binding.chatListBox.layoutManager = LinearLayoutManager(this)
        chatListAdapter = ChatListAdapter(chatListData)
        binding.chatListBox.adapter = chatListAdapter


        // footer 설정 !!!
        binding.footer.homeIcon.setOnClickListener {
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        binding.footer.chatIcon.setOnClickListener {
            var intent = Intent(this, ChatListActivity::class.java)
            startActivity(intent)
        }
        binding.footer.mypageIcon.setOnClickListener {
            val loginCheck = GlobalState.isLogin
            if(loginCheck==true){
                //적당한 페이지 이동(마이페이지)
                val intent = Intent(this, MyPageActivity::class.java)

                startActivity(intent)
            }else{
                val intent = Intent(this,LoginActivity::class.java)
                startActivity(intent)
            }
        }
        binding.footer.chatIcon.setImageResource(R.drawable.chat_icon_active)
    }

    override fun onResume() {
        super.onResume()
        getChatRoomList()
    }

    private fun getChatRoomList() {
        val call = apiService.getChatRoomList()
        call.enqueue(object : Callback<List<ChatListVO>> {
            override fun onResponse(

                call: Call<List<ChatListVO>>,
                response: Response<List<ChatListVO>>
            ) {
                if (response.isSuccessful) {
                    val chatRoomList = response.body()
                    if(chatRoomList !=null){
                        Log.d("getchatlist", chatRoomList.toString())
                        chatListAdapter.setItem(chatRoomList.toMutableList())
                    }
                } else {
                    // 서버로부터 응답을 받지 못한 경우 처리
                    Log.d("getchatlist","실패")
                }

            }

            override fun onFailure(call: Call<List<ChatListVO>>, t: Throwable) {
                // 요청 자체가 실패한 경우 처리
                Log.d("getchatlist","ERROR")
            }
        })
    }
}