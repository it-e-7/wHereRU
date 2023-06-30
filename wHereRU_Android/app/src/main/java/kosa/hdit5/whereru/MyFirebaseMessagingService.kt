package kosa.hdit5.whereru

import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    class MyFirebaseMessagingService : FirebaseMessagingService() {
        override fun onMessageReceived(remoteMessage: RemoteMessage) {
            super.onMessageReceived(remoteMessage)

            // 푸시 알림 메시지를 수신하였을 때 실행되는 코드
            // 인앱 메시지를 토스트로 출력하는 함수 호출
            val message = remoteMessage.notification?.body ?: ""
            showToast(message)
        }

        private fun showToast(message: String) {
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

}

