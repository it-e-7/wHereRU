package kosa.hdit5spring.whereru.main.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import kosa.hdit5spring.whereru.main.service.MissingBoardService;
import kosa.hdit5spring.whereru.main.vo.DetailMissingBoardVo;
import kosa.hdit5spring.whereru.main.vo.MissingBoardVo;
import kosa.hdit5spring.whereru.notice.service.NoticeService;
import kosa.hdit5spring.whereru.notice.service.SetNoticeService;
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
	@Autowired
	SetNoticeService setNoticeService;
	
	@RequestMapping("main")
	public ResponseEntity<List<MissingBoardVo>> mainPage() {
		List<MissingBoardVo> list = missingBoardService.getTotalList(); 
		//@SessionAttribute UserVO currUser
		return ResponseEntity.ok(list);
	}

	@RequestMapping("writemissingboard")
	public String writeMissingBoard(@RequestBody MissingBoardVo missingBoardVo) {
		
		missingBoardService.writeMissingBoard(missingBoardVo);
		//게시글정상처리되고 나면 알림 전송
		noticeService.sendingToAll(missingBoardVo.getUserSeq());
		setNoticeService.setNoticeByBoard(missingBoardVo);
		
		return "success";
	}

	@PostMapping("detail")
	public DetailMissingBoardVo getMissingBoardDetail(@RequestBody int missingBoardSeq, @SessionAttribute(required = false) UserVO currUser) {

	    String userSeq = null;
	    if (currUser != null) {
	        userSeq = currUser.getUserSeq();
	    }

	    DetailMissingBoardVo detail = missingBoardService.getMissingBoardDetail(missingBoardSeq, userSeq);

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
	    
	@GetMapping("openchat/{missingBoardSeq}")
	public DetailMissingBoardVo openChatActivity(@PathVariable int missingBoardSeq) {
		
        return missingBoardService.openChatActivity(missingBoardSeq);
    }
	
	@GetMapping("summary")
	public MissingBoardVo getMissingBoardSummary(@RequestParam("roomSeq") int roomSeq, @RequestParam("missingSeq") int missingSeq) {
		MissingBoardVo result = missingBoardService.getMissingBoardSummary(roomSeq, missingSeq);
		return result;
	}
	
}
