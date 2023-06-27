package kosa.hdit5spring.whereru.main.service;

import kosa.hdit5spring.whereru.main.vo.MissingBoardVo;

public interface MissingBoardService {
	
   MissingBoardVo getMissingBoardDetail(int missingSeq, String userSeq);
   void writeMissingBoard(MissingBoardVo missingBoardVo);
   void deleteMissingBoard(int missingSeq, String userSeq);
   void updateMissingBoard(MissingBoardVo missingBoardVo);
}