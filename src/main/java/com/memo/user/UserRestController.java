package com.memo.user;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.memo.common.EncryptUtils;
import com.memo.user.bo.UserBO;
import com.memo.user.entity.UserEntity;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RequestMapping("/user")
@RestController
public class UserRestController {


	@Autowired
	private UserBO userBO;
	/**
	 * 아이디 중복확인 API
	 * @param loginId
	 * @return
	 */
	@RequestMapping("/is-duplicated-id")
	public Map<String, Object> isDuplicatedId(
			@RequestParam("loginId") String loginId) {
		
		// DB 조회 - select
		UserEntity user = userBO.getUserEntityByLoginId(loginId);
		
		Map<String, Object> result = new HashMap<>();
		if (user != null) { // 중복
			result.put("code", 200);
			result.put("is_duplicated_id", true);
		} else { // 중복 아님
			result.put("code", 200);
			result.put("is_duplicated_id", false);
		}
		return result;
	}
	
	/**
	 * 회원가입 API
	 * @param loginId
	 * @param password
	 * @param name
	 * @param email
	 * @return
	 */
	@PostMapping("/sign-up")
	public Map<String, Object> signUP(
			@RequestParam("loginId") String loginId,
			@RequestParam("password") String password,
			@RequestParam("name") String name,
			@RequestParam("email") String email){
		
		// md5 알고리즘 -> password hashing
		String hashedPassword = EncryptUtils.md5(password);
		//
		// db insert
		Integer userId = userBO.addUser(loginId, hashedPassword, name, email);
		
		//응답값
		Map<String, Object> result = new HashMap<>();
		if(userId != null) {
		result.put("code", 200);
		result.put("result", "성공");
		} else {
			result.put("code", 500);
			result.put("error_massage", "회원가입에 실패했습니다.");
		}
		return result;
		
	}
		@PostMapping("/sign-in")
		public Map<String, Object> signIn(
				@RequestParam("loginId") String loginId,
				@RequestParam("password") String password,
				HttpServletRequest request){
		// 비밀번호 mb5
			String hashedPassword = EncryptUtils.md5(password);
			
		// DB 조회(loginId, 해싱된 비밀번호) => userEntity
			UserEntity user = userBO.getUserEntityByLoginIdPassword(loginId, hashedPassword);
					
		// 응답	
			Map<String, Object> result = new HashMap<>();
			if(user != null) {   //성공
				//로그인 처리
				//로그인 정보를 세션에 담는다.(사용자 마다)
				HttpSession session = request.getSession();
				session.setAttribute("userId", user.getId());
				session.setAttribute("userLoginId", user.getLoginId());
				session.setAttribute("userName", user.getName());
				
				result.put("code", 200);
				result.put("result", "성공");
			}	else {  // 로그인 불가
				result.put("code", 300);
				result.put("error_massage", "존재하지 않는 사용자입니다.");
			}
			return result;
		}
}
