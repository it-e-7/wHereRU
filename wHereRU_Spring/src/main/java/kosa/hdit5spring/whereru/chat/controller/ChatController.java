package kosa.hdit5spring.whereru.chat.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import kosa.hdit5spring.whereru.chat.service.ChatService;
import kosa.hdit5spring.whereru.chat.vo.ChatListVO;
import kosa.hdit5spring.whereru.user.vo.UserVO;

@RestController("chat")
public class ChatController {
	
	@Autowired
	private ChatService service;

	@GetMapping
	public String getChatController() {
		System.out.println("hi its chatController");
		return service.getChatList();
	}
	
	@GetMapping("list")
	public List<ChatListVO> getChatListController(@SessionAttribute UserVO currUser) {
		
		return service.getChatListByUserId(currUser.getUserId());
	}
}
