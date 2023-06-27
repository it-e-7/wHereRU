package kosa.hdit5.whereru

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kosa.hdit5.whereru.databinding.ActivityChatBinding
import kosa.hdit5.whereru.util.OkHttpClientSingleton
import kosa.hdit5.whereru.util.WebSocketListener
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket

class ChatActivity : AppCompatActivity() {

    private lateinit var client: OkHttpClient
    private lateinit var chatSocket: WebSocket

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityChatBinding.inflate(layoutInflater)

        setContentView(binding.root)

        client = OkHttpClientSingleton.instance

        val request: Request = Request.Builder()
            .url("http://10.0.2.2:8080/whereru/chatSocket")
            .build()
        val listener: WebSocketListener = WebSocketListener()

        chatSocket = client.newWebSocket(request, listener)

        binding.chatButton.setOnClickListener {
            chatSocket.send(binding.chatEdit.text.toString())
            binding.chatEdit.text.clear()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        client.dispatcher.executorService.shutdown()
    }
}