package com.memo.common;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@Slf4j
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
		
		//C:\yangjunwoo\6_Spring project\MEMO\MEMO_workspace\images\aaaa_1705482936930C:\\yangjunwoo\\6_Spring project\\MEMO\\MEMO_workspace\\images/
		// 파일 업로드가 성공했으면 웹 이미지 url path를 리턴
		// 주소는 이렇게 될 것이다.(예언)
		// \images\aaaa_1705482936930\sun.jpg 
		return "/images/" + directoryName + "/" + file.getOriginalFilename();
	}
	
	// input: imagePath    output: x
	public void deleteFile(String imagePath) { //  /images/aaaa_1705570788506/bird-8270722_640.jpg
	// 주소에 겹치는 /images/ 지운다. 
		Path path = Paths.get(FILE_UPLOAD_PATH + imagePath.replace("/images/", ""));
		
		// 삭제할 이미지가 존재하는가??
		if(Files.exists(path)) {
			// 이미지 삭제
			try {
				Files.delete(path);
			} catch (IOException e) {
				log.info("[파일매니저 삭제] 이미지 삭제 실패. path:{}", path.toString());
				return;
			}
			// 폴더 삭제
			path = path.getParent();
			if(Files.exists(path)) {
				try {
					Files.delete(path);
				} catch (IOException e) {
				log.info("[파일매니저 삭제] 폴더 삭제 실패. path:{}", path.toString());
				}
			}
			
		}
	}
}
