package com.example.Project.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.example.Project.service.FileService;
import com.example.Project.vo.FileVo;

@Controller
public class FileController {

	private FileService fileService;
	
	public FileController(FileService fileService) {
		this.fileService = fileService;
	}
	
	@PostMapping("/user/file/upload")
	@ResponseBody
	public FileVo uploadFile(MultipartFile file) throws IOException {
		int id = fileService.saveFile(file, "image");
		return fileService.getFileById(id);
	}
	
	// 이미지 가져오기
	@GetMapping("/user/file/images/{id}")
	@ResponseBody
	public Resource images(@PathVariable("id") int id, Model model) throws IOException {
		
		String imagePath = fileService.getFileById(id).getSavedPath();
		
		return new UrlResource("file:" + imagePath);
	}
	
}