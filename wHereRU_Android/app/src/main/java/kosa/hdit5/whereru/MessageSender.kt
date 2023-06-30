package kosa.hdit5.whereru

import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.RemoteMessage

class MessageSender {
    companion object {
        fun sendInAppMessage(token: String, title: String, message: String) {
            Log.d("로그","서비스실행")
            val data = hashMapOf(
                "title" to title,
                "message" to message
            )
            Log.d("로그","서비스생성 데이터 : $data")
            val remoteMessage = RemoteMessage.Builder(token)
                .setData(data)
                .build()
            Log.d("로그","서비스생성 메시지 : $remoteMessage")
            FirebaseMessaging.getInstance().send(remoteMessage)
        }
    }
}