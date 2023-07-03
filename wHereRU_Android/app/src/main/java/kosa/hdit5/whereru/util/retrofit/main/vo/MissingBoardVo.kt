package kosa.hdit5.whereru.util.retrofit.main.vo

data class MissingBoardVo(
    val missingSeq: Int,
    val missingName: String,
    val missingAge: Int,
    val missingSex: String,
    val missingOutfit: String,
    val missingTime: String,
    val missingPoint: String,
    val userSeq: Int,
    val imgUrl1: String,
    val imgUrl2: String?,
    val imgUrl3: String?,
    val owner: Boolean
)

data class DetailMissingBoardVo(
    val missingSeq: Int,
    val missingName: String,
    val missingAge: Int,
    val missingSex: String,
    val missingOutfit: String,
    val missingTime: String,
    val missingPoint: String,
    val userSeq: Int,
    val owner: Boolean,
    val missingImgUrls: String
)

{
    // 쉼표로 구분된 이미지 URL 문자열을 리스트로 변환
    fun getImageUrls(): List<String> {
        return missingImgUrls?.split(",") ?: listOf()
    }
}


data class MainMissingBoardVo(
    val missingSeq:Int,
    val missingName: String,
    val missingAge: Int,
    val missingSex: String,
    val missingImg: String
)

data class writeMissingBoardVo(
    val missingName: String,
    val missingAge: Int,
    val missingSex: String,
    val missingOutfit: String,
    val missingTime: String,
    val missingPoint: String,
    val userSeq: Int?,
    val imgUrl1: String?,
    val imgUrl2: String?,
    val imgUrl3: String?
)

