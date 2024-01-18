package com.memo.post.bo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.memo.common.FileManagerService;
import com.memo.post.domain.Post;
import com.memo.post.mapper.PostMapper;

@Service
public class PostBO {
	@Autowired
	private PostMapper postMapper;
	
	@Autowired
	private FileManagerService fileManagerService;
	
	// input: userId   output: List<Post>
	public List<Post> getPostListById(int userId){
		
		return postMapper.selectPostListById(userId);
	}
	// input: 글쓴이번호, 제목, 내용
	public void addPost(int userId, String userLoginId, String subject, String content, 
			MultipartFile file) {
		
		String imagePath = null;
		//업로드 할 이미지가 있을 때 업로드
		if(file != null) {
			imagePath = fileManagerService.saveFile(userLoginId, file);
		}
	
		 postMapper.insertPost(userId, subject, content, imagePath);
	}
	public Post getPostByPostIdUserId(int postId, int userId) {
		return postMapper.selectPostByPostIdUserId(postId, userId);
	}
	
}
