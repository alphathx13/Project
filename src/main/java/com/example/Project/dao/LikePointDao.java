package com.example.Project.dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface LikePointDao {

	@Insert("""
			insert into likePoint
				set memberId = #{memberId}
					, relId = #{relId}
					, relTypeCode = #{relTypeCode}; 
			""")
	public void doLike(int memberId, int relId, String relTypeCode);

	@Delete("""
			DELETE FROM likePoint
				where memberId = #{memberId} AND
					relId = #{relId} AND
					relTypeCode = #{relTypeCode}
			""")
	public void undoLike(int memberId, int relId, String relTypeCode);
	
	@Select("""
			SELECT IFNULL(SUM(POINT), 0) `likePoint`
				FROM likePoint
				WHERE relId = #{relId} AND
					relTypeCode = #{relTypeCode}
			""")
	public int totalLikePoint(int relId, String relTypeCode);

	@Select("""
			SELECT COUNT(*)
				FROM likePoint
				WHERE relId = #{relId} AND
					relTypeCode = #{relTypeCode} AND
					memberId = #{loginMemberNumber}
			""")
	public int likeCheck(int loginMemberNumber, int relId, String relTypeCode);

}