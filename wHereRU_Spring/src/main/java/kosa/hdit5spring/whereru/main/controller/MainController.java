package kosa.hdit5spring.whereru.main.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kosa.hdit5spring.whereru.main.service.MissingBoardService;
import kosa.hdit5spring.whereru.main.vo.MissingBoardVo;

@RestController
public class MainController {

	
	@Autowired
	MissingBoardService missingBoardService;
	
	@RequestMapping("writemissingboard")
	public String writeMissingBoard(@RequestBody MissingBoardVo missingBoardVo ) {
		missingBoardService.writeMissingBoard(missingBoardVo);

		
		return "데이터 전송 성공";
	}
}
