package kosa.hdit5spring.whereru.user.service;

import org.springframework.stereotype.Service;

import kosa.hdit5spring.whereru.user.mapper.UserMapper;
import kosa.hdit5spring.whereru.user.vo.UserVO;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserMapper mapper;

	@Override
	public UserVO login(UserVO userVO) {
		return mapper.selectUserByUserVO(userVO);
	}

	@Override
	public boolean registerUser(UserVO userVO) {
	    String userId = userVO.getUserId();
	    String existUserId = mapper.selectUserIdByUserId(userId);
		
	    if (existUserId != null) {
	        throw new IllegalArgumentException("존재하는 아이디 입니다.");
	    }

	    int cnt = mapper.insertUserByUserVO(userVO);
	    return cnt == 1;
	}
	
	@Override
	public boolean isUserIdExist(String userId) {
		
		String existUserId = mapper.selectUserIdByUserId(userId);
        return existUserId != null;
	}
}
