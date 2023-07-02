package kosa.hdit5.whereru

import android.app.Notification

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import android.content.Intent
import androidx.core.content.ContextCompat
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.inappmessaging.FirebaseInAppMessaging
import com.google.firebase.messaging.RemoteMessage
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import kosa.hdit5.whereru.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {


    lateinit var binding:ActivityMainBinding

    private val channelId = "default_channel_id"
    private val channelName = "Default Channel"
    private val channelDescription = "Default Channel for Notifications"
    private val notificationId = 1

    val resultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        //Contract가 나와야함
        ActivityResultContracts.StartActivityForResult()

    ){
        //결과 처리하는 부분
            result ->
        if(result.resultCode== Activity.RESULT_OK){
            //만약 결과가 OK면 처리를 진행
            val fragment = MainViewPager()
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 파이어베이스 메시징 인스턴스로 토큰 생성 또는 가져오기
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
                Log.d("토큰", token ?: "Token is null")

            } else {
                val exception = task.exception
                Log.e("Error", "Fetching FCM registration token failed: ${exception?.message}")
            }
        }

        binding.chatButton.setOnClickListener {
            val intent = Intent(this, ChatListActivity::class.java)
            startActivity(intent)
        }

        val fragment = MainViewPager()
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, fragment)
            .commit()
        binding.writeButton.setOnClickListener {
            val intent = Intent(this, WritePageActivity::class.java)

            resultLauncher.launch(intent)
        }


        binding.noticeButton.setOnClickListener {
//            val token = "dwZTkMA2SfGmBSxr8iIpZN"
//            val title = "Sample Title"
//            val message = "Sample Message"
//            Log.d("로그","버튼실행")
//            MessageSender.sendInAppMessage(token, title, message)
//            val intent = Intent(this,InAppMSGService::class.java)
//            ContextCompat.startForegroundService(this,intent)
//            sendFCMMessage()
            val token = "ezYV150HQni7HzCP3e2_BP:APA91bHH1FlIXD-0g_IX06WNkzaRz_aCkvVOO4RkDa2IbFJfyG9oAtf5gv-6sF8XRmCj-0fMVggcntMG45DmSTCoqBQtsTYPpDtSWdiUDwxtR3T_hWLvQi0uVWStDYU-y5e3M5i8o1gm" // 2번 디바이스의 토큰 정보를 입력합니다.

            // FirebaseMessaging 인스턴스를 가져옵니다.
            val firebaseMessaging = FirebaseMessaging.getInstance()

            // 푸시 알림을 생성합니다.
            val notification = RemoteMessage.Builder(token)
                .setMessageId("your_message_id")
                .setData(mapOf("title" to "In-App Message", "body" to "토스트로 출력되는 인앱 메시지입니다."))
                .build()

            // 푸시 알림을 2번 디바이스로 전송합니다.
            firebaseMessaging.send(notification)
        }


        binding.detailButton.setOnClickListener {
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("missingBoardSeq", 122)
            startActivity(intent)
        }
    }
    private fun sendFCMMessage() {
        Log.d("hi","sendmsg")
        val message = HashMap<String, String>()
        message["title"] = "인앱메시지 제목"
        message["content"] = "인앱메시지 내용"

        val targetDeviceToken = "exmGr566QyK-xaWtBhRwwX"

        val data = HashMap<String, String>()
        data["message"] = message.toString()

        FirebaseMessaging.getInstance().send(
            RemoteMessage.Builder(targetDeviceToken)
                .setData(data)
                .build()
        )
    }

}
