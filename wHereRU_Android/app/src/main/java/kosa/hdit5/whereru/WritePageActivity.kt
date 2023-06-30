package kosa.hdit5.whereru

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.storage.FirebaseStorage
import kosa.hdit5.whereru.databinding.ActivityWritePageBinding
import kosa.hdit5.whereru.util.GlobalState
import kosa.hdit5.whereru.util.retrofit.main.RetrofitBuilder
import kosa.hdit5.whereru.util.retrofit.main.`interface`.WhereRUAPI
import kosa.hdit5.whereru.util.retrofit.main.vo.MissingBoardVo
import kosa.hdit5.whereru.util.retrofit.main.vo.UserVO
import kosa.hdit5.whereru.util.retrofit.main.vo.writeMissingBoardVo
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WritePageActivity : AppCompatActivity() {

    lateinit var binding: ActivityWritePageBinding

    val fbStorage = FirebaseStorage.getInstance()

    var storage = fbStorage.reference.child("images")


    var imageButton1Bitmap: Uri? = null
    var imageButton2Bitmap: Uri? = null
    var imageButton3Bitmap: Uri? = null
    // 사진 추가 -> 갤러리 연동
    var imagePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            if (data != null) {
                val imageUri: Uri? = data.data
                Log.d("hong","$imageUri")
                if (imageUri != null) {
                    when {
                        imageButton1Bitmap == null -> {
                            imageButton1Bitmap = imageUri
                            binding.imgButton1.setImageURI(imageButton1Bitmap)

                        }

                        imageButton2Bitmap == null -> {
                            imageButton2Bitmap = imageUri
                            binding.imgButton2.setImageURI(imageButton2Bitmap)
                        }

                        imageButton3Bitmap == null -> {
                            imageButton3Bitmap = imageUri
                            binding.imgButton3.setImageURI(imageButton3Bitmap)
                        }
                    }
                }
            }
        }
    }
    var count:Int = 0
    var imgUrl1: String? =null
    var imgUrl2: String? =null
    var imgUrl3: String? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWritePageBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.imgButton1.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            imagePickerLauncher.launch(intent)
        }

        binding.imgButton2.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            imagePickerLauncher.launch(intent)
        }

        binding.imgButton3.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            imagePickerLauncher.launch(intent)
        }

        binding.finishWriteButton.setOnClickListener {

            var imgFileName1 = (GlobalState.userSeq.toString())+".1.png"
            var imgFileName2 = (GlobalState.userSeq.toString())+".2.png"
            var imgFileName3 = (GlobalState.userSeq.toString())+".3.png"
            var storage1 = storage.child(imgFileName1)
            var storage2 = storage.child(imgFileName2)
            var storage3 = storage.child(imgFileName3)


            if (storage1 != null && imageButton1Bitmap != null) {
                storage1.putFile(imageButton1Bitmap!!).addOnSuccessListener { uploadTask ->
                    Log.d("hong", "upload 성공")
                    uploadTask.storage.downloadUrl
                        .addOnSuccessListener {
                            imgUrl1=it.toString()

                            Log.d("hong", "$it")
                            checkUploadComplete()
                        }
                        .addOnFailureListener {
                            // Handle failure
                        }

                }
            }else{
                checkUploadComplete()
            }

            if (storage2 != null && imageButton2Bitmap != null) {
                storage2.putFile(imageButton2Bitmap!!).addOnSuccessListener { uploadTask ->
                    Log.d("hong", "upload 성공")
                    uploadTask.storage.downloadUrl
                        .addOnSuccessListener {
                            imgUrl2=it.toString()
                            Log.d("hong", "$it")
                            checkUploadComplete()
                        }
                        .addOnFailureListener {
                            // Handle failure
                        }

                }
            }else{
                checkUploadComplete()
            }

            if (storage3 != null && imageButton3Bitmap != null) {
                storage3.putFile(imageButton3Bitmap!!).addOnSuccessListener { uploadTask ->
                    Log.d("hong", "upload 성공")
                    uploadTask.storage.downloadUrl
                        .addOnSuccessListener {
                            imgUrl3=it.toString()
                            Log.d("hong", "$it")
                            checkUploadComplete()
                        }
                        .addOnFailureListener {
                            // Handle failure
                        }

                }
            }else{
                checkUploadComplete()
            }


        }

    }
    private fun checkUploadComplete(){
        count++

        if(count==3){
            val writeName = binding.writeName.text.toString()
            val writeAge = binding.writeAge.text.toString()
            val writeSex = binding.writeSex.text.toString()
            val writeOutfit = binding.writeOutfit.text.toString()
            val writeTime = binding.writeTime.text.toString()
            val writePoint = binding.writePoint.text.toString()
            Log.d("hong","imgurl1:$imgUrl1")
            val writeService: WhereRUAPI = RetrofitBuilder.api
            val call = writeService.writeMissingBoard(writeMissingBoardVo(missingName = writeName, missingAge = writeAge.toInt(), missingSex = writeSex, missingOutfit = writeOutfit, missingTime = writeTime, missingPoint = writePoint, imgUrl1 = imgUrl1, imgUrl2 = imgUrl2, imgUrl3 = imgUrl3, userSeq = GlobalState.userSeq))

            call.enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    count = 0
                    if (response.isSuccessful) {
                        setResult(RESULT_OK)
                        finish()
                    } else {

                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    count = 0
                }


            })

        }
    }


}