package com.memo.common;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component // Spring bean
public class FileManagerService {

	// 실제 업로드가 된 이미작 저장될 경로(서버)
	
	//학원용 경로
	public static final String FILE_UPLOAD_PATH = "C:\\yangjunwoo\\6_Spring project\\MEMO\\MEMO_workspace\\images/";
	
	// input : file 원본, userLoginId(폴더명)    output: 이미지 경로
	public String saveFile(String loginId, MultipartFile file) {
		// 폴더(디렉토리) 생성
		// 예): aaaa_1281235315/sun.png  폴더명 뒤에 시간까지 붙여준다.
		String directoryName = loginId + "_" + System.currentTimeMillis();
		String filePath = FILE_UPLOAD_PATH + directoryName;
		
		File directory = new File(filePath);
		if(directory.mkdir() == false) {
			// 폴더 생성 실패시 이미지 경로 null로 리턴
			return null;
		}
		
		//파일 업로드: byte 단위로 업로드
		try {
			byte[] bytes = file.getBytes();   // try/catch로 에러 해결
			//★★★★★★★★★ 한글 이름 이미지는 지금 올릴 수 없으므로 나중에 영문자로 바꾸는 코드를 사용하여 올리기
			Path path = Paths.get(filePath + "/" + file.getOriginalFilename());
			Files.write(path, bytes);   // 실제 파일 업로드
		} catch (IOException e) {
			e.printStackTrace();
			return null; /// 이미지 업로드 실패시 null로 리턴
		}
		
		//C:\yangjunwoo\6_Spring project\MEMO\MEMO_workspace\images\aaaa_1705482936930
		// 파일 업로드가 성공했으면 웹 이미지 url path를 리턴
		// 주소는 이렇게 될 것이다.(예언)
		// \images\aaaa_1705482936930\sun.jpg 
		return "/images/" + directoryName + "/" + file.getOriginalFilename();
	}
}
