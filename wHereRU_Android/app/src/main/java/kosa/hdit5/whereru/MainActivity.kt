package kosa.hdit5.whereru

import GPSservice
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
import android.content.pm.PackageManager
import android.view.View
import androidx.core.content.ContextCompat
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.inappmessaging.FirebaseInAppMessaging
import com.google.firebase.messaging.RemoteMessage
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import kosa.hdit5.whereru.databinding.ActivityMainBinding
import kosa.hdit5.whereru.util.GlobalState
import kosa.hdit5.whereru.util.retrofit.main.RetrofitBuilder


class MainActivity : AppCompatActivity() {

    lateinit var token:String
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
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // APN 설정에 접근할 수 있는 권한이 부여되었으므로 처리할 코드를 추가하세요.
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //gps실행 및 전송
        val gps = GPSservice()
        gps.startLocationUpdates(this)
        // FCM을 위해 토큰 가져오기
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                token = task.result
                Log.d("토큰-메인", "$token")
            } else {
                val exception = task.exception
                Log.e("Error", "Fetching FCM registration token failed: ${exception?.message}")
            }
        }

        binding.noticecenterButton.setOnClickListener {
            val intent = Intent(this, NoticeCenterActivity::class.java)
            startActivity(intent)
        }

        val fragment = MainViewPager()
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, fragment)
            .commit()

        binding.writeButton.setOnClickListener {
            val loginCheck = GlobalState.isLogin
            if(loginCheck==true){
                val intent = Intent(this, WritePageActivity::class.java)

                resultLauncher.launch(intent)
            }else{
                val intent = Intent(this,LoginActivity::class.java)
                startActivity(intent)
            }

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
            val loginCheck = GlobalState.isLogin
            //Log.d("one","CLEAR${RetrofitBuilder.cookieJar}")
            if(loginCheck!=false){
                //적당한 페이지 이동(마이페이지)
                val intent = Intent(this, MyPageActivity::class.java)
                startActivity(intent)
            }else{
                val intent = Intent(this,LoginActivity::class.java)
                intent.putExtra("token",token)
                startActivity(intent)
            }
        }

        binding.footer.footerLine.visibility = View.GONE
        binding.footer.homeIcon.setImageResource(R.drawable.home_icon_active)
    }


}
