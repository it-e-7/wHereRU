import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleService
import com.google.android.gms.location.*
import kosa.hdit5.whereru.util.retrofit.main.RetrofitBuilder
import kosa.hdit5.whereru.util.retrofit.main.`interface`.WhereRUAPI

class GPSservice : LifecycleService() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate() {
        super.onCreate()
        Log.d("location","일단 함수 호출?")
        startLocationUpdates(this)
    }

    // 위치 업데이트 시작
    fun startLocationUpdates(context: Context) {
        Log.d("location","ㅇㅋㅇㅋ")
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        Log.d("location","ㅇㅋㅇㅋ")
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("location","ㅇㅋㅇㅋ")
            fusedLocationClient.requestLocationUpdates(
                createLocationRequest(),
                locationCallback,
                null
            )
        }
    }

    // 위치 요청 생성
    private fun createLocationRequest(): LocationRequest {
        Log.d("location","여기까지 온다고?")
        return LocationRequest.create().apply {
            interval = 300000 // 위치 업데이트 간격(ms) - 5분
            fastestInterval = 300000 // 가장 빠른 위치 업데이트 간격(ms) - 5분
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY // 정확도 우선
        }
    }

    // 위치 업데이트 콜백
    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            Log.d("location","근데 이게 안된다고?")
            locationResult ?: return
            val locationList: MutableList<Double> = mutableListOf()
            for (location in locationResult.locations) {
                locationList.add(location.latitude)
                locationList.add(location.longitude)
                Log.d("location","위경도 검색?")
            }
            if (locationList[0] != null && locationList[1] != null) {
                // 위경도 전송
                Log.d("location","위경도 전송?")
                Log.d("location","${locationList[0]}")
                Log.d("location","${locationList[1]}")
                val service: WhereRUAPI = RetrofitBuilder.api
                service.setLocation(locationList)
            }
        }
    }

}


