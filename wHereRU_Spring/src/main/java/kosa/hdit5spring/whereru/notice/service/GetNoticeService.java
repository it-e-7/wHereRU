package kosa.hdit5spring.whereru.notice.service;

import java.util.List;

import kosa.hdit5spring.whereru.notice.vo.NoticeVO;

public interface GetNoticeService {

	List<NoticeVO> getNoticeServiceLogin(String userToken);
	List<NoticeVO> getNoticeServiceLogout(String userToken);
}
