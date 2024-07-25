package com.example.Project.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.example.Project.vo.FileVo;

@Mapper
public interface FileDao {

	@Insert("""
			INSERT INTO file
				SET regDate = NOW()
					, originName = #{orgName}
					, savedName = #{savedName}
					, savedPath = #{savedPath}
			""")
	void insertFile(String orgName, String savedName, String savedPath);

	@Select("""
			SELECT id
				FROM `file`
				ORDER BY id DESC
				LIMIT 1
			""")
	int lastFileId();

	@Select("""
			SELECT *
				FROM `file`
				WHERE id = #{id}
			""")
	FileVo getFileById(int id);

	@Update("""
			UPDATE `file`
				SET articleId = #{articleId}
				WHERE id = #{image}
			""")
	void imageArticleId(int articleId, int image);

	@Select("""
			SELECT savedPath
			FROM `file`
			WHERE articleId = #{id}
			""")
	List<String> getImagePath(int id);

	@Delete("""
			DELETE FROM `file`
				WHERE articleId = #{id}
			""")
	void imageDBDelete(int id);

}