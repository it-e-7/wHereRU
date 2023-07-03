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

        // FCM을 위해 토큰 가져오기
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

        }

        // footer 설정 !!!
        binding.footer.homeIcon.setOnClickListener {
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        binding.footer.chatIcon.setOnClickListener {
            var intent = Intent(this, ChatListActivity::class.java)
            startActivity(intent)
        }
        binding.footer.mypageIcon.setOnClickListener {
            var intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }


}
