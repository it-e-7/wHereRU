package kosa.hdit5.whereru

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kosa.hdit5.whereru.databinding.ActivityChatListBinding
import kosa.hdit5.whereru.databinding.ChatListItemBinding
import kosa.hdit5.whereru.util.GlobalState

data class ChatListVO (
    val chatSenderId: String,
    val chatSenderName: String,
    val chatSenderImgUrl: String,
    val chatContent: String,
    val chatDate: String,
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

        binding.chatListItemName.text = data[position].chatSenderName
        binding.chatListItemChat.text = data[position].chatContent
        binding.chatListItemDate.text = data[position].chatDate
        binding.chatListItemCount.text = data[position].chatCount.toString()

        holder.itemView.setOnClickListener {
            val chatIntent = Intent(binding.root.context, ChatActivity::class.java)
            chatIntent.putExtra("sender", data[position].chatSenderId)
            chatIntent.run { binding.root.context.startActivity(chatIntent) }
        }
    }
}

class ChatListActivity : AppCompatActivity() {

    var chatListData = mutableListOf<ChatListVO>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityChatListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.chatListBox.layoutManager = LinearLayoutManager(this)
        var chatListAdapter = ChatListAdapter(chatListData)
        binding.chatListBox.adapter = chatListAdapter

        if(GlobalState.userId == "hong") {
            var chatListVO = ChatListVO("shin","신사임당","d","아이야","06-28",2)
            chatListData.add(chatListVO)
        } else {
            var chatListVO = ChatListVO("hong","홍길동","d","ㅎㅇ","06-28",1)
            chatListData.add(chatListVO)
        }
    }
}