package kosa.hdit5spring.whereru.main.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kosa.hdit5spring.whereru.main.mapper.MissingBoardMapper;
import kosa.hdit5spring.whereru.main.vo.DetailMissingBoardVo;
import kosa.hdit5spring.whereru.main.vo.MissingBoardVo;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MissingBoardServiceImpl implements MissingBoardService {

	@Autowired
	MissingBoardMapper missingBoardMapper;

	@Override
	public void writeMissingBoard(MissingBoardVo missingBoardVo) {
		missingBoardMapper.writeMissingBoard(missingBoardVo);

	}

	@Override
	public DetailMissingBoardVo getMissingBoardDetail(int missingSeq, String userSeq) {

		System.out.println(missingSeq + " " + userSeq);
		DetailMissingBoardVo detail = missingBoardMapper.selectMissingBoardDetail(missingSeq);
		if (detail != null && userSeq != null) {
			detail.setOwner(userSeq.equals(String.valueOf(detail.getUserSeq())));
		}

		return detail;
	}

	@Override
	public List<MissingBoardVo> getTotalList() {
		List<MissingBoardVo> list = missingBoardMapper.getTotalList();
		return list;
	}

	@Override
	public void deleteMissingBoard(int missingSeq, String userSeq) {

		DetailMissingBoardVo detail = missingBoardMapper.selectMissingBoardDetail(missingSeq);

//	   TODO: if(detail != null && userSeq != null && userSeq.equals(detail.getUserSeq())) {
		missingBoardMapper.deleteMissingBoard(missingSeq);
	}

	@Override
	public void updateMissingBoard(MissingBoardVo missingBoardVo) {

		DetailMissingBoardVo original = missingBoardMapper.selectMissingBoardDetail(missingBoardVo.getMissingSeq());

//	    TODO: if (original != null && userSeq != null && userSeq.equals(original.getUserSeq())) {
		missingBoardMapper.updateMissingBoard(missingBoardVo);

	}

	@Override
	public DetailMissingBoardVo openChatActivity(int missingSeq) {

		return missingBoardMapper.openChatActivity(missingSeq);
	}
	
	@Override
	public MissingBoardVo getMissingBoardSummary(int roomSeq, int missingSeq) {
		if(roomSeq == -1) {
			return missingBoardMapper.selectMissingBoardSummaryByMissingSeq(missingSeq);
		} else {
			return missingBoardMapper.selectMissingBoardSummaryByRoomSeq(roomSeq);
		}
	}
}