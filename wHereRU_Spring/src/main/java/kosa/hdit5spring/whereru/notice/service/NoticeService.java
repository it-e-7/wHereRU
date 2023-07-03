package kosa.hdit5spring.whereru.notice.service;

import java.util.List;

import kosa.hdit5spring.whereru.chat.vo.ChatVO;

public interface NoticeService {

	String getToken(String userId);
	void sendingToOne(String userId);
	List<String> getTokenList(int userSeq);
	void sendingToAll(int userSeq);
	
}
