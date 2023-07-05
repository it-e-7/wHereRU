package kosa.hdit5.whereru

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ScrollView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import kosa.hdit5.whereru.databinding.ActivityWritePageBinding
import kosa.hdit5.whereru.util.GlobalState
import kosa.hdit5.whereru.util.retrofit.main.RetrofitBuilder
import kosa.hdit5.whereru.util.retrofit.main.`interface`.WhereRUAPI
import kosa.hdit5.whereru.util.retrofit.main.vo.writeMissingBoardVo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class WritePageActivity : AppCompatActivity() {

    lateinit var binding: ActivityWritePageBinding

    val fbStorage = FirebaseStorage.getInstance()

    var storage = fbStorage.reference.child("images")

    var noImage = false

    var imageButton1Bitmap: Uri? = null
    var imageButton2Bitmap: Uri? = null
    var imageButton3Bitmap: Uri? = null
    var formattedDateTime: String =""
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
                            Log.d("hong","imagButton1: ${imageButton1Bitmap}")
                            binding.imgButton1.setImageURI(imageButton1Bitmap)
                            binding.imgView1.visibility = View.GONE
                            binding.firstImgFrame.setBackgroundResource(android.R.color.transparent)
                            noImage = true

                        }

                        imageButton2Bitmap == null -> {
                            imageButton2Bitmap = imageUri
                            binding.imgButton2.setImageURI(imageButton2Bitmap)
                            binding.imgView2.visibility = View.GONE
                            binding.secondImgFrame.setBackgroundResource(android.R.color.transparent)
                        }

                        imageButton3Bitmap == null -> {
                            imageButton3Bitmap = imageUri
                            binding.imgButton3.setImageURI(imageButton3Bitmap)
                            binding.imgView3.visibility = View.GONE
                            binding.thirdImgFrame.setBackgroundResource(android.R.color.transparent)
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

        binding.leftArrow.setOnClickListener {
            this.finish()
        }

        val scrollView = findViewById<NestedScrollView>(R.id.write_page_scrollview) // 스크롤 뷰의 ID에 맞게 수정

        scrollView.viewTreeObserver.addOnGlobalLayoutListener {
            val rect = Rect()
            val contentView = scrollView.getChildAt(0)
            contentView.getWindowVisibleDisplayFrame(rect)

            val screenHeight = contentView.rootView.height
            val keyboardHeight = screenHeight - rect.bottom

            if (keyboardHeight > screenHeight * 0.15) {
                scrollView.post {
                }
            }

        }

        fun showDateTimePicker(){
            val currentDate = Calendar.getInstance()
            val year = currentDate.get(Calendar.YEAR)
            val month = currentDate.get(Calendar.MONTH)
            val day = currentDate.get(Calendar.DAY_OF_MONTH)
            val hour = currentDate.get(Calendar.HOUR_OF_DAY)
            val minute = currentDate.get(Calendar.MINUTE)

            val datePickerDialog = DatePickerDialog(this@WritePageActivity, { _, selectedYear, selectedMonth, selectedDay ->

                val timePickerDialog = TimePickerDialog(this@WritePageActivity, { _, selectedHour, selectedMinute ->
                    // 선택된 날짜와 시간을 처리하는 로직을 추가합니다.
                    val selectedDate = "$selectedYear-${selectedMonth + 1}-$selectedDay"
                    val selectedTime = "$selectedHour:$selectedMinute"
                    val combinedDateTime = "$selectedDate $selectedTime"

                    val desiredFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                    val date = desiredFormat.parse(combinedDateTime)
                    formattedDateTime = desiredFormat.format(date)
                    Log.d("Combined DateTime", formattedDateTime)
                }, hour, minute, true)

                timePickerDialog.show()
            }, year, month, day)

            datePickerDialog.show()
        }

        val showPickerButton = binding.showPickerButton
        showPickerButton.setOnClickListener {
            showDateTimePicker()
        }

        val clickListener = View.OnClickListener{
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            imagePickerLauncher.launch(intent)
        }
        binding.imgButton1.setOnClickListener(clickListener)
        binding.imgView1.setOnClickListener(clickListener)
        binding.imgButton2.setOnClickListener(clickListener)
        binding.imgView2.setOnClickListener(clickListener)
        binding.imgButton3.setOnClickListener(clickListener)
        binding.imgView3.setOnClickListener(clickListener)

        binding.finishWriteButton.setOnClickListener {
            Log.d("hong","버튼버튼버튼ㅐ")
            if(noImage==true){


                var imgFileName1 = (GlobalState.userSeq.toString())+".1.png"
                var imgFileName2 = (GlobalState.userSeq.toString())+".2.png"
                var imgFileName3 = (GlobalState.userSeq.toString())+".3.png"
                var storage1 = storage.child(imgFileName1)
                var storage2 = storage.child(imgFileName2)
                var storage3 = storage.child(imgFileName3)


                if (storage1 != null && imageButton1Bitmap != null) {
                    storage1.putFile(imageButton1Bitmap!!).addOnSuccessListener { uploadTask ->
                        uploadTask.storage.downloadUrl
                            .addOnSuccessListener {
                                imgUrl1=it.toString()
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

            }else{
                Toast.makeText(applicationContext, "이미지를 등록해 주세요", Toast.LENGTH_SHORT).show()
            }

        }

        binding.footer.homeIcon.setOnClickListener {
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        binding.footer.chatIcon.setOnClickListener {
            var intent = Intent(this, ChatListActivity::class.java)
            startActivity(intent)
        }
        binding.footer.mypageIcon.setOnClickListener {
            val intent = Intent(this, MyPageActivity::class.java)
        }

    }
    private fun checkUploadComplete(){
        count++

        if(count==3){
            val writeName = binding.writeName.text.toString()
            val writeAge = binding.writeAge.text.toString()
            val writeSex = binding.writeSex.text.toString()
            val writeOutfit = binding.writeOutfit.text.toString()
            val combinedDateTime = formattedDateTime
            Log.d("hong","combinedDateTime : $combinedDateTime")
            val writePoint = binding.writePoint.text.toString()
            Log.d("hong","imgurl1:$imgUrl1")
            val writeService: WhereRUAPI = RetrofitBuilder.api
            val call = writeService.writeMissingBoard(writeMissingBoardVo(missingName = writeName, missingAge = writeAge.toInt(), missingSex = writeSex, missingOutfit = writeOutfit, missingTime = combinedDateTime, missingPoint = writePoint, imgUrl1 = imgUrl1, imgUrl2 = imgUrl2, imgUrl3 = imgUrl3, userSeq = GlobalState.userSeq))

            call.enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    count = 0
                    if (response.isSuccessful) {
                        setResult(RESULT_OK)
                        Log.d("youmin","fa;slkdfj;lskdfj;l")
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
