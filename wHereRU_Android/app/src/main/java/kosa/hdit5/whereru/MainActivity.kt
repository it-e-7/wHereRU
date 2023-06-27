package kosa.hdit5.whereru

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
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kosa.hdit5.whereru.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val channelId = "default_channel_id"
    private val channelName = "Default Channel"
    private val channelDescription = "Default Channel for Notifications"
    private val notificationId = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 알림 채널 생성 및 설정
        createNotificationChannel()

        // 파이어베이스 메시징 인스턴스 생성
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
            Log.d("토큰", token.toString())
        })

        // 알림 생성 및 표시
        showNotification()

        binding.chatButton.setOnClickListener {
            val intent = Intent(this, ChatActivity::class.java)
            startActivity(intent)
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
