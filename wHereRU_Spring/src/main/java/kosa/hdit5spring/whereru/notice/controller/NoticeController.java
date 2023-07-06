package kosa.hdit5spring.whereru.notice.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kosa.hdit5spring.whereru.notice.service.GetNoticeService;
import kosa.hdit5spring.whereru.notice.service.LocationService;
import kosa.hdit5spring.whereru.notice.service.SetNoticeService;
import kosa.hdit5spring.whereru.notice.vo.LocationVO;
import kosa.hdit5spring.whereru.notice.vo.NoticeVO;

@RestController
@RequestMapping("notice")
public class NoticeController {
	
	@Autowired
	GetNoticeService getNoticeListService;
	@Autowired
	LocationService setLocationService;
	
	//로그인 유저의 알림센터 출력
	@PostMapping("getnoticelistLogin")
	public List<NoticeVO> getNoticeListLogin(@RequestBody String userToken){
		userToken = userToken.replaceAll("\"", "");
		List<NoticeVO> noticeList = new ArrayList<NoticeVO>();
		noticeList = getNoticeListService.getNoticeServiceLogin(userToken);
		System.out.println(noticeList);
		return noticeList;
	}
	
	//로그아웃 유저의 알림센터 출력
		@PostMapping("getnoticelistLogout")
		public List<NoticeVO> getNoticeListLogout(@RequestBody String userToken){
			userToken = userToken.replaceAll("\"", "");
			List<NoticeVO> noticeList = new ArrayList<NoticeVO>();
			noticeList = getNoticeListService.getNoticeServiceLogout(userToken);
			System.out.println(noticeList);
			return noticeList;
		}
	

	@PostMapping("sendLoc")
	public boolean sendLoc(@RequestBody LocationVO locvo) {
		System.out.println("------------------------------------------");
		System.out.println(locvo.getUserToken());
		System.out.println("위"+locvo.getLatitude()+"경"+locvo.getLongitude());
		setLocationService.setLocation(locvo);
		return true;
	}

}
