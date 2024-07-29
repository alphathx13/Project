package com.example.Project.service;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.example.Project.dao.FileDao;
import com.example.Project.vo.FileVo;

@Service
public class FileService {

	@Value("${file.dir}")
	private String fileDir;

	private FileDao fileDao;

	public FileService(FileDao fileDao) {
		this.fileDao = fileDao;
	}
	
	// 실제 파일 업로드
	public int saveFile(MultipartFile file, @RequestParam(defaultValue = "") String type) throws IOException {

		String orgName = file.getOriginalFilename();
		String uuid = UUID.randomUUID().toString();
		String extension = orgName.substring(orgName.lastIndexOf("."));
		String savedName = uuid + extension;
		String savedPath;
		String table;
		
		if (type.equals("member")) {
			savedPath = fileDir + "\\memberImg\\" + savedName;
			table = "memberImg";
		} else if (type.equals("image")) {
			savedPath = fileDir + "\\imgUpload\\" + savedName;
			table = "imgUpload";
		} else {
			savedPath = fileDir + "\\fileUpload\\" + savedName;
			table = "fileUpload";
		}
		
		if (table.equals("memberImg")) {
			fileDao.memberImgUpload(orgName, savedName, savedPath);
		} else if (table.equals("imgUpload")) {
			fileDao.imgUpload(orgName, savedName, savedPath);
		} else {
			fileDao.fileUpload(orgName, savedName, savedPath);
		}
		
		file.transferTo(new File(savedPath));
		
		if (table.equals("memberImg")) {
			return fileDao.memberImgLast();
		} else if (table.equals("imgUpload")) {
			return fileDao.imgLast();
		} 
			
		return fileDao.fileLast();
	}
	
	// 번호로 이미지 파일 가져오기
	public FileVo getImageFileById(int id) {
		return fileDao.getImageFileById(id);
	}

	// 번호로 첨부파일 가져오기
	public String getFilePathById(int id) {
		return fileDao.getFilePathById(id);
	}
	
	// 번호로 멤버이미지 삭제
	public void memberImgDelete(int id) {
		fileDao.memberImgDelete(id);
	}

	// DB와 서버에서 파일 삭제
	public void fileAndFileDBDelete(String[] list, String type) {
		
		// int[] 배열로 변환
		int[] numbers = Arrays.stream(list).mapToInt(Integer::parseInt).toArray();
		
		System.out.println("type : " + type);
		for (int k : numbers) {
			System.out.println(k);
		}
		
		// 실제 DB에 저장되어있는 파일 저장위치 가져오기
        String[] pathArr = new String[numbers.length];
        int i = 0;
        for (int id : numbers) {
        	if (type.equals("image")) {
        		pathArr[i] = fileDao.getImagePathById(id);
        	} else {
        		pathArr[i] = fileDao.getFilePathById(id);
        	}
        	i++;
        }
        
        // DB에서 파일 정보 삭제
        for (int id : numbers) {
        	if (type.equals("image")) {
        		fileDao.imageDBDelete(id);
        	} else {
        		fileDao.fileDBDelete(id);
        	}
        }
        
        // 실제 파일서버에서 파일 삭제
        for (String filePath : pathArr) {
        	File file = new File(filePath);
            file.delete();
        }
	}

	public FileVo getFileById(int id) {
		return fileDao.getFileById(id);
	}
	
}