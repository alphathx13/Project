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
			SELECT r.*, m.nickname `nickname`
				FROM reply r
				LEFT JOIN `member` m
					on r.memberId = m.id
				WHERE r.relTypeCode = #{relTypeCode} AND r.relID = #{relId}
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