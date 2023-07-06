package kosa.hdit5spring.whereru.notice.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kosa.hdit5spring.whereru.chat.vo.ChatVO;
import kosa.hdit5spring.whereru.main.vo.MissingBoardVo;
import kosa.hdit5spring.whereru.notice.vo.LocationVO;
import kosa.hdit5spring.whereru.notice.vo.NoticeVO;

@Mapper
public interface NoticeMapper {

	String getToken(String userId);
	List<NoticeVO> getNoticeServiceLogin(String userToken);
	List<NoticeVO> getNoticeServiceLogout(String userToken);
	void setNoticeByChat(ChatVO chatvo);
	void setNoticeByBoard(MissingBoardVo msbvo);
	int setLocation(LocationVO locvo);
	List<LocationVO> getTargetLoc(String token);
	LocationVO getMyLoc(int userSeq);
}
