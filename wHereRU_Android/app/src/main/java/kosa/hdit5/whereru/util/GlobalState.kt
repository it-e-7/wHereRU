package kosa.hdit5.whereru.util

object GlobalState {
    var isLogin: Boolean = false;
    var userId: String? = null;
    var userSeq: Int? = null;
    var userToken: String? = null;
    var userName:String?=null;
    var apiBaseUrl: String = "http://18.118.210.196:8080/";
}