package kosa.hdit5spring.whereru.chat.service;

import java.util.List;

import kosa.hdit5spring.whereru.chat.vo.ChatListVO;
import kosa.hdit5spring.whereru.chat.vo.ChatVO;

public interface ChatService {

	public int addChat(ChatVO chatVO);
	public List<ChatVO> getChatList(int roomSeq, String userId);
	public List<ChatListVO> getChatRoomListByUserId(String userId);
}
