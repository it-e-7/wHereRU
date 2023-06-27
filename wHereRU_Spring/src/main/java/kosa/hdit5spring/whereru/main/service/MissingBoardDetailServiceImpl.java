package kosa.hdit5spring.whereru.main.service;

import org.springframework.stereotype.Service;

import kosa.hdit5spring.whereru.main.mapper.MissingBoardDetailMapper;
import kosa.hdit5spring.whereru.main.vo.MissingBoardDetailVO;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MissingBoardDetailServiceImpl implements MissingBoardDetailService {
	
	private final MissingBoardDetailMapper mapper;

	@Override
	public MissingBoardDetailVO getMissingBoardDetail(int missingSeq, String userSeq) {
		MissingBoardDetailVO detail = mapper.selectMissingBoardDetail(missingSeq);
		
		if(detail != null && userSeq != null) {
			detail.setOwner(userSeq.equals(detail.getUserSeq()));
		}
		
		
		return detail;
	}
}