package kosa.hdit5.whereru

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kosa.hdit5.whereru.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}