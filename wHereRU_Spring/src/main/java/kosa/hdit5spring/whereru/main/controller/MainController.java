package kosa.hdit5spring.whereru.main.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import kosa.hdit5spring.whereru.main.service.MissingBoardService;
import kosa.hdit5spring.whereru.main.vo.MissingBoardVo;
import kosa.hdit5spring.whereru.notice.service.NoticeService;
import kosa.hdit5spring.whereru.user.vo.UserVO;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("main")
public class MainController {
	
	@Autowired
	MissingBoardService missingBoardService;
	@Autowired
	NoticeService noticeService;
	
	@RequestMapping("main")
	public ResponseEntity<List<MissingBoardVo>> mainPage(@SessionAttribute UserVO currUser) {
		List<MissingBoardVo> list = missingBoardService.getTotalList(); 
		
		return ResponseEntity.ok(list);
	}

	@RequestMapping("writemissingboard")
	public String writeMissingBoard(@RequestBody MissingBoardVo missingBoardVo) {
		
		missingBoardService.writeMissingBoard(missingBoardVo);
		//게시글정상처리되고 나면 알림 전송
		//작동 안함 오류
		noticeService.sendingToAll(missingBoardVo.getUserSeq());
		
		return "success";
	}

	@PostMapping("detail")
	public MissingBoardVo getMissingBoardDetail(@RequestBody int missingBoardSeq, @SessionAttribute UserVO currUser) {

	    System.out.println("MissingBoardSeq: " + missingBoardSeq);  
		MissingBoardVo detail = missingBoardService.getMissingBoardDetail(missingBoardSeq, currUser.getUserSeq());
		System.out.println("MissingBoardService" + detail);
 
		return detail;
	}
	
	@PostMapping("deletemissingboard")
	public ResponseEntity deleteMissingBoardDetail(@RequestBody Map<String, Object> map, @SessionAttribute UserVO currUser) {

		int missingBoardSeq = Integer.parseInt(map.get("missingBoardSeq").toString());
		missingBoardService.deleteMissingBoard(missingBoardSeq, currUser.getUserSeq());

		System.out.println("delete: " + missingBoardSeq);
		return ResponseEntity.ok().build();
	}

	@PostMapping("updatemissingboard")
   public ResponseEntity<MissingBoardVo> updateMissingBoardDetail(@RequestBody MissingBoardVo missingBoardVo, @SessionAttribute UserVO currUser) {

		missingBoardService.updateMissingBoard(missingBoardVo);
		
	    MissingBoardVo detail = missingBoardService.getMissingBoardDetail(missingBoardVo.getMissingSeq(), currUser.getUserSeq());

		return ResponseEntity.ok(detail);
	}
	    
}
