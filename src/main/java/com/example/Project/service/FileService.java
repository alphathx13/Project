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
	
	public int saveFile(MultipartFile file, @RequestParam(defaultValue = "") String type) throws IOException {
		String orgName = file.getOriginalFilename();
		String uuid = UUID.randomUUID().toString();
		String extension = orgName.substring(orgName.lastIndexOf("."));
		String savedName = uuid + extension;
		String savedPath;
		
		if (type.equals("member")) {
			savedPath = fileDir + "\\memberImg\\" + savedName;
		} else if (type.equals("images")) {
			savedPath = fileDir + "\\images\\" + savedName;
		} else {
			savedPath = fileDir + "\\" + savedName;
		}
		
		fileDao.insertFile(orgName, savedName, savedPath);
		
		file.transferTo(new File(savedPath));
		
		return fileDao.lastFileId();
	}
	
	public FileVo getFileById(int id) {
		return fileDao.getFileById(id);
	}

	public void imageArticleId(int articleId, int image) {
		fileDao.imageArticleId(articleId, image);
	}

	public List<String> getImagePath(int id) {
		return fileDao.getImagePath(id);
	}

	public String getFilePathById(int id) {
		return fileDao.getFilePathById(id);
	}

	public void fileAndFileDBDelete(String[] fileListArr) {

		// int[] 배열로 변환
		int[] numbers = Arrays.stream(fileListArr).mapToInt(Integer::parseInt).toArray();

        // 실제 DB에 저장되어있는 파일 저장위치 가져오기
        String[] filePathArr = new String[numbers.length];
        int i = 0;
        for (int fileId : numbers) {
        	filePathArr[i] = getFilePathById(fileId);
        	i++;
        }
        
        // DB에서 파일 정보 삭제
        for (int fileId : numbers) {
        	fileDao.fileDBDelete(fileId);
        }
        
        // 실제 파일서버에서 파일 삭제
        for (String filePath : filePathArr) {
        	File file = new File(filePath);
            file.delete();
        }
	}
	
}