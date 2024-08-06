package com.example.Project.dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.example.Project.vo.FileVo;

@Mapper
public interface FileDao {

	@Insert("""
			INSERT INTO memberImg
				SET regDate = NOW()
					, originName = #{orgName}
					, savedName = #{savedName}
					, savedPath = #{savedPath}
			""")
	void memberImgUpload(String orgName, String savedName, String savedPath);

	@Insert("""
			INSERT INTO imgUpload
				SET regDate = NOW()
					, originName = #{orgName}
					, savedName = #{savedName}
					, savedPath = #{savedPath}
			""")
	void imgUpload(String orgName, String savedName, String savedPath);

	@Insert("""
			INSERT INTO fileUpload
				SET regDate = NOW()
					, originName = #{orgName}
					, savedName = #{savedName}
					, savedPath = #{savedPath}
			""")
	void fileUpload(String orgName, String savedName, String savedPath);

	@Select("""
			SELECT id
				FROM memberImg
				ORDER BY id DESC
				LIMIT 1
			""")
	int memberImgLast();
	
	@Select("""
			SELECT id
				FROM imgUpload
				ORDER BY id DESC
				LIMIT 1
			""")
	int imgLast();

	@Select("""
			SELECT id
				FROM fileUpload
				ORDER BY id DESC
				LIMIT 1
			""")
	int fileLast();

	@Select("""
			SELECT *
				FROM imgUpload
				WHERE id = #{id}
			""")
	FileVo getImageFileById(int id);

	@Select("""
			SELECT savedName
				FROM imgUpload
				WHERE id = #{id}
			""")
	String getImageNameById(int id);

	@Select("""
			SELECT savedName
				FROM fileUpload
				WHERE id = #{id}
			""")
	String getFileNameById(int id);
	
	@Delete("""
			DELETE FROM imgUpload
				WHERE id = #{id}
			""")
	void imageDBDelete(int id);

	@Delete("""
			DELETE FROM fileUpload
				WHERE id = #{id}
			""")
	void fileDBDelete(int id);

	@Delete("""
			DELETE FROM memberImg
				WHERE id = #{memberImg}
			""")
	void memberImgDelete(int memberImg);

	@Select("""
			SELECT *
				FROM fileUpload
				WHERE id = #{id}
			""")
	FileVo getFileById(int id);

	@Select("""
			SELECT savedPath
				FROM memberImg
				WHERE id = #{id}
			""")
	String getMemberImgPath(int id);

}