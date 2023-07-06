package kosa.hdit5spring.whereru.notice.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kosa.hdit5spring.whereru.chat.vo.ChatVO;
import kosa.hdit5spring.whereru.main.vo.MissingBoardVo;
import kosa.hdit5spring.whereru.notice.vo.LocationVO;
import kosa.hdit5spring.whereru.notice.vo.NoticeVO;

@Mapper
public interface NoticeMapper {

//	List<String> getTokenList(int userSeq);
	String getToken(String userId);
	List<NoticeVO> getNoticeList(String userToken);
	void setNoticeByChat(ChatVO chatvo);
	void setNoticeByBoard(MissingBoardVo msbvo);
	int setLocation(LocationVO locvo);
	List<LocationVO> getTargetLoc(String token);
	LocationVO getMyLoc(int userSeq);
}
