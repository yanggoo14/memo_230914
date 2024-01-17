package com.memo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.memo.common.FileManagerService;

@Configuration //설정을 위한 spring bean
public class WebMvcConfig implements WebMvcConfigurer{
	
	// 웹 이미지 path와 서버에 업로드 된 실제 이미지와 매핑 설정
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry
		.addResourceHandler("/images/**")   // 웹에서 image로 시작하는 주소로 들어갈때 사용
		.addResourceLocations("file:///" + FileManagerService.FILE_UPLOAD_PATH);   // 실제 파일 위치 
		// 윈도우는   /// 3개 넣어주기
		
	}
}
