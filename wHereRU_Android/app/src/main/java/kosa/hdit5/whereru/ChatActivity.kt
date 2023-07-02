package kosa.hdit5.whereru

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kosa.hdit5.whereru.databinding.ActivityChatBinding
import kosa.hdit5.whereru.databinding.ChatItemBinding
import kosa.hdit5.whereru.databinding.LeftChatItemBinding
import kosa.hdit5.whereru.util.GlobalState
import kosa.hdit5.whereru.util.OkHttpClientSingleton
import kosa.hdit5.whereru.util.retrofit.main.RetrofitBuilder
import kosa.hdit5.whereru.util.retrofit.main.`interface`.WhereRUAPI
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okio.ByteString
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import java.text.SimpleDateFormat
import kotlin.reflect.typeOf

data class ChatVO (
    val chatSender: String,
    val chatReceiver: String,
    val chatType: String,
    val chatContent: String,
    val chatDate: String,
    var viewType: Int
)

object ViewType {
    val CENTER_JOIN = 0
    val LEFT_CHAT = 1
    val RIGHT_CHAT = 2
}
class ChatViewHolder(val binding: ChatItemBinding): RecyclerView.ViewHolder(binding.root)
class LeftChatViewHolder(val binding: LeftChatItemBinding): RecyclerView.ViewHolder(binding.root)

class ChatAdapter(var data: MutableList<ChatVO>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun getItemCount(): Int {
        return data.size;
    }

    override fun getItemViewType(position: Int): Int {
        return data[position].viewType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if(viewType == ViewType.LEFT_CHAT) {
            LeftChatViewHolder(LeftChatItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        } else {
            ChatViewHolder(ChatItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if(holder is LeftChatViewHolder) {
            var binding = holder.binding

            if(data[position].chatType == "text") {
                binding.chatSender.text = data[position].chatSender
                binding.chatText.text = data[position].chatContent
                binding.chatDate.text = data[position].chatDate
            } else {

            }
        } else {
            var binding = (holder as ChatViewHolder).binding

            if(data[position].chatType == "text") {
                binding.chatSender.text = data[position].chatSender
                binding.chatText.text = data[position].chatContent
                binding.chatDate.text = data[position].chatDate
            } else {

            }
        }


    }

    fun addItem(chat: ChatVO) {
        data.add(chat)
        notifyDataSetChanged()
    }
    fun setItem(newData: MutableList<ChatVO>) {
        data = newData
        notifyDataSetChanged()
    }
}
class ChatActivity : AppCompatActivity() {

    private lateinit var client: OkHttpClient
    private lateinit var chatSocket: WebSocket
    var roomSeq: Int = -1
    var chatAdapter = ChatAdapter(mutableListOf<ChatVO>())
    lateinit var receiverId: String
    lateinit var binding: ActivityChatBinding
    private var apiService: WhereRUAPI = RetrofitBuilder.api

    inner class WebSocketListener : okhttp3.WebSocketListener() {

        override fun onOpen(webSocket: WebSocket, response: Response) {
            webSocket.send("{\"type\":\"socket-open\", \"user\": \"" + GlobalState.userId + "\"}")
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            Log.d("ChatActivity","Socket Receiving : $text")
            val chatJson = JSONObject(text)
            var viewType = ViewType.RIGHT_CHAT;

            if(GlobalState.userId == chatJson.getString("chatReceiver")) {
                viewType = ViewType.LEFT_CHAT;
            }

            val chatvo = ChatVO(chatJson.getString("chatSender"),
                                chatJson.getString("chatReceiver"),
                                chatJson.getString("chatType"),
                                chatJson.getString("chatContent"),
                                chatJson.getString("chatDate"),
                                viewType)

            runOnUiThread {
                chatAdapter.addItem(chatvo)
                binding.chatBox.smoothScrollToPosition(chatAdapter.itemCount)
            }
        }

        override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
            Log.d("ChatActivity", "Receiving bytes : $bytes")
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            Log.d("ChatActivity","Closing : $code / $reason")
            webSocket.close(1000, null)
            webSocket.cancel()
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            Log.d("ChatActivity","Error : " + t.message)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChatBinding.inflate(layoutInflater)

        setContentView(binding.root)

        receiverId = intent.getStringExtra("sender") ?: ""

        binding.chatBox.layoutManager = LinearLayoutManager(this)
        binding.chatBox.adapter = chatAdapter

        createSocketConnection()

        binding.chatButton.setOnClickListener {
            chatSocket.send(createChatJSON(binding.chatEdit.text.toString()))
            binding.chatEdit.text.clear()
        }

        roomSeq = intent.getIntExtra("roomSeq", -1)
        getChatList()
    }


    fun createChatJSON(text: String): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd hh:mm")
        val date = sdf.format(System.currentTimeMillis())

        return "{" +
                "\"chatSender\":\"" + GlobalState.userId + "\"," +
                "\"chatReceiver\":\"" + receiverId + "\"," +
                "\"chatType\":\"text\"," +
                "\"chatContent\":\"" + text + "\"," +
                "\"chatDate\":\"" + date + "\"" +
                "}"
    }

    fun createSocketConnection() {
        client = OkHttpClientSingleton.instance

        val request: Request = Request.Builder()
            .url("http://10.0.2.2:8080/whereru/chatSocket")
            .build()
        val listener: WebSocketListener = WebSocketListener()

        chatSocket = client.newWebSocket(request, listener)
    }

    fun getChatList() {
        val call = apiService.getChatList(roomSeq)
        call.enqueue(object : Callback<List<ChatVO>> {
            override fun onResponse(

                call: Call<List<ChatVO>>,
                response: retrofit2.Response<List<ChatVO>>
            ) {
                if (response.isSuccessful) {
                    var chatList = response.body()
                    if(chatList !=null){
                        var newData = mutableListOf<ChatVO>()

                        for(chat in chatList) {
                            if(GlobalState.userId == chat.chatSender) {
                                chat.viewType = ViewType.RIGHT_CHAT
                            } else {
                                chat.viewType = ViewType.LEFT_CHAT
                            }
                            newData.add(chat)
                        }
                        Log.d("ChatActivity", "채팅 리스트 : $chatList")
                        chatAdapter.setItem(newData)
                        binding.chatBox.smoothScrollToPosition(chatAdapter.itemCount)
                    }
                } else {
                    // 서버로부터 응답을 받지 못한 경우 처리
                    Log.d("ChatActivity","채팅 리스트 받아오기 실패")
                }

            }

            override fun onFailure(call: Call<List<ChatVO>>, t: Throwable) {
                // 요청 자체가 실패한 경우 처리
                Log.d("ChatActivity","채팅 리스트 ERROR")
            }
        })
    }
}