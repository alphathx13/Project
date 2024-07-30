package com.example.Project.service;

import java.util.List;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.example.Project.dao.MemberDao;
import com.example.Project.vo.Member;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
@Service
public class MemberService {

	private MemberDao memberDao;
	private JavaMailSender javaMailSender;

	public MemberService(MemberDao memberDao, JavaMailSender javaMailSender) {
		this.memberDao = memberDao;
		this.javaMailSender = javaMailSender;
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
        String subject = "임시 패스워드 발송";
        String text = "<html>"
                    + "<body>"
                    + "<h3>임시 패스워드 : " + tempPassword + "</h3>"
                    + "<a style='display:inline-block;padding:10px;border-radius:10px;border:5px solid black;font-size:4rem;color:inherit;text-decoration:none;' href='http://localhost:8081/' target='_blank'>로그인 하러가기</a>"
                    + "</body>"
                    + "</html>";
        sendEmail(email, subject, text);
    }

	public void sendWithdrawalEmail(Member member) {
		String subject = "탈퇴 메일 발송";
        String text = "<html>"
                    + "<body>"
                    + "<h3>" + member.getNickname() + "님 지금까지 현 사이트를 이용해주셔서 감사합니다." + "</h3>"
                    + "<h3> 아래버튼을 누르면 회원탈퇴가 이루어집니다. </h3>"
                    + "<h3> 회원탈퇴시 일주일 내로 탈퇴신청을 취소하실 수 있습니다. </h3>"
                    + "<a style='display:inline-block;padding:10px;border-radius:10px;border:5px solid black;font-size:4rem;color:inherit;text-decoration:none;' href='http://localhost:8000/user/member/doWithdrawal?id=" + member.getId() +  "' target='_blank'>회원탈퇴</a>"
                    + "</body>"
                    + "</html>";
        sendEmail(member.getEmail(), subject, text);
	}
	
	public void sendCheckJoinEmail(int id, String email) {
		String subject = "가입 메일 발송";
        String text = "<html>"
                    + "<body>"
                    + "<h3> 회원가입을 환영합니다. </h3>"
                    + "<a style='display:inline-block;padding:10px;border-radius:10px;border:5px solid black;font-size:4rem;color:inherit;text-decoration:none;' href='http://localhost:8000/user/member/doJoin?id=" + id +  "' target='_blank'>회원가입</a>"
                    + "</body>"
                    + "</html>";
        sendEmail(email, subject, text);
		
	}
	
	public void memberJoinFail(int id) {
		memberDao.memberJoinFail(id);
	}
	
	public void sendEmail(String to, String subject, String text) {
		MimeMessage message = javaMailSender.createMimeMessage();
		
		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(text, true);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		
		javaMailSender.send(message);
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

}
