package kosa.hdit5.whereru



import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import kosa.hdit5.whereru.databinding.ActivityMainBinding

import kosa.hdit5.whereru.databinding.ActivityMainViewPagerBinding
import kosa.hdit5.whereru.util.GlobalState

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

        // 파이어베이스 메시징 인스턴스로 토큰생성or가져오기
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                val exception = task.exception
                exception?.let {
                    // 토큰 가져오기 실패
                    showError("Fetching FCM registration token failed: ${it.message}")
                }
                return@OnCompleteListener
            }
            val token = task.result
            Log.d("토큰", token.toString()) // 내 토큰은 나중에 채팅알림에 사용해야 할듯
        })

        // 알림 채널 생성 및 설정
        createNotificationChannel()
        // 알림 생성 및 표시
        showNotification()

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
            //FCM http 요청
            Log.d("========Start========","")
            val requestNoticeService = RequestNoticeService()
            requestNoticeService.requestToFCM()
            Log.d("=========End==========","")
        }
    }

    // 알림 채널 생성 및 설정
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = channelDescription
            }

            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    // 알림 생성 및 표시
    private fun showNotification() {
        val title = "Notification Title"
        val message = "Notification Message"

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.icon)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(notificationId, notificationBuilder.build())

    }




    private fun showError(message: String) {
        Log.e("Error", message)
    }
}
