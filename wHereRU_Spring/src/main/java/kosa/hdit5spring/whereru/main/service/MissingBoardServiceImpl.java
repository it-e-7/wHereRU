package kosa.hdit5spring.whereru.main.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kosa.hdit5spring.whereru.main.mapper.MissingBoardMapper;
import kosa.hdit5spring.whereru.main.vo.MissingBoardVo;

@Service
public class MissingBoardServiceImpl implements MissingBoardService{

	@Autowired
	MissingBoardMapper missingBoardMapper;
	
	@Override
	public void writeMissingBoard(MissingBoardVo missingBoardVo) {		
		missingBoardMapper.writeMissingBoard(missingBoardVo);
		
	}

	
}
