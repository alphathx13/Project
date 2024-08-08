package com.example.Project.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.Project.dao.MemberDao;
import com.example.Project.vo.Member;

@Service
public class MemberService {

	private MemberDao memberDao;
	private AWSSESService emailSender;

	public MemberService(MemberDao memberDao, AWSSESService emailSender) {
		this.memberDao = memberDao;
		this.emailSender = emailSender;
	}

	public void checkJoin(String loginId, String loginPw, String name, String nickname, String cellphone, String email, int memberImg) {
		memberDao.checkJoin(loginId, loginPw, name, nickname, cellphone, email, memberImg);
	}
	
	public void doJoin(int id) {
		memberDao.doJoin(id);
	}
	
	public Member getMemberByLoginId(String loginId) {
		return memberDao.getMemberByLoginId(loginId);
	}

	public Member getMemberById(int id) {
		return memberDao.getMemberById(id);
	}
	
	public Member memberInfo(int id) {
		return memberDao.memberInfo(id);
	}

	public void change(int memberNumber, String loginPw, String nickname, String cellphone, String email) {
		memberDao.change(memberNumber, loginPw, nickname, cellphone, email);
	}

	public Member getMemberByCellphone(String cellphone) {
		return memberDao.getMemberByCellphone(cellphone);
	}

	public Member getMemberByEmail(String email) {
		return memberDao.getMemberByEmail(email);
	}

	public Member getMemberByNickname(String nickname) {
		return memberDao.getMemberByNickname(nickname);
	}

	public boolean passCheck(String loginId, String loginPw) {
		return memberDao.passCheck(loginId, loginPw) == 0 ? false : true;
	}

	public void checkWithdrawal(int id, String reason) {
		memberDao.checkWithdrawal(id, reason);
	}

	public void doWithdrawal(int id) {
		memberDao.doWithdrawal(id);
	}
	
	public void withdrawalCancel(int id) {
		memberDao.withdrawalCancel(id);
	}

	public Member doFindLoginId(String name, String cellphone, String email) {
		return memberDao.doFindLoginId(name, cellphone, email);
	}

	public void doPasswordModify(int id, String loginPw) {
		memberDao.doPasswordModify(id, loginPw);
	}
	
    public void sendPasswordRecoveryEmail(String email, String tempPassword) {
        
        List<String> receivers = new ArrayList<String>();
        receivers.add(email);
        
        emailSender.passRecoveryMailSend(tempPassword, receivers);
        
    }

	public void sendWithdrawalEmail(Member member) {
        
        List<String> receivers = new ArrayList<String>();
        receivers.add(member.getEmail());
        
        emailSender.withdrawalMailSend(member.getId(), receivers);
        
	}
	
	public void sendCheckJoinEmail(int id, String email) {
		
        List<String> receivers = new ArrayList<String>();
        receivers.add(email);
        
        emailSender.welcomeMailSend(id, receivers);
	}
	
	public void memberJoinFail(int id) {
		memberDao.memberJoinFail(id);
	}
	
	// 주기적으로 멤버 삭제처리
	public void memberDelete() {
		memberDao.memberDelete();
	}

	public String getMemberImg(int id) {
		return memberDao.getMemberImg(id);
	}

	public String getNicknameById(int id) {
		return memberDao.getNicknameById(id);
	}

	public List<Integer> getDeleteMemberImg() {
		return memberDao.getDeleteMemberImg();
	}

	public Member getMemberByUid(String uid) {
		return memberDao.getMemberByUid(uid);
	}

	public void firebaseCheckJoin(String name, String nickname, String cellphone, String email, String uid, int memberImg) {
		memberDao.firebaseCheckJoin(name, nickname, cellphone, email, uid, memberImg);
	}

}
