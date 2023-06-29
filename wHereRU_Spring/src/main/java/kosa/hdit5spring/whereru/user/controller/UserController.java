package kosa.hdit5spring.whereru.user.controller;

import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

import kosa.hdit5spring.whereru.user.service.UserService;
import kosa.hdit5spring.whereru.user.vo.UserVO;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("user")
@SessionAttributes(value = { "currUser" })
@RequiredArgsConstructor
public class UserController {

	private final UserService service;

	// 로그인
    @PostMapping("login")
    public ResponseEntity<String> login(@RequestBody UserVO userVO, HttpSession session) {
        UserVO vo = service.login(userVO);
        System.out.println(userVO);
        System.out.println(vo);
        if (vo == null) {
            return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
        } else {
            session.setAttribute("currUser", vo);
            return new ResponseEntity<String>(HttpStatus.OK);
        }
    }
	

	// 회원가입
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
	
	// 아이디 중복 확인
	@GetMapping("checkuseridExist")
	public boolean checkUserIdExist(@RequestBody String userId) {
		
        return service.isUserIdExist(userId);
	}
}
