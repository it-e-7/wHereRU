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
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

        override fun onMessageReceived(remoteMessage: RemoteMessage) {
            // FCM 메시지 수신 시 호출되는 콜백 함수
            if (remoteMessage.notification != null) {
                if (isAppInForeground(applicationContext)) {
                    // 포어그라운드면 인앱알림(토스트)
                    Log.d("check","여기 실행돼?")
                    val message = remoteMessage.notification?.body
                    showToast(message)
                } else {
                    Log.d("check","여기는 어때?")
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
                        Log.d("check","포어그라운드/ 트루리턴")
                        return true
                    }
                } 
                Log.d("check","백그라운드/ 폴스리턴")
                return false
        }

        private fun showToast(message: String?) {
            Log.d("check","토스트?")
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
            }
        }
        
        //시스템알림은 채널생성하고 노티생성해서 띄움
        private fun showNotification(message: String?) {
            val channelId = "default_channel_id"
            val channelName = "Default Channel"

            val notificationBuilder = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.icon)
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



