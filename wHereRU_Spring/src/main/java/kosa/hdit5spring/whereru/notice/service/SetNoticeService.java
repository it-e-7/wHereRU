package kosa.hdit5spring.whereru.notice.service;

import kosa.hdit5spring.whereru.chat.vo.ChatVO;
import kosa.hdit5spring.whereru.main.vo.MissingBoardVo;

public interface SetNoticeService {

	void setNoticeByChat(ChatVO chatvo);
	void setNoticeByBoard(MissingBoardVo msbvo);
}
