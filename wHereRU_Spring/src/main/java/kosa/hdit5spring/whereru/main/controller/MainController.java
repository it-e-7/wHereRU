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
import kosa.hdit5spring.whereru.main.vo.DetailMissingBoardVo;
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
		List<String> tokenList = noticeService.getTokenList(missingBoardVo.getUserSeq());
		noticeService.requestToFCM(tokenList);
		
		return "success";
	}

	@PostMapping("detail")
	public DetailMissingBoardVo getMissingBoardDetail(@RequestBody int missingBoardSeq, @SessionAttribute UserVO currUser) {

	    System.out.println("MissingBoardSeq: " + missingBoardSeq);  
		DetailMissingBoardVo detail = missingBoardService.getMissingBoardDetail(missingBoardSeq, currUser.getUserSeq());
		System.out.println("MissingBoardService" + detail);
 
		return detail;
	}
	
	@PostMapping("deletemissingboard")
	public ResponseEntity deleteMissingBoardDetail(@RequestBody int missingBoardSeq, @SessionAttribute UserVO currUser) {

		missingBoardService.deleteMissingBoard(missingBoardSeq, currUser.getUserSeq());

		System.out.println("delete: " + missingBoardSeq);
		return ResponseEntity.ok().build();
	}

	@PostMapping("updatemissingboard")
   public ResponseEntity<DetailMissingBoardVo> updateMissingBoardDetail(@RequestBody MissingBoardVo missingBoardVo, @SessionAttribute UserVO currUser) {

		missingBoardService.updateMissingBoard(missingBoardVo);
		
	    DetailMissingBoardVo detail = missingBoardService.getMissingBoardDetail(missingBoardVo.getMissingSeq(), currUser.getUserSeq());

		return ResponseEntity.ok(detail);
	}
	    
}
