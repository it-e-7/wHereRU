package kosa.hdit5spring.whereru.notice.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kosa.hdit5spring.whereru.chat.vo.ChatVO;

@Mapper
public interface NoticeMapper {

	List<String> getTokenList(int userSeq);
	String getToken(String userId);
}
