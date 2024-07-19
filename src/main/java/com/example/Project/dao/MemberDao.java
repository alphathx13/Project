package com.example.Project.dao;

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
			     	, loginId = #{loginId}
			     	, loginPw = #{loginPw}
			     	, `name` = #{name}
			     	, nickname = #{nickname}
			     	, cellphone = #{cellphone}
			     	, email = #{email}
			""")
	public void memberJoin(String loginId, String loginPw, String name, String nickname, String cellphone, String email);

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
				SET delStatus = 1,
					delDate = NOW()
				WHERE id = #{loginMemberNumber};
			""")
	public void withdrawal(int loginMemberNumber);

	@Update("""
			UPDATE `member`
				SET delStatus = 0,
					delDate = NULL
				WHERE id = #{id};
			""")
	public void withdrawalCancel(int id);
}