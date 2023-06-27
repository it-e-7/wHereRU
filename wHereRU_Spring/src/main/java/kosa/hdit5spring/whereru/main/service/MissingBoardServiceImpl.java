package kosa.hdit5spring.whereru.main.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kosa.hdit5spring.whereru.main.mapper.MissingBoardMapper;
import kosa.hdit5spring.whereru.main.vo.MissingBoardVo;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MissingBoardServiceImpl implements MissingBoardService{

	@Autowired
	MissingBoardMapper missingBoardMapper;
	
	@Override
	public void writeMissingBoard(MissingBoardVo missingBoardVo) {		
		missingBoardMapper.writeMissingBoard(missingBoardVo);
		
	}
	@Override
	public MissingBoardVo getMissingBoardDetail(int missingSeq, String userSeq) {
		MissingBoardVo detail = missingBoardMapper.selectMissingBoardDetail(missingSeq);
		
		if(detail != null && userSeq != null) {
			detail.setOwner(userSeq.equals(detail.getUserSeq()));
		}
		
		
		return detail;
	}
	@Override
	public List<MissingBoardVo> getTotalList() {
		List<MissingBoardVo> list = missingBoardMapper.getTotalList();
		return list;
	}

   
}