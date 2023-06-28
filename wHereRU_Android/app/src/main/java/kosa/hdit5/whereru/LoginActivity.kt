package kosa.hdit5.whereru

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kosa.hdit5.whereru.databinding.ActivityLoginBinding
import kosa.hdit5.whereru.util.GlobalState
import kosa.hdit5.whereru.util.retrofit.main.RetrofitBuilder
import kosa.hdit5.whereru.util.retrofit.main.`interface`.WhereRUAPI
import kosa.hdit5.whereru.util.retrofit.main.okHttpClient
import kosa.hdit5.whereru.util.retrofit.main.vo.UserVO
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MyCookieJar : CookieJar {
    private var cookies: List<Cookie> = ArrayList()

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        this.cookies = cookies
    }

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        return cookies
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
            val call = loginService.login(UserVO(userId, userPw))

            call.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    Log.d("hong","onRespnose들어옴")
                    if (response.isSuccessful) {
                        Toast.makeText(this@LoginActivity, "로그인 성공", Toast.LENGTH_SHORT).show()
                        var mainIntent = Intent(this@LoginActivity, MainActivity::class.java)
                        GlobalState.isLogin = true;
                        GlobalState.userId = userId;
                        startActivity(mainIntent)
                    } else {
                        Toast.makeText(this@LoginActivity, "로그인 실패", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(this@LoginActivity, "통신 에러", Toast.LENGTH_SHORT).show()
                }
            })
        }

        // 회원가입 버튼 클릭 이벤트
        binding.registerLink.setOnClickListener {

            val registerIntent = Intent(this, RegisterActivity::class.java)
            startActivity(registerIntent)
        }
    }
}