package kosa.hdit5.whereru

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.widget.Toast

class InAppMSGService : Service() {

    val CHANNEL_ID = "ForegroundChannel"

    override fun onBind(intent: Intent): IBinder {
        return Binder()
    }
    //채널 생성 함수
    // 채널 생성 함수
    fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Foreground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
    }

    // 알림 실행
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // 채널 객체 생성
        createNotificationChannel()

        // 토스트 메시지 표시
        Toast.makeText(this, "Test Toast Message", Toast.LENGTH_SHORT).show()

        return super.onStartCommand(intent, flags, startId)
    }


}