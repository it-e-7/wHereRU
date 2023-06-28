package kosa.hdit5.whereru

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kosa.hdit5.whereru.databinding.ActivityLoginBinding
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginBtn.setOnClickListener {
            val user_Id = binding.loginId.text.toString()
            val user_Pw = binding.loginPw.text.toString()

            val retrofit = Retrofit.Builder()
                .baseUrl("")
                .addConverterFactory(GsonConverterFactory.create())
                .build()


        }
    }
}