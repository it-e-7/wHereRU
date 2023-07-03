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
		return mapper.insertChat(chatVO);
	}

	@Override
	public List<ChatVO> getChatList(int roomSeq, String userId) {
		mapper.updateChatChecked(roomSeq, userId);
		return mapper.selectAllChat(roomSeq);
	}
	
	@Override
	public List<ChatListVO> getChatRoomListByUserId(String userId) {
		
		return mapper.selectAllChatRoom(userId);
	}
}
