package kosa.hdit5spring.whereru.notice.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kosa.hdit5spring.whereru.chat.vo.ChatVO;
import kosa.hdit5spring.whereru.notice.service.GetNoticeService;
import kosa.hdit5spring.whereru.notice.service.SetNoticeService;
import kosa.hdit5spring.whereru.notice.vo.NoticeVO;

@RestController
@RequestMapping("notice")
public class NoticeController {
	
	@Autowired
	GetNoticeService getNoticeListService;
	@Autowired
	SetNoticeService service;
	
	//알림리스트 불러와서 출력하는 핸들러
	@PostMapping("getnoticelist")
	public List<NoticeVO> getNoticeList(@RequestBody String userToken){
		userToken = userToken.replaceAll("\"", "");
		List<NoticeVO> noticeList = new ArrayList<NoticeVO>();
		noticeList = getNoticeListService.getNoticeService(userToken);
		System.out.println(noticeList);
		return noticeList;
	}

}
