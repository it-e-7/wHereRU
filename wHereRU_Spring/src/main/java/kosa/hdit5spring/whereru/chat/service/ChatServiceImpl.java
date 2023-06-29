package kosa.hdit5spring.whereru.chat.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kosa.hdit5spring.whereru.chat.mapper.ChatMapper;
import kosa.hdit5spring.whereru.chat.vo.ChatListVO;
import kosa.hdit5spring.whereru.chat.vo.ChatVO;

@Service
public class ChatServiceImpl implements ChatService {
	
	@Autowired
	ChatMapper mapper;
	
	@Override
	public int addChat(ChatVO chatVO) {
		System.out.println(chatVO.getChatSender());
		return mapper.insertChat(chatVO);
	}

	@Override
	public String getChatList() {
		mapper.selectAllChat();
		return "hi this is service";
	}
	
	@Override
	public List<ChatListVO> getChatListByUserId(String userId) {
		
		return mapper.selectAllChatList(userId);
	}
}
