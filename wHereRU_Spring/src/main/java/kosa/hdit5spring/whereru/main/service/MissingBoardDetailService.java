package kosa.hdit5spring.whereru.main.service;

import kosa.hdit5spring.whereru.main.vo.MissingBoardDetailVO;

public interface MissingBoardDetailService {

	MissingBoardDetailVO getMissingBoardDetail(int missingSeq, String userSeq);
}
