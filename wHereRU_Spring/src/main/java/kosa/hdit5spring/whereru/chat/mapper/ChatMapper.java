package kosa.hdit5spring.whereru.chat.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kosa.hdit5spring.whereru.chat.vo.ChatListVO;
import kosa.hdit5spring.whereru.chat.vo.ChatVO;

@Mapper
public interface ChatMapper {

	public int insertChat(ChatVO chatVO);
	public int selectAllChat();
	public List<ChatListVO> selectAllChatRoom(String userId);
}
