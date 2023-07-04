package kosa.hdit5.whereru.util.retrofit.main.vo

data class NoticeVO(

    val noticeSeq: Int? = null,
    val notiSender: String? = null,
    val notiReceiver: String? = null,
    val notiMessage: String? = null,
    var notiTime: String? = null,
    var notiType: String? = null,
    var msgType: String? = null
)
