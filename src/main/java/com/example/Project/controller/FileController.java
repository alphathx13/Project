package com.example.Project.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
	
	// 이미지 업로드
	@PostMapping("/user/file/imageUpload")
	@ResponseBody
	public List<FileVo> uploadFile(@RequestParam("file") MultipartFile[] files) throws IOException {
		List<FileVo> imageArray = new ArrayList<>();
		
		for (MultipartFile file : files)
			imageArray.add(fileService.getImageFileById(fileService.saveFile(file, "image")));
		
		return imageArray;
	}
	
	// 파일 업로드
	@PostMapping("/user/file/fileUpload")
	@ResponseBody
	public int[] fileUpload(@RequestParam("files[]") MultipartFile[] files) throws IOException {
		int[] fileArray = new int[files.length];
		int i = 0;
		
		for (MultipartFile file : files) {
			fileArray[i] = fileService.saveFile(file, "");
			i++;
	    }
		
		return fileArray;
	}
	
	// 파일 업로드 목록 보기
	@PostMapping("/user/file/getFileById")
	@ResponseBody
	public List<FileVo> getFileById(String file) {
		
		List<FileVo> fileList = new ArrayList<>();
		
		for (String str : file.split(",\\s*")) {
			fileList.add(fileService.getFileById(Integer.parseInt(str)));
		}

		return fileList;
	}
	
	// 서버에서 이미지 불러오기
	@GetMapping("/user/file/images/{id}")
	@ResponseBody
	public Resource images(@PathVariable("id") int id, Model model) throws IOException {
		
		String imagePath = fileService.getImageFileById(id).getSavedPath();
		
		return new UrlResource("file:" + imagePath);
	}
	
	// 첨부파일 삭제
//	@GetMapping("/user/file/images/{id}")
//	@ResponseBody
//	public Resource images(@PathVariable("id") int id, Model model) throws IOException {
//		
//		String imagePath = fileService.getImageFileById(id).getSavedPath();
//		
//		return new UrlResource("file:" + imagePath);
//	}
	
}