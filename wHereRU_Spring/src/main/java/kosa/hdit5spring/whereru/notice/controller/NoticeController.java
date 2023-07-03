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

	}
	
}
