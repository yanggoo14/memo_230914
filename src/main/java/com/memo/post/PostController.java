package com.memo.post;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.memo.post.bo.PostBO;
import com.memo.post.domain.Post;

import jakarta.servlet.http.HttpSession;

@RequestMapping("/post")
@Controller
public class PostController {
	@Autowired
	private PostBO postBO;
	
	@GetMapping("/post-list-view")
	public String postListView(Model model, HttpSession session) {
		//로그인 여부 조회
		Integer userId = (Integer)session.getAttribute("userId");  //object타입 int나 null이될기떄문
		if(userId == null) {
			// 비로그인이면 로그인 페이지로 이동
			return "redirect:/user/sign-in-view";
		}
		List<Post> postList = postBO.getPostListById(userId);
		model.addAttribute("postList", postList);
		// DB 글 목록 조회	
		model.addAttribute("viewName","post/postList");
		return "template/layout";	
	}
	/**
	 * 글쓰기 화면
	 * @param model
	 * @return
	 */
	@GetMapping("/post-create-view")
	public String postCreateView(Model model) {
		model.addAttribute("viewName","post/postCreate");
		return "template/layout";
				
	}
	
}
