package kosa.hdit5.whereru

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kosa.hdit5.whereru.databinding.ActivityDetailViewPagerBinding
import kosa.hdit5.whereru.databinding.DetailItemPagerBinding
import kosa.hdit5.whereru.util.retrofit.main.RetrofitBuilder
import kosa.hdit5.whereru.util.retrofit.main.`interface`.WhereRUAPI
import kosa.hdit5.whereru.util.retrofit.main.vo.DetailMissingBoardVo
import kosa.hdit5.whereru.util.retrofit.main.vo.MissingBoardVo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailPagerViewHolder(val binding: DetailItemPagerBinding) : RecyclerView.ViewHolder(binding.root){
    val imageView: ImageView = binding.detailImg
}

class DetailPagerAdapter(private var DetailData: MutableList<DetailMissingBoardVo>) : RecyclerView.Adapter<DetailPagerViewHolder>() {
    private val imageUrlList = mutableListOf<String>()

    override fun getItemCount(): Int {
        return imageUrlList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailPagerViewHolder {
        return DetailPagerViewHolder(
            DetailItemPagerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: DetailPagerViewHolder, position: Int) {
        val binding = holder.binding
        val ImageView = holder.imageView
        Log.d("hong","imageLink : ${imageUrlList[position]}")
        Glide.with(ImageView.context)
            .load(imageUrlList[position]).fitCenter()
            .into(ImageView)
    }

    fun setItem(data: MutableList<DetailMissingBoardVo>) {
        Log.d("setItem", data.toString())
        DetailData = data
        buildImageUrlList()
        notifyDataSetChanged()
    }

    private fun buildImageUrlList() {
        Log.d("function", "building image url list")
        imageUrlList.clear()
        Log.d("function","${DetailData}")
        DetailData.forEach { DetailMissingBoardVo ->
            imageUrlList.addAll(DetailMissingBoardVo.getImageUrls())  // 문자열을 리스트로 변환하고 추가
        }
        Log.d("function", "list built: $imageUrlList")
    }
}

class DetailViewPager : Fragment() {
    private lateinit var binding: ActivityDetailViewPagerBinding
    private var apiService: WhereRUAPI = RetrofitBuilder.api
    private lateinit var DetailDataAdapter: DetailPagerAdapter
    private val DetailData = mutableListOf<DetailMissingBoardVo>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ActivityDetailViewPagerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val missingSeq = arguments?.getInt("missingBoardSeq", 0)
        DetailDataAdapter = DetailPagerAdapter(DetailData)
        binding.detailviewpager.adapter = DetailDataAdapter

        if (missingSeq != null) {
            Log.d("missing","$missingSeq")
            getTotalList(missingSeq)
        } else {
            // handle missingSeq being null
        }
    }

    private fun getTotalList(missingSeq: Int) {
        val call = apiService.getMissingBoardDetail(missingSeq)
        Log.d("missing","$call")
        call.enqueue(object : Callback<DetailMissingBoardVo> {
            override fun onResponse(
                call: Call<DetailMissingBoardVo>,
                response: Response<DetailMissingBoardVo>
            ) {
                Log.d("missing", "$response")
                if (response.isSuccessful) {
                    val missingPersonimgList = response.body()
                    if (missingPersonimgList != null) {
                        DetailData.add(missingPersonimgList)
//                        missingPersonimgList.forEach { img ->
//                            DetailData.add(img)
//                        }
                        DetailDataAdapter.setItem(DetailData)
                    }
                } else {
                    // 서버로부터 응답을 받지 못한 경우 처리
                    Log.d("missing", "실패")
                }
            }

            override fun onFailure(call: Call<DetailMissingBoardVo>, t: Throwable) {
                // 요청 자체가 실패한 경우 처리
                Log.d("missing", "${t.message}")
            }
        })
    }

}
