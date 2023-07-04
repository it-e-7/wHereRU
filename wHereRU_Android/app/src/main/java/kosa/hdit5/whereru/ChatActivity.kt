package kosa.hdit5.whereru

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.updateLayoutParams
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import kosa.hdit5.whereru.databinding.ActivityChatBinding
import kosa.hdit5.whereru.databinding.ChatItemBinding
import kosa.hdit5.whereru.databinding.LeftChatItemBinding
import kosa.hdit5.whereru.util.GlobalState
import kosa.hdit5.whereru.util.OkHttpClientSingleton
import kosa.hdit5.whereru.util.retrofit.main.RetrofitBuilder
import kosa.hdit5.whereru.util.retrofit.main.`interface`.WhereRUAPI
import kosa.hdit5.whereru.util.retrofit.main.vo.DetailMissingBoardVo
import kosa.hdit5.whereru.util.retrofit.main.vo.MissingBoardVo
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okio.ByteString
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import java.text.SimpleDateFormat

data class ChatVO(
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

class ChatViewHolder(val binding: ChatItemBinding) : RecyclerView.ViewHolder(binding.root) {
    init {
        binding.chatImage.visibility = View.GONE
        binding.chatText.visibility = View.VISIBLE
    }

}
class LeftChatViewHolder(val binding: LeftChatItemBinding) : RecyclerView.ViewHolder(binding.root) {
    init {
        binding.chatImage.visibility = View.GONE
        binding.chatText.visibility = View.VISIBLE
    }
}

class ChatAdapter(var data: MutableList<ChatVO>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun getItemCount(): Int {
        return data.size;
    }

    override fun getItemViewType(position: Int): Int {
        return data[position].viewType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == ViewType.LEFT_CHAT) {
            LeftChatViewHolder(
                LeftChatItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        } else {
            ChatViewHolder(
                ChatItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is LeftChatViewHolder) {
            var binding = holder.binding

            binding.chatDate.text = getHourMin(data[position].chatDate)

            if (data[position].chatType == "text") {
                Log.d("ChatActivity", "CHATTING DATA : ${data[position]}")
                binding.chatText.text = data[position].chatContent
                binding.chatText.visibility = View.VISIBLE
                binding.chatImage.visibility = View.GONE
            } else {
                Log.d("ChatActivity", "IMAGE CHATTING DATA : ${data[position]}")
                binding.chatText.visibility = View.GONE
                binding.chatImage.visibility = View.VISIBLE
                Glide.with(holder.itemView)
                    .load(data[position].chatContent)
                    .override(600, 800)
                    .transform(CenterCrop(), RoundedCorners(30))
                    .into(binding.chatImage)
            }
        } else {
            var binding = (holder as ChatViewHolder).binding

            binding.chatDate.text = getHourMin(data[position].chatDate)

            if (data[position].chatType == "text") {
                Log.d("ChatActivity", "CHATTING DATA : ${data[position]}")
                binding.chatText.text = data[position].chatContent
                binding.chatText.visibility = View.VISIBLE
                binding.chatImage.visibility = View.GONE
            } else {
                Log.d("ChatActivity", "IMAGE CHATTING DATA : ${data[position]}")
                binding.chatText.visibility = View.GONE
                binding.chatImage.visibility = View.VISIBLE
                Glide.with(holder.itemView)
                    .load(data[position].chatContent)
                    .override(600, 800)
                    .transform(CenterCrop(), RoundedCorners(30))
                    .into(binding.chatImage)
            }
        }


    }

    fun addItem(chat: ChatVO) {
        data.add(chat)
        notifyItemInserted(data.size - 1)
    }

    fun setItem(newData: MutableList<ChatVO>) {
        data = newData
        notifyDataSetChanged()
    }

    fun getHourMin(date: String): String {
        var hour = 0
        var min = 0
        var time = date.substring(11)
        hour = time.substring(0, 2).toInt()
        min = time.substring(3).toInt()
        if (hour > 12) {
            hour -= 12
        }

        return "$hour:${min.toString().padStart(2, '0')}"
    }
}

class ChatActivity : AppCompatActivity() {

    private lateinit var client: OkHttpClient
    private lateinit var chatSocket: WebSocket
    private var roomSeq: Int = -1
    private var missingSeq: Int = -1
    private var receiverSeq: Int = -1
    private var chatAdapter = ChatAdapter(mutableListOf<ChatVO>())
    private lateinit var receiverId: String
    private lateinit var binding: ActivityChatBinding
    private var apiService: WhereRUAPI = RetrofitBuilder.api

    inner class WebSocketListener : okhttp3.WebSocketListener() {

        override fun onOpen(webSocket: WebSocket, response: Response) {
            webSocket.send("{\"type\":\"socket-open\", \"user\": \"" + GlobalState.userId + "\"}")
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            Log.d("ChatActivity", "Socket Receiving : $text")
            val chatJson = JSONObject(text)
            var viewType = ViewType.RIGHT_CHAT;

            if (GlobalState.userId == chatJson.getString("chatReceiver")) {
                viewType = ViewType.LEFT_CHAT;
            }

            val chatvo = ChatVO(
                chatJson.getString("chatSender"),
                chatJson.getString("chatReceiver"),
                chatJson.getString("chatType"),
                chatJson.getString("chatContent"),
                chatJson.getString("chatDate"),
                viewType
            )

            runOnUiThread {
                chatAdapter.addItem(chatvo)
                binding.chatBox.smoothScrollToPosition(chatAdapter.itemCount)
            }
        }

        override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
            Log.d("ChatActivity", "Receiving bytes : $bytes")
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            Log.d("ChatActivity", "Closing : $code / $reason")
            webSocket.close(1000, null)
            webSocket.cancel()
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            Log.d("ChatActivity", "Error : " + t.message)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChatBinding.inflate(layoutInflater)

        setContentView(binding.root)

        receiverId = intent.getStringExtra("sender") ?: ""
        roomSeq = intent.getIntExtra("roomSeq", -1)
        missingSeq = intent.getIntExtra("missingSeq", -1)
        receiverSeq = intent.getIntExtra("receiverSeq", -1)

        binding.chatBox.layoutManager = LinearLayoutManager(this)
        binding.chatBox.adapter = chatAdapter

        binding.chatBox.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            binding.chatBox.smoothScrollToPosition(chatAdapter.itemCount)
        }

        createSocketConnection()

        binding.chatButton.setOnClickListener {
            chatSocket.send(createChatJSON(binding.chatEdit.text.toString()))
            binding.chatEdit.text.clear()
        }

        binding.chatEdit.doOnTextChanged {
                text, start, before, count ->
            if(!text.isNullOrEmpty()) {
                binding.chatButton.setImageResource(R.drawable.chat_send_active)
            } else {
                binding.chatButton.setImageResource(R.drawable.chat_button)
            }
        }

        binding.toDetailSection.setOnClickListener {
            var detailIntent = Intent(this, DetailActivity::class.java)
            detailIntent.putExtra("missingBoardSeq", missingSeq)
            startActivity(detailIntent)
        }

        binding.arrowLeft.setOnClickListener {
            this.finish()
        }

        if(roomSeq == -1) {
            getChatListByUserSeq(receiverSeq, missingSeq)
        } else {
            getChatList()
        }

        binding.senderName.text = intent.getStringExtra("senderName")
        getMissingBoard()
    }


    fun createChatJSON(text: String): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd hh:mm")
        val date = sdf.format(System.currentTimeMillis())
        val missingSeq = intent.getIntExtra("missingSeq", 1)

        return "{" +
                "\"chatSender\":\"" + GlobalState.userId + "\"," +
                "\"chatReceiver\":\"" + receiverId + "\"," +
                "\"chatType\":\"text\"," +
                "\"chatContent\":\"" + text + "\"," +
                "\"chatDate\":\"" + date + "\"," +
                "\"missingSeq\":" + missingSeq +
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
                    if (chatList != null) {
                        var newData = mutableListOf<ChatVO>()

                        for (chat in chatList) {
                            if (GlobalState.userId == chat.chatSender) {
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
                    Log.d("ChatActivity", "채팅 리스트 받아오기 실패")
                }

            }

            override fun onFailure(call: Call<List<ChatVO>>, t: Throwable) {
                // 요청 자체가 실패한 경우 처리
                Log.d("ChatActivity", "채팅 리스트 ERROR")
            }
        })
    }

    private fun getChatListByUserSeq(receiverSeq: Int, missingSeq: Int) {
        val call = apiService.getChatListByReceiverSeq(receiverSeq, missingSeq)
        call.enqueue(object : Callback<List<ChatVO>> {
            override fun onResponse(

                call: Call<List<ChatVO>>,
                response: retrofit2.Response<List<ChatVO>>
            ) {
                if (response.isSuccessful) {
                    var chatList = response.body()
                    if (chatList != null) {
                        var newData = mutableListOf<ChatVO>()

                        for (chat in chatList) {
                            if (GlobalState.userId == chat.chatSender) {
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
                    Log.d("ChatActivity", "채팅 리스트 받아오기 실패")
                }

            }

            override fun onFailure(call: Call<List<ChatVO>>, t: Throwable) {
                // 요청 자체가 실패한 경우 처리
                Log.d("ChatActivity", "채팅 리스트 ERROR")
            }
        })
    }

    private fun getMissingBoard() {
        val call = apiService.getMissingBoardSummary(roomSeq, missingSeq)
        call.enqueue(object : Callback<MissingBoardVo> {
            override fun onResponse(
                call: Call<MissingBoardVo>,
                response: retrofit2.Response<MissingBoardVo>
            ) {
                if (response.isSuccessful) {
                    var missingBoardSummary = response.body()
                    if (missingBoardSummary != null) {
                        Log.d("ChatActivity", "게시판 데이터: $missingBoardSummary")
                        binding.detailText.text = "${missingBoardSummary.missingName} ${missingBoardSummary.missingAge}세 ${missingBoardSummary.missingSex}"
                        missingSeq = missingBoardSummary.missingSeq

                        Glide.with(binding.root)
                            .load(missingBoardSummary.imgUrl1)
                            .transform(CenterCrop(), RoundedCorners(12))
                            .into(binding.detailImage)
                    }
                } else {
                    // 서버로부터 응답을 받지 못한 경우 처리
                    Log.d("ChatActivity", "게시판 데이터 받아오기 실패")
                }

            }

            override fun onFailure(call: Call<MissingBoardVo>, t: Throwable) {
                // 요청 자체가 실패한 경우 처리
                Log.d("ChatActivity", "게시판 데이터 ERROR ${t.message}")
            }
        })

    }
}