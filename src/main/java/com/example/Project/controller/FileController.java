package com.example.Project.controller;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.S3Object;
import com.example.Project.config.AWSS3Config;
import com.example.Project.service.FileService;
import com.example.Project.service.MemberService;
import com.example.Project.vo.FileVo;

@Controller
public class FileController {

	private MemberService memberService;
	private FileService fileService;

	public FileController(FileService fileService, MemberService memberService) {
		this.fileService = fileService;
		this.memberService = memberService;
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

	// 회원번호로 이미지 가져오기
	@GetMapping("/user/file/memberImg/{id}")
	@ResponseBody
	public Resource images(@PathVariable("id") int id) throws IOException {

		String imagePath = memberService.getMemberImg(id);

		return new UrlResource(imagePath);

	}

	// 첨부파일 삭제
	@PostMapping("/user/file/uploadFileDelete")
	@ResponseBody
	public void uploadFileDelete(int id) {
		fileService.uploadFileDelete(id);
	}

}