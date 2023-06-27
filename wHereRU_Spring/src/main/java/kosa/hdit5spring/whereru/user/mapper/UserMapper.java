package kosa.hdit5spring.whereru.user.mapper;

import org.apache.ibatis.annotations.Mapper;

import kosa.hdit5spring.whereru.user.vo.UserVO;

@Mapper
public interface UserMapper {

	UserVO selectUserByUserVO(UserVO uservo);

	String selectUserIdByUserId(String userId);

	int insertUserByUserVO(UserVO userVO);
}
