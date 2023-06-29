package kosa.hdit5.whereru

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kosa.hdit5.whereru.databinding.ActivityCheckUserIdExistBinding
import kosa.hdit5.whereru.databinding.ActivityRegisterBinding

class CheckUserIdExistActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityCheckUserIdExistBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}

