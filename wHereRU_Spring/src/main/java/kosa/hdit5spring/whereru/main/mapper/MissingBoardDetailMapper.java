package kosa.hdit5spring.whereru.main.mapper;

import org.apache.ibatis.annotations.Mapper;

import kosa.hdit5spring.whereru.main.vo.MissingBoardDetailVO;

@Mapper
public interface MissingBoardDetailMapper {
	
	MissingBoardDetailVO selectMissingBoardDetail(int missingSeq);
}