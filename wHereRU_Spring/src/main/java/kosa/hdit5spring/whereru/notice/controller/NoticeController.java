package kosa.hdit5spring.whereru.notice.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import kosa.hdit5spring.whereru.notice.service.NoticeService;

@RestController
public class NoticeController {

	@Autowired
	NoticeService noticeService;
	
	@GetMapping("sendRequest")
	public void sendRequest() {
		System.out.println("µé¾î¿È");
		List<String> tokenList = new ArrayList<String>();
		tokenList.add("exmGr566QyK-xaWtBhRwwX:APA91bFvrFLt95pdmrrea_Qa_ZqH3P-AdW7R-NGspGKFdDPbvfQR60a3u0WwfA2GD_ZFEverBKDopMiUqbEwtaEur_XhtYBiY0DVz7Arn0sDGg8au8SSk-AI2zUhC00b5S3ohSu_ZPWu");
		noticeService.requestToFCM(tokenList);
	}
	
}
