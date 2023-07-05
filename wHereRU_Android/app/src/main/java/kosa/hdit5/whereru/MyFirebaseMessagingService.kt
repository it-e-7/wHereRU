package kosa.hdit5.whereru

import android.app.ActivityManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.RingtoneManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

private fun isChatActivityForeground(context: Context): Boolean {
    val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

    // 최상위 액티비티 정보 가져오기
    val topActivity = activityManager.getRunningTasks(1)?.get(0)?.topActivity

    // 최상위 액티비티가 ChatActivity인지 확인
    return topActivity?.className == ChatActivity::class.java.name
}

class MyFirebaseMessagingService : FirebaseMessagingService() {

        override fun onMessageReceived(remoteMessage: RemoteMessage) {
            // FCM 메시지 수신 시 호출되는 콜백 함수
            if (remoteMessage.notification != null) {
                // 포어그라운드면 인앱알림(토스트)
                if (isAppInForeground(applicationContext)) {
                   // 포그라운드 액티비티 판별해서 채팅이면 안보내고 아니면 보냄
                    if (isChatActivityForeground(this)) {
                    } else {
                        val message = remoteMessage.notification?.body
                        showToast(message)
                    }
                } else {
                    // 백그라운드/종료상태면 시스템알림
                    val message = remoteMessage.notification?.body
                    showNotification(message)
                }
            }
        }

        private fun isAppInForeground(context: Context): Boolean {
            //포어그라운드인지 백그라운드인지 여기서 판단하고 불린값 리턴!!이것만 처리하면돼!!
                val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager
                val appProcesses = activityManager?.runningAppProcesses ?: return false

                val packageName = context.packageName
                for (appProcess in appProcesses) {
                    if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND &&
                        appProcess.processName == packageName) {
                        return true
                    }
                } 
                return false
        }

        private fun showToast(message: String?) {
            Handler(Looper.getMainLooper()).post {
                val inflater = LayoutInflater.from(applicationContext)
                val layout = inflater.inflate(R.layout.custom_toast, null)
                val textView = layout.findViewById<TextView>(R.id.toast_message)
                textView.text = message

                val toast = Toast(applicationContext)
                toast.duration = Toast.LENGTH_SHORT
                toast.view = layout
                toast.setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, 16)
                toast.show()
            }
        }
        
        //시스템알림은 채널생성하고 노티생성해서 띄움
        private fun showNotification(message: String?) {
            val channelId = "default_channel_id"
            val channelName = "Default Channel"

            val notificationBuilder = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
                .setContentTitle("Notification Title")
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))

            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    channelId,
                    channelName,
                    NotificationManager.IMPORTANCE_DEFAULT
                )
                notificationManager.createNotificationChannel(channel)
            }
            notificationManager.notify(0, notificationBuilder.build())
        }
}



