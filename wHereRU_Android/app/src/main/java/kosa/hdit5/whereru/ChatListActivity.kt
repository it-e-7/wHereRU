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
class ChatListViewHolder(val binding: ChatListItemBinding): RecyclerView.ViewHolder(binding.root)

class ChatListAdapter(var data: MutableList<ChatListVO>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun getItemCount(): Int {
        return data.size;
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ChatListViewHolder(ChatListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var binding = (holder as ChatListViewHolder).binding

        binding.chatListItemName.text = data[position].senderName
        binding.chatListItemChat.text = data[position].lastChatContent
        binding.chatListItemDate.text = data[position].lastChatDate
        binding.chatListItemCount.text = data[position].chatCount.toString()

        holder.itemView.setOnClickListener {
            val chatIntent = Intent(binding.root.context, ChatActivity::class.java)
            chatIntent.putExtra("sender", data[position].senderId)
            chatIntent.putExtra("roomSeq", data[position].roomSeq)
            chatIntent.run { binding.root.context.startActivity(chatIntent) }
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
            var intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    fun getChatRoomList() {
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