package kosa.hdit5.whereru

import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kosa.hdit5.whereru.databinding.ActivityDetailBinding
import kosa.hdit5.whereru.util.retrofit.main.RetrofitBuilder
import kosa.hdit5.whereru.util.retrofit.main.`interface`.WhereRUAPI
import kosa.hdit5.whereru.util.retrofit.main.vo.MissingBoardVo
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.properties.Delegates

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private var ifFabOpen = false
    var isAuthor by Delegates.notNull<Boolean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 프래그먼트 인스턴스 생성
        val fragment = DetailViewPager()

        // 프래그먼트를 프래그먼트 컨테이너에 추가
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
           .commit()


        val missingBoardSeq = intent.getIntExtra("missingBoardSeq", -1)
        Log.d("log.missingBoardSeq", "$missingBoardSeq")


        if (missingBoardSeq != -1) {
            //val params = HashMap<String, Any>()
            //params["missingBoardSeq"] = missingBoardSeq

            val detailPageService: WhereRUAPI = RetrofitBuilder.api
            val call = detailPageService.getMissingBoardDetail(missingBoardSeq)

            Log.d("log.call", "$call")
            call.enqueue(object : Callback<MissingBoardVo> {
                override fun onResponse(
                    call: Call<MissingBoardVo>,
                    response: Response<MissingBoardVo>
                ) {
                    Log.d("DetailActivityCustom", response.toString())
                    if (response.isSuccessful && response.body() != null) {
                        val detail = response.body()!!
                        isAuthor = detail.owner
                        Log.d("log.owner", "$isAuthor")

                        binding.detailName.text = detail.missingName
                        binding.detailAge.text = detail.missingAge.toString()
                        binding.detailSex.text = detail.missingSex
                        binding.detailOutfit.text = detail.missingOutfit
                        binding.detailTime.text = detail.missingTime
                        binding.detailPoint.text = detail.missingPoint

                        // 작성자, 일반인에 따라 다른 화면 보이기
                        if (detail.owner) {
                            binding.fabDelete.visibility = View.VISIBLE
                            binding.fabChat.visibility = View.GONE
                        } else {
                            binding.fabChat.visibility = View.VISIBLE
                            binding.fabDelete.visibility = View.GONE
                        }
                    }
                }

                override fun onFailure(call: Call<MissingBoardVo>, t: Throwable) {
                    // Handle failure here
                    Log.d("DetailActivityCustom", t.message.toString())
                    Log.e("DetailPageActivity", "Failed to retrieve missing board detail", t)
                }
            })
        }

        binding.fab.setOnClickListener {
            toggleFab()
        }
    }

    private fun toggleFab() {
        val fabHeight = binding.fab.height.toFloat()

        val isAuthor = true

        if (ifFabOpen) {
            if (isAuthor) {
                // 글 작성자
                ObjectAnimator.ofFloat(binding.fabDelete, "translationY", -fabHeight - 24).apply { start() }
            } else {
                // 일반사람
                ObjectAnimator.ofFloat(binding.fabChat, "translationY", -fabHeight).apply { start() }
            }

        } else {
                if (isAuthor) {
                    // 글 작성자
                    ObjectAnimator.ofFloat(binding.fabDelete, "translationY", 0f).apply { start() }
                } else {
                    // 일반사람
                    ObjectAnimator.ofFloat(binding.fabChat, "translationY", 0f).apply { start() }
                }
        }

        ifFabOpen = !ifFabOpen
    }
}
