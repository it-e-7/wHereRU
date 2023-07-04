package kosa.hdit5spring.whereru.chat.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import kosa.hdit5spring.whereru.chat.service.ChatService;
import kosa.hdit5spring.whereru.chat.vo.ChatListVO;
import kosa.hdit5spring.whereru.chat.vo.ChatVO;
import kosa.hdit5spring.whereru.user.vo.UserVO;

@RestController
public class ChatController {
	
	@Autowired
	private ChatService service;

	@GetMapping("/chat")
	public List<ChatVO> getChatController(@RequestParam("roomSeq") int roomSeq, @SessionAttribute UserVO currUser) {
		return service.getChatList(roomSeq, currUser.getUserId());
	}
	
	@GetMapping("/chat/user")
	public List<ChatVO> getChatByReceiverController(@RequestParam("receiverSeq") int receiverSeq, @SessionAttribute UserVO currUser) {
		return service.getChatRoomListByReceiverSeq(receiverSeq, currUser.getUserId());
	}
	
	@GetMapping("/chat/list")
	public List<ChatListVO> getChatListController(@SessionAttribute UserVO currUser) {
		List<ChatListVO> result = service.getChatRoomListByUserId(currUser.getUserId());
		return result;
	}
}
