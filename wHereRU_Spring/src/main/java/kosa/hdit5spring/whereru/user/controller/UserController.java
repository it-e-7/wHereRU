package kosa.hdit5spring.whereru.user.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

import kosa.hdit5spring.whereru.main.vo.MissingBoardVo;
import kosa.hdit5spring.whereru.user.service.UserService;
import kosa.hdit5spring.whereru.user.vo.UserVO;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("user")
@SessionAttributes(value = { "currUser" })
@RequiredArgsConstructor
public class UserController {

	private final UserService service;

	// �α���
    @PostMapping("login")
    public ResponseEntity<UserVO> login(@RequestBody UserVO userVO, HttpSession session) {
        UserVO vo = service.login(userVO);

        System.out.println(vo);
        if (vo == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else {
            session.setAttribute("currUser", vo);
            return ResponseEntity.ok(vo);
        }
    }
	

	// ȸ������
	@PostMapping("register")
	public ResponseEntity<String> register(@RequestBody UserVO userVO) {
		System.out.println("register:" + userVO);
	    try {
	        boolean result = service.registerUser(userVO);
	        if (result) {
	            return new ResponseEntity<String>(HttpStatus.OK);
	        } else {
	            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
	        }
	    } catch (IllegalArgumentException e) {
	        return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
	    }
	}
}
