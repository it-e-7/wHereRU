package kosa.hdit5.whereru

import android.util.Log
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException
import okhttp3.MediaType.Companion.toMediaType

class RequestNoticeService {
    fun requestToFCM() {
        try {
            val url = "https://fcm.googleapis.com/fcm/send"

            // FCM 서버 키
            val serverKey = "AAAAQGwJreo:APA91bERH85R8sckereChqMrm1niq1MQh7qXOEXSESjpvn5eDPzt72z_1JT114p5IFv90z8dAeHJ88l62__SIKpkXuVdoDU1QVWbMgGna96_K297YIuEB9_A0OtX0lfiN1cAFtFswkuE"

            // 수신 대상 디바이스의 FCM 토큰
            val targetToken =
                "exmGr566QyK-xaWtBhRwwX:APA91bFvrFLt95pdmrrea_Qa_ZqH3P-AdW7R-NGspGKFdDPbvfQR60a3u0WwfA2GD_ZFEverBKDopMiUqbEwtaEur_XhtYBiY0DVz7Arn0sDGg8au8SSk-AI2zUhC00b5S3ohSu_ZPWu"

            // 메시지 데이터
            val messageData = HashMap<String, Any>()
            messageData["title"] = "test"
            messageData["body"] = "test"
            Log.d("====noti=====",""+messageData)

            // 메시지 생성
            val message = HashMap<String, Any>()
            message["to"] = targetToken
            message["data"] = messageData

            // OkHttpClient 생성
            val client = OkHttpClient()

            // RequestBody 생성
            val mediaType = "application/json; charset=utf-8".toMediaType()
            val requestBody = RequestBody.create(mediaType, Gson().toJson(message))

            // Request 생성
            val request = Request.Builder()
                .url(url)
                .addHeader("Authorization", "key=$serverKey")
                .post(requestBody)
                .build()

            // 요청 실행
            client.newCall(request).enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    val responseBody = response.body?.string()
                    Log.d("FCM", "FCM 메시지 전송 성공: $responseBody")
                }

                override fun onFailure(call: Call, e: IOException) {
                    Log.e("FCM", "FCM 메시지 전송 실패: ${e.message}")
                }
            })
        } catch (e: Exception) {
            Log.e("FCM", "FCM 메시지 전송 중 예외 발생: ${e.message}")
        }
    }
}
