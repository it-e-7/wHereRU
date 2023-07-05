package kosa.hdit5spring.whereru.user.service;

import kosa.hdit5spring.whereru.user.vo.UserVO;

public interface UserService {

	UserVO login(UserVO vo);

	boolean registerUser(UserVO userVO);
	
	boolean isUserIdExist(String userId);
}
