package kosa.hdit5spring.whereru.main.service;

import java.util.List;

import kosa.hdit5spring.whereru.main.vo.MissingBoardVo;

public interface MissingBoardService {
	MissingBoardVo getMissingBoardDetail(int missingSeq, String userSeq);
	void writeMissingBoard(MissingBoardVo missingBoardVo);
	List<MissingBoardVo> getTotalList();
   
}