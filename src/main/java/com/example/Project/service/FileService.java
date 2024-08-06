package com.example.Project.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.example.Project.dao.FileDao;
import com.example.Project.vo.FileVo;

@Service
public class FileService {

	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	@Value("${cloud.aws.region.static}")
	private String region;

	private FileDao fileDao;
	private AmazonS3Client amazonS3Client;

	public FileService(FileDao fileDao, AmazonS3Client amazonS3Client) {
		this.fileDao = fileDao;
		this.amazonS3Client = amazonS3Client;
	}
	
	// 실제 파일 업로드
	public int saveFile(MultipartFile file, @RequestParam(defaultValue = "") String type) throws IOException {

		String orgName = file.getOriginalFilename();
		String uuid = UUID.randomUUID().toString();
		String extension = orgName.substring(orgName.lastIndexOf("."));
		String savedName = uuid + extension;
		String awsPath = "http://" + bucket + ".s3." + region + ".amazonaws.com";
		String savedPath;
		String table;
		
		if (type.equals("member")) {
			savedName = "memberImg/" + savedName;
			savedPath = awsPath + "/" + savedName;
			table = "memberImg";
		} else if (type.equals("image")) {
			savedName = "imgUpload/" + savedName;
			savedPath = awsPath + "/" + savedName;
			table = "imgUpload";
		} else {
			savedName = "fileUpload/" + savedName;
			savedPath = awsPath + "/" + savedName;
			table = "fileUpload";
		}
		
		if (table.equals("memberImg")) {
			fileDao.memberImgUpload(orgName, savedName, savedPath);
		} else if (table.equals("imgUpload")) {
			fileDao.imgUpload(orgName, savedName, savedPath);
		} else {
			fileDao.fileUpload(orgName, savedName, savedPath);
		}
		
		try {
		    ObjectMetadata metadata = new ObjectMetadata();
		    metadata.setContentType(file.getContentType());
		    metadata.setContentLength(file.getSize());
		    amazonS3Client.putObject(bucket, savedName, file.getInputStream(), metadata);
		    
		} catch (IOException e) {
		    e.printStackTrace();
		} catch (AmazonServiceException e) {
		    // AWS S3에서 발생할 수 있는 예외 처리
		    e.printStackTrace();
		} catch (SdkClientException e) {
		    // AWS S3 클라이언트에서 발생할 수 있는 예외 처리
		    e.printStackTrace();
		}
		
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

	// 번호로 멤버이미지 삭제
	public void memberImgDelete(int id) {
		fileDao.memberImgDelete(id);
	}

	// DB와 서버에서 파일 삭제
	public void fileAndFileDBDelete(String[] list, String type) {
		
		if(!list[0].equals("")) {
			// int[] 배열로 변환
			int[] numbers = Arrays.stream(list).mapToInt(Integer::parseInt).toArray();
			
			// 실제 DB에 저장되어있는 파일 저장위치 가져오기
	        String[] pathArr = new String[numbers.length];
	        int i = 0;
	        
	        for (int id : numbers) {
	        	if (type.equals("image")) {
	        		pathArr[i] = fileDao.getImageNameById(id);
	        	} else {
	        		pathArr[i] = fileDao.getFileNameById(id);
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
	        
	        // S3에서 파일 삭제
	        for (String filePath : pathArr) {
	        	amazonS3Client.deleteObject(bucket, filePath);
	        }
		}
	}
	
	// DB와 서버에서 파일 삭제
	public void memberImgDelete(List<Integer> memberImgList) {
		
		if (memberImgList.size() == 0)
			return;
		
		// 실제 DB에 저장되어있는 파일 저장위치 가져오기
        String[] pathArr = new String[memberImgList.size()];
        
        int i = 0;
        for (Integer id : memberImgList) {
        	pathArr[i] = fileDao.getMemberImgPath(id);
        	i++;
        }
        
        // DB에서 파일 정보 삭제
        for (Integer id : memberImgList) {
        	fileDao.memberImgDelete(id);
        }
        
        // S3에서 파일 삭제
        for (String filePath : pathArr) {
        	amazonS3Client.deleteObject(bucket, filePath);
        }
	}

	public FileVo getFileById(int id) {
		return fileDao.getFileById(id);
	}
	
	public void uploadFileDelete(int id) {
		amazonS3Client.deleteObject(bucket, fileDao.getFileNameById(id));
		fileDao.fileDBDelete(id);
	}

	public String getMemberImgPath(int memberNumber) {
		return fileDao.getMemberImgPath(memberNumber);
	}

}

