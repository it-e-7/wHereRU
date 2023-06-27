package kosa.hdit5spring.whereru.main.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

import kosa.hdit5spring.whereru.main.service.MissingBoardDetailService;
import kosa.hdit5spring.whereru.main.vo.MissingBoardDetailVO;
import kosa.hdit5spring.whereru.user.vo.UserVO;
import lombok.RequiredArgsConstructor;



@RestController
@RequiredArgsConstructor
@RequestMapping("main")
@SessionAttributes("currUser")
public class MissingBoardDetailController {
	
	private final MissingBoardDetailService service;

	@PostMapping("/detail")
	public ResponseEntity<MissingBoardDetailVO> getMissingBoardDetail(@RequestBody Map<String, Object> map, UserVO currUser) {
		int missingBoardSeq = Integer.parseInt(map.get("missingBoardSeq").toString());
		MissingBoardDetailVO detail = service.getMissingBoardDetail(missingBoardSeq, currUser.getUserSeq());
		
		System.out.println("controller" + detail);
		return ResponseEntity.ok(detail);
	}
}