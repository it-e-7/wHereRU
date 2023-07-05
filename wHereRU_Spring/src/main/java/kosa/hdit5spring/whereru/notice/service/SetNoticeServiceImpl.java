package kosa.hdit5spring.whereru.notice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kosa.hdit5spring.whereru.chat.vo.ChatVO;
import kosa.hdit5spring.whereru.main.vo.MissingBoardVo;
import kosa.hdit5spring.whereru.notice.mapper.NoticeMapper;

@Service
public class SetNoticeServiceImpl implements SetNoticeService {

	@Autowired
	NoticeMapper mapper;
	
	@Override
	public void setNoticeByChat(ChatVO chatvo) {
		mapper.setNoticeByChat(chatvo);
	}
	@Override
	public void setNoticeByBoard(MissingBoardVo msbvo) {
		mapper.setNoticeByBoard(msbvo);
	}
}
