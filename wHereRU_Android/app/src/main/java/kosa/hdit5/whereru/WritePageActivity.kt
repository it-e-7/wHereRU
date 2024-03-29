package kosa.hdit5.whereru

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Rect
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.firebase.storage.FirebaseStorage
import kosa.hdit5.whereru.databinding.ActivityWritePageBinding
import kosa.hdit5.whereru.util.GlobalState
import kosa.hdit5.whereru.util.retrofit.main.RetrofitBuilder
import kosa.hdit5.whereru.util.retrofit.main.`interface`.WhereRUAPI
import kosa.hdit5.whereru.util.retrofit.main.vo.writeMissingBoardVo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.UUID

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
                if (imageUri != null) {
                    when {
                        imageButton1Bitmap == null -> {
                            imageButton1Bitmap = imageUri
                            binding.firstImgView.setImageURI(imageButton1Bitmap)
                            binding.firstImgView.setBackgroundResource(android.R.color.transparent)
                            binding.imgView1.visibility = View.GONE
                            noImage = true

                        }

                        imageButton2Bitmap == null -> {
                            imageButton2Bitmap = imageUri
                            binding.secondImgView.setImageURI(imageButton2Bitmap)
                            binding.secondImgView.setBackgroundResource(android.R.color.transparent)
                            binding.imgView2.visibility = View.GONE
                        }

                        imageButton3Bitmap == null -> {
                            imageButton3Bitmap = imageUri
                            binding.thirdImgView.setImageURI(imageButton3Bitmap)
                            binding.thirdImgView.setBackgroundResource(android.R.color.transparent)
                            binding.imgView3.visibility = View.GONE
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


    //시도
//    private lateinit var fusedLocationClient: FusedLocationProviderClient
//    private val REQUEST_CODE_LOCATION: Int = 2
//    var currentLocation: String = ""
//    var latitude: Double? = null
//    var longitude: Double? = null
//
//    private val locationCallback = object : LocationCallback() {
//        override fun onLocationResult(locationResult: LocationResult) {
//            super.onLocationResult(locationResult)
//            for (location in locationResult.locations) {
//                latitude = location.latitude
//                longitude = location.longitude
//                Log.d("CheckCurrentLocation", "현재 내 위치 값: $latitude, $longitude")
//
//                var mGeocoder = Geocoder(applicationContext, Locale.KOREAN)
//                var mResultList: List<Address>? = null
//                try {
//                    mResultList = mGeocoder.getFromLocation(
//                        latitude!!, longitude!!, 1
//                    )
//                } catch (e: IOException) {
//                    e.printStackTrace()
//                }
//                if (mResultList != null) {
//                    Log.d("CheckCurrentLocation", mResultList[0].getAddressLine(0))
//                    currentLocation = mResultList[0].getAddressLine(0)
//                }
//            }
//        }
//    }
//
//    private fun getCurrentLoc() {
//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
//
//        // 위치 권한 확인
//        if (ContextCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) == PackageManager.PERMISSION_GRANTED
//        ) {
//            val locationRequest = LocationRequest.create().apply {
//                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
//                interval = 1000 // 위치 업데이트 간격 (1초마다 업데이트)
//            }
//
//            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
//        } else {
//            // 위치 권한이 없는 경우 권한 요청
//            ActivityCompat.requestPermissions(
//                this,
//                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
//                REQUEST_CODE_LOCATION
//            )
//        }
//    }


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
        //시도
//        binding.location.setOnClickListener {
//            getCurrentLoc()
//        }

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

                    // 선택한 날짜와 시간 보여주기
                    binding.writeDateTime.text = formattedDateTime
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
        binding.firstImgView.setOnClickListener(clickListener)
        binding.secondImgView.setOnClickListener(clickListener)
        binding.thirdImgView.setOnClickListener(clickListener)
        binding.imgView1.setOnClickListener(clickListener)
        binding.imgView2.setOnClickListener(clickListener)
        binding.imgView3.setOnClickListener(clickListener)


        binding.finishWriteButton.setOnClickListener {
            Log.d("hong","버튼버튼버튼ㅐ")
            if(noImage==true){


                var imgFileName1 = UUID.randomUUID().toString() + ".png"
                var imgFileName2 = UUID.randomUUID().toString() + ".png"
                var imgFileName3 = UUID.randomUUID().toString() + ".png"
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
                    Log.d("youmin","RESPONSE: $response")
                    Log.d("youmin","RESPONSE: ${response.body()}")
                    Log.d("youmin","RESPONSE: ${response.errorBody()}")
                    if (response.isSuccessful) {
                        setResult(RESULT_OK)

                        finish()
                    } else {

                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    count = 0
                    Log.d("youmin","FAIL WITH: ${t.message}")
                    setResult(RESULT_OK)

                    finish()
                }


            })

        }
    }



}
