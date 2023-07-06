package kosa.hdit5spring.whereru.notice.service;

import java.util.List;

import kosa.hdit5spring.whereru.notice.vo.LocationVO;

public interface LocationService {
	boolean setLocation(LocationVO locvo);
	List<LocationVO> getTargetLoc(String token);
	LocationVO getMyLoc(int userSeq);
	List<String> getTokenList(int userSeq);
}
