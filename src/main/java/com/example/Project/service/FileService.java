package com.example.Project.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
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

	public int saveFile(MultipartFile file) throws IOException {

		String orgName = file.getOriginalFilename();
		String uuid = UUID.randomUUID().toString();
		String extension = orgName.substring(orgName.lastIndexOf("."));
		String savedName = uuid + extension;
		String savedPath = fileDir + "\\" + savedName;
		fileDao.insertFile(orgName, savedName, savedPath);
		
		file.transferTo(new File(savedPath));
		
		return fileDao.lastFileId();
	}

}