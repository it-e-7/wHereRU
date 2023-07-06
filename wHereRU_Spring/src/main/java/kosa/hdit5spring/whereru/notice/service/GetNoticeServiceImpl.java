package kosa.hdit5spring.whereru.notice.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import kosa.hdit5spring.whereru.notice.mapper.NoticeMapper;
import kosa.hdit5spring.whereru.notice.vo.NoticeVO;

@Service
public class GetNoticeServiceImpl implements GetNoticeService {
	
	@Autowired
	NoticeMapper mapper;

	@Override
	public List<NoticeVO> getNoticeServiceLogin(@RequestBody String userToken) {
		List<NoticeVO> noticeList = new ArrayList<NoticeVO>();
		noticeList = mapper.getNoticeServiceLogin(userToken);
		return noticeList;
	}
	
	@Override
	public List<NoticeVO> getNoticeServiceLogout(@RequestBody String userToken) {
		List<NoticeVO> noticeList = new ArrayList<NoticeVO>();
		noticeList = mapper.getNoticeServiceLogout(userToken);
		return noticeList;
	}
	
}
