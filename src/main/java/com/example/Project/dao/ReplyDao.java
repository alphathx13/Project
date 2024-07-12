package com.example.Project.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.example.Project.vo.Reply;

@Mapper
public interface ReplyDao {

	@Insert("""
			INSERT INTO reply
				SET regDate = NOW()
					, updateDate = NOW()
					, memberId = #{loginMemberNumber}
					, replyBody = #{replyBody}
					, relTypeCode = #{relTypeCode}
					, relId = #{relId}
			""")
	public void writeReply(int loginMemberNumber, String replyBody, String relTypeCode, int relId);
	
	@Select("""
			SELECT r.*, m.nickname `nickname`, IFNULL(SUM(l.point), 0) `likePoint`
				FROM reply r
				LEFT JOIN `member` m
					ON r.memberId = m.id
				LEFT JOIN likePoint l
                    ON r.id = l.relId
				WHERE r.relTypeCode = #{relTypeCode} AND r.relID = #{relId}
                GROUP BY r.id
			""")
	public List<Reply> viewReply(String relTypeCode, int relId);

	@Update("""
			UPDATE reply
				SET replyBody = #{replyBody}
					, updateDate = NOW()
				WHERE id = #{id}
			""")
	public void replyModify(int id, String replyBody);

	@Delete("""
			DELETE FROM reply
				where id = #{id}
			""")
	public void replyDelete(int id);

	@Select("""
			SELECT * FROM reply
				where id = #{id}
			""")
	public Reply getReplyBody(int id);
}