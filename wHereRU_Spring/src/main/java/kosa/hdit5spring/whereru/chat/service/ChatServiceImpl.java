package kosa.hdit5spring.whereru.chat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kosa.hdit5spring.whereru.chat.mapper.ChatMapper;

@Service
public class ChatServiceImpl implements ChatService {
	
	@Autowired
	ChatMapper mapper;

	@Override
	public String getChatList() {
		mapper.selectAllChatList();
		return "hi this is service";
	}
}
