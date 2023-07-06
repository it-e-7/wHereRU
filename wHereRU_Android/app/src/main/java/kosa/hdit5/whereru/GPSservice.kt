import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleService
import com.google.android.gms.location.*
import kosa.hdit5.whereru.util.GlobalState
import kosa.hdit5.whereru.util.GlobalState.userToken
import kosa.hdit5.whereru.util.retrofit.main.RetrofitBuilder
import kosa.hdit5.whereru.util.retrofit.main.`interface`.WhereRUAPI
import kosa.hdit5.whereru.util.retrofit.main.vo.LocationVO
import kosa.hdit5.whereru.util.retrofit.main.vo.NoticeVO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GPSservice : LifecycleService() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate() {
        super.onCreate()
        startLocationUpdates(this)
    }

    // 위치 업데이트 시작
    fun startLocationUpdates(context: Context) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.requestLocationUpdates(
                createLocationRequest(),
                locationCallback,
                null
            )
        }
    }

    // 위치 요청 생성
    private fun createLocationRequest(): LocationRequest {
        return LocationRequest.create().apply {
            //interval = 300000 // 위치 업데이트 간격(ms) - 5분
            //fastestInterval = 300000 // 가장 빠른 위치 업데이트 간격(ms) - 5분
            //priority = LocationRequest.PRIORITY_HIGH_ACCURACY // 정확도 우선
        }
    }

    // 위치 업데이트 콜백
    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            locationResult ?: return
            var latitude: Double = 0.0
            var longitude: Double = 0.0
            var userToken = GlobalState.userToken
            for (location in locationResult.locations) {
                latitude = location.latitude
                longitude = location.longitude
            }
            Log.d("location","$userToken")
            val locationvo = LocationVO(userToken,latitude, longitude)
            if (latitude != null && longitude != null) {
                // 위경도 전송
                Log.d("location","${locationvo.latitude}")
                Log.d("location","${locationvo.longitude}")
                val service: WhereRUAPI = RetrofitBuilder.api
                var call = service.sendLoc(locationvo)

                call.enqueue(object : Callback<Boolean> {
                    override fun onResponse(
                        call: Call<Boolean>,
                        response: Response<Boolean>
                    ) {
                        Log.d("location", response.toString())
                        if (response.isSuccessful && response.body() != null) {
                            Log.d("location","success")
                        }
                    }
                    override fun onFailure(call: Call<Boolean>, t: Throwable) {
                        Log.d("location","error!!!")
                    }

                })
            }
        }
    }

}


