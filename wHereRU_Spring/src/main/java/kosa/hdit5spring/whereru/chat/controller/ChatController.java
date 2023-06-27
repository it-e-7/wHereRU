package kosa.hdit5spring.whereru.chat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import kosa.hdit5spring.whereru.chat.service.ChatService;

@RestController
public class ChatController {
	
	@Autowired
	private ChatService service;

	@GetMapping("/chat")
	public String getChatController() {
		System.out.println("hi its chatController");
		return service.getChatList();
	}
}
