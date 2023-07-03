package kosa.hdit5spring.whereru.chat.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kosa.hdit5spring.whereru.chat.vo.ChatListVO;
import kosa.hdit5spring.whereru.chat.vo.ChatVO;

@Mapper
public interface ChatMapper {

	public int insertChat(ChatVO chatVO);
	public List<ChatVO> selectAllChat(int roomSeq);
	public List<ChatListVO> selectAllChatRoom(String userId);
	public void updateChatChecked(@Param("roomSeq") int roomSeq, @Param("userId") String userId);
}
