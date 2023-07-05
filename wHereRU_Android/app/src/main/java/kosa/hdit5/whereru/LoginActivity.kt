package kosa.hdit5.whereru

import android.app.Activity
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kosa.hdit5.whereru.databinding.ActivityLoginBinding
import kosa.hdit5.whereru.util.GlobalState
import kosa.hdit5.whereru.util.retrofit.main.RetrofitBuilder
import kosa.hdit5.whereru.util.retrofit.main.`interface`.WhereRUAPI
import kosa.hdit5.whereru.util.retrofit.main.vo.UserVO
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MyCookieJar : CookieJar {
    private var cookies: List<Cookie> = ArrayList()

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        this.cookies = cookies
    }

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        return cookies
    }

    fun clearCookies() {
        cookies = ArrayList()
        GlobalState.isLogin=false
    }
}
class LoginActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //로그인 버튼 클릭
        binding.loginBtn.setOnClickListener {
            val userId = binding.loginId.text.toString()
            val userPw = binding.loginPw.text.toString()
            val loginService: WhereRUAPI=RetrofitBuilder.api//LoginService = retrofit.create(LoginService::class.java)
            val call = loginService.login(UserVO(userId = userId, userPw = userPw))

            call.enqueue(object : Callback<UserVO> {
                override fun onResponse(call: Call<UserVO>, response: Response<UserVO>) {

                    Log.d("hong","onRespnose들어옴")
                    Log.d("hong", "$response")

                    val loginVo = response.body()
                    Log.d("hong","$loginVo")
                    if (response.isSuccessful) {
                        Toast.makeText(this@LoginActivity, "로그인 성공", Toast.LENGTH_SHORT).show()
                        var mainIntent = Intent(this@LoginActivity, MainActivity::class.java)
                        GlobalState.isLogin = true;
                        GlobalState.userId = userId;
                        GlobalState .userToken = intent.getStringExtra("token")
                        GlobalState.userName = loginVo?.userName
                        GlobalState.userSeq = (loginVo?.userSeq)?.toInt()
                        
                        startActivity(mainIntent)
                    } else {
                        Toast.makeText(this@LoginActivity, "로그인 실패", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<UserVO>, t: Throwable) {
                    Toast.makeText(this@LoginActivity, "통신 에러", Toast.LENGTH_SHORT).show()
                }
            })
        }

        // 회원가입 버튼 클릭 이벤트
        binding.registerLink.setOnClickListener {
            val token = intent.getStringExtra("token")
            val registerIntent = Intent(this, RegisterActivity::class.java)
            registerIntent.putExtra("token",token)
            startActivity(registerIntent)
        }
    }
}