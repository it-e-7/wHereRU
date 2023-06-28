package kosa.hdit5.whereru

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kosa.hdit5.whereru.databinding.ActivityChatBinding
import kosa.hdit5.whereru.databinding.ChatItemBinding
import kosa.hdit5.whereru.util.GlobalState
import kosa.hdit5.whereru.util.OkHttpClientSingleton
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okio.ByteString
import org.json.JSONObject
import java.text.SimpleDateFormat

data class ChatVO (
    val chatSender: String,
    val chatReceiver: String,
    val chatType: String,
    val chatContent: String,
    val chatDate: String
)
class ChatViewHolder(val binding: ChatItemBinding): RecyclerView.ViewHolder(binding.root)

class ChatAdapter(var data: MutableList<ChatVO>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun getItemCount(): Int {
        return data.size;
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ChatViewHolder(ChatItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var binding = (holder as ChatViewHolder).binding

        if(data[position].chatType == "text") {
            binding.chatSender.text = data[position].chatSender
            binding.chatText.text = data[position].chatContent
            binding.chatDate.text = data[position].chatDate
        } else {

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
    var chatAdapter = ChatAdapter(mutableListOf())
    lateinit var receiverId: String

    inner class WebSocketListener : okhttp3.WebSocketListener() {

        override fun onOpen(webSocket: WebSocket, response: Response) {
            webSocket.send("{\"message\":\"안드로이드로부터 온 메세지\"}")
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            Log.d("Socket","Receiving : $text")
            val chatJson = JSONObject(text)
            val chatvo = ChatVO(chatJson.getString("chatSender"),
                                chatJson.getString("chatReceiver"),
                                chatJson.getString("chatType"),
                                chatJson.getString("chatContent"),
                                chatJson.getString("chatDate"))
            runOnUiThread {
                chatAdapter.addItem(chatvo)
                ActivityChatBinding.inflate(layoutInflater).chatBox
            }
        }

        override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
            Log.d("Socket", "Receiving bytes : $bytes")
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            Log.d("Socket","Closing : $code / $reason")
            webSocket.close(1000, null)
            webSocket.cancel()
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            Log.d("Socket","Error : " + t.message)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityChatBinding.inflate(layoutInflater)

        setContentView(binding.root)

        receiverId = intent.getStringExtra("sender") ?: ""

        binding.chatBox.layoutManager = LinearLayoutManager(this)
        binding.chatBox.adapter = chatAdapter

        client = OkHttpClientSingleton.instance

        val request: Request = Request.Builder()
            .url("http://10.0.2.2:8080/whereru/chatSocket")
            .build()
        val listener: WebSocketListener = WebSocketListener()

        chatSocket = client.newWebSocket(request, listener)

        binding.chatButton.setOnClickListener {

            chatSocket.send(createChatJSON(binding.chatEdit.text.toString()))
            binding.chatEdit.text.clear()
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        client.dispatcher.executorService.shutdown()
    }

    fun createChatJSON(text: String): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd-hh-mm")
        val date = sdf.format(System.currentTimeMillis())

        return "{" +
                "\"chatSender\":\"" + GlobalState.userId + "\"," +
                "\"chatReceiver\":\"" + receiverId + "\"," +
                "\"chatType\":\"text\"," +
                "\"chatContent\":\"" + text + "\"," +
                "\"chatDate\":\"" + date + "\"" +
                "}"
    }
}