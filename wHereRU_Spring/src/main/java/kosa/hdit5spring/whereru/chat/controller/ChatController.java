package kosa.hdit5spring.whereru.chat.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import kosa.hdit5spring.whereru.chat.service.ChatService;
import kosa.hdit5spring.whereru.chat.vo.ChatListVO;
import kosa.hdit5spring.whereru.user.vo.UserVO;

@RestController
public class ChatController {
	
	@Autowired
	private ChatService service;

	@GetMapping("/chat")
	public String getChatController() {
		System.out.println("hi its chatController");
		return service.getChatList();
	}
	
	@GetMapping("/chat/list")
	public List<ChatListVO> getChatListController(@SessionAttribute UserVO currUser) {
		List<ChatListVO> result = service.getChatRoomListByUserId(currUser.getUserId());
		System.out.println(result);
		return result;
	}
}
