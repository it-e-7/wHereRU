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
	
	//알림리스트 불러와서 출력하는 핸들러
	@PostMapping("getnoticelist")
	public List<NoticeVO> getNoticeList(@RequestBody String userToken){
		userToken = userToken.replaceAll("\"", "");
		List<NoticeVO> noticeList = new ArrayList<NoticeVO>();
		noticeList = getNoticeListService.getNoticeService(userToken);
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
