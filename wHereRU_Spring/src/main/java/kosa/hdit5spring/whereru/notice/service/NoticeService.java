package kosa.hdit5spring.whereru.notice.service;

import java.util.List;

public interface NoticeService {

	void requestToFCM(List<String> tokenList);

	List<String> getTokenList(int userSeq);
}
