package kosa.hdit5.whereru

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import kosa.hdit5.whereru.databinding.ActivityMyPageBinding
import kosa.hdit5.whereru.util.GlobalState
import kosa.hdit5.whereru.util.retrofit.main.RetrofitBuilder
import kosa.hdit5.whereru.util.retrofit.main.`interface`.WhereRUAPI
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl


class MyPageActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMyPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.logoutName.text = GlobalState.userName
        binding.logoutId.text = GlobalState.userId

        // 뒤로가기 버튼
        binding.leftArrow.setOnClickListener {
            this.finish()
        }

        // 로그아웃 버튼
        binding.logoutBtn.setOnClickListener {
            // 쿠키 제거
            RetrofitBuilder.cookieJar.clearCookies()

            Log.d("one", "${GlobalState.userName}")
            val intent = Intent(this@MyPageActivity, MainActivity::class.java)
            startActivity(intent)
            finish()  // MyPageActivity 종료
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
            var intent = Intent(this, MyPageActivity::class.java)
            startActivity(intent)
        }
        binding.footer.mypageIcon.setImageResource(R.drawable.mypage_icon_active)
    }
}