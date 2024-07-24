package com.example.Project.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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

}