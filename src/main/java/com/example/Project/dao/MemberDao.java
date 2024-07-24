package com.example.Project.dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.example.Project.vo.Member;

@Mapper
public interface MemberDao {

	@Insert("""
			INSERT INTO `member`
			 	SET regDate = NOW()
			     	, updateDate = NOW()
			     	, checkJoin = 0
			     	, loginId = #{loginId}
			     	, loginPw = #{loginPw}
			     	, `name` = #{name}
			     	, nickname = #{nickname}
			     	, cellphone = #{cellphone}
			     	, email = #{email}
			     	, memberImg = #{memberImg}
			""")
	public void checkJoin(String loginId, String loginPw, String name, String nickname, String cellphone, String email, int memberImg);
	
	@Update("""
			UPDATE `member`
			 	SET checkJoin = 1
			    WHERE id = #{id}
			""")
	public void doJoin(int id);

	@Delete("""
			DELETE FROM `member`
				WHERE id = #{id}
			""")
	public void memberJoinFail(int id);
	
	@Select("""
			SELECT *
				FROM `member`
				WHERE id = #{id}
			""")
	public Member getMemberById(int id);
	
	@Select("""
			select * 
				from `member` 
				where loginId = #{loginId}
			""")
	public Member getMemberByLoginId(String loginId);

	@Select("SELECT last_insert_id()")
	public int getLastInsertId();

	@Update("""
			UPDATE `member`
				SET loginPw = #{loginPw}
					, nickname = #{nickname}
					, cellphone = #{cellphone}
					, email = #{email}
					, updateDate = NOW()
				WHERE id = #{memberNumber}
			""")
	public void change(int memberNumber, String loginPw, String nickname, String cellphone, String email);

	@Select("""
			select * 
				from `member` 
				where cellphone = #{cellphone}
			""")
	public Member getMemberByCellphone(String cellphone);

	@Select("""
			select * 
				from `member` 
				where email = #{email}
			""")
	public Member getMemberByEmail(String email);

	@Select("""
			select * 
				from `member` 
				where nickname = #{nickname}
			""")
	public Member getMemberByNickname(String nickname);

	@Select("""
			select COUNT(*)
				from `member` 
				where loginId = #{loginId} AND loginPw = #{loginPw}
			""")
	public int passCheck(String loginId, String loginPw);

	@Select("""
			select regDate, loginId, nickname, name, cellphone, email
				from `member` 
				where id = #{id}
			""")
	public Member memberInfo(int id);

	@Update("""
			UPDATE `member`
				SET delStatus = 1
					, delReason = #{reason}
				WHERE id = #{id}
			""")
	public void checkWithdrawal(int id, String reason);
	
	@Update("""
			UPDATE `member`
				SET delStatus = 2,
					delDate = NOW()
				WHERE id = #{id};
			""")
	public void doWithdrawal(int id);

	@Update("""
			UPDATE `member`
				SET delStatus = 0,
				delReason = NULL
				WHERE id = #{id};
			""")
	public void withdrawalCancel(int id);

	@Select("""
			SELECT loginId
				FROM `member`
				WHERE name = #{name} AND cellphone = #{cellphone} AND email = #{email}
			""")
	public Member doFindLoginId(String name, String cellphone, String email);

	@Update("""
			UPDATE `member`
				SET loginPw = #{loginPw}
				WHERE id = #{id}
			""")
	public void doPasswordModify(int id, String loginPw);

	@Delete("""
			DELETE FROM `member`
				WHERE delDate < DATE_SUB(NOW(), INTERVAL 1 WEEK);
			""")
	public void memberDelete();

	@Select("""
			SELECT f.savedPath 
			    FROM `member` m
			    LEFT JOIN `file` f
			    ON m.memberImg = f.id
			    WHERE m.id = #{id}
			""")
	public String getMemberImg(int id);

	@Select("""
			SELECT nickname
			    FROM `member`
			    WHERE id = #{id}
			""")
	public String getNicknameById(int id);

}