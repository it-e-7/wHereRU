package kosa.hdit5.whereru

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.messaging.FirebaseMessaging
import kosa.hdit5.whereru.databinding.ActivityRegisterBinding
import kosa.hdit5.whereru.util.retrofit.main.RetrofitBuilder
import kosa.hdit5.whereru.util.retrofit.main.`interface`.WhereRUAPI
import kosa.hdit5.whereru.util.retrofit.main.vo.UserVO
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.registerBtn.setOnClickListener {
            val userName = binding.registerName.text.toString()
            val userId = binding.registerId.text.toString()
            val userPw = binding.loginPw.text.toString()
            val userPwConfirm = binding.loginCheckPw.text.toString()
            val userToken = intent.getStringExtra("token")
            val user = UserVO(null, userName, userId, userPw,userToken)
            val registerService: WhereRUAPI = RetrofitBuilder.api
            val call = registerService.register(user)

            // 패스워드 검사
            if (userPw != userPwConfirm) {
                Toast.makeText(this@RegisterActivity, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }


            call.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@RegisterActivity, "회원가입에 성공했습니다.", Toast.LENGTH_LONG).show()
                        var loginIntent = Intent(this@RegisterActivity, LoginActivity::class.java)
                        startActivity(loginIntent)
                    } else {
                        Toast.makeText(this@RegisterActivity, "회원가입에 실패했습니다.", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(this@RegisterActivity, "통신 오류가 발생했습니다.", Toast.LENGTH_LONG).show()
                }
            })
        }

        binding.leftArrow.setOnClickListener {
            this.finish()
        }
    }
}
