package com.example.Project.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.example.Project.service.MemberService;
import com.example.Project.util.Util;
import com.example.Project.vo.Member;
import com.example.Project.vo.ResultData;
import com.example.Project.vo.Rq;

@Controller
public class MemberController {

	private MemberService memberService;
	private Rq rq;
	
	@Value("${custom.salt.key}")
	private String salt;

	public MemberController(MemberService memberService, Rq rq) {
		this.memberService = memberService;
		this.rq = rq;
	}
	
	@GetMapping("/user/member/join")
	public String join() {
		return "/user/member/join"; 
	}
	
	@PostMapping("/user/member/doJoin")
	@ResponseBody
	public String join(String loginId, String loginPw, String name, String nickname, String cellphone, String email) {
		memberService.memberJoin(loginId, pwSecure(pwSecure(loginPw)), name, nickname, cellphone, email);
		return Util.jsReplace("정상적으로 회원가입 되었습니다.", "/user/home/main");
	}

	@GetMapping("/user/member/login")
	public String login() {
		return "user/member/login";
	}
	
	@PostMapping("/user/member/doLogin")
	@ResponseBody
	public String doLogin(String loginId, String loginPw, String uri) {
		
		Member member = memberService.getMemberByLoginId(loginId);
		
		if (member == null) 
			return Util.jsBack(String.format("[ %s ] 계정은 존재하지 않습니다.", loginId));
		
		if (!member.getLoginPw().equals(pwSecure(pwSecure(loginPw)))) 
			return Util.jsBack(String.format("비밀번호가 일치하지 않습니다."));
		
		if (member.getDelStatus() != 0) {
			return Util.jsConfirm("현재 탈퇴처리가 진행중 계정입니다. 탈퇴를 취소하시겠습니까?", String.format("withdrawalCancel?id=%s", member.getId()), "/");
		}

		rq.login(member);
		
		return Util.jsReplace(String.format("%s 님의 로그인을 환영합니다.", member.getNickname()), uri);
	}
	
	@GetMapping("/user/member/doLogout")
	@ResponseBody
	public String doLogout(String uri) {
		rq.logout();
		return Util.jsReplace("정상적으로 로그아웃 되었습니다.", uri);
	}
	
	@GetMapping("/user/member/myPage")
	public String myPage(Model model) {
		model.addAttribute("member", memberService.memberInfo(rq.getLoginMemberNumber()));
		return "user/member/myPage";
	}
	
	@PostMapping("/user/member/passCheck")
	@ResponseBody
	public String passCheck(String type, String loginId, String loginPw) {
		boolean check = memberService.passCheck(loginId, pwSecure(pwSecure(loginPw)));
		
		if (check == false) 
			return Util.jsReplace("비밀번호가 일치하지않습니다.", "/user/member/myPage");
		
		if (type.equals("config")) {
			return Util.jsReplace("", "/user/member/config");
		} 
			
		return Util.jsConfirm("정말로 탈퇴하시겠습니까?", "withdrawal", "myPage");
	}
	
	@GetMapping("/user/member/config")
	public String config(Model model) {
		model.addAttribute("member", memberService.getMemberById(rq.getLoginMemberNumber()));
		return "/user/member/config";
	}
	
	@GetMapping("/user/member/withdrawal")
	@ResponseBody
	public String withdrawal() {
		memberService.withdrawal(rq.getLoginMemberNumber());
		rq.logout();
	
		return Util.jsReplace("회원 탈퇴처리가 진행중입니다. 일주일내로 취소하실 수 있습니다.", "/user/home/main");
	}
	
	@GetMapping("/user/member/withdrawalCancel")
	@ResponseBody
	private String withdrawalCancel(int id) {
		memberService.withdrawalCancel(id);
		return Util.jsReplace("탈퇴처리가 취소되었습니다. 정상적으로 계정을 이용하실 수 있습니다.", "/");
	}
	
	@PostMapping("/user/member/memberModify")
	public String memberModify(Model model, String nickname, String loginPw, String cellphone, String email){
		
		memberService.change(rq.getLoginMemberNumber(), pwSecure(pwSecure(loginPw)), nickname, cellphone, email);
		
		model.addAttribute("member", memberService.getMemberById(rq.getLoginMemberNumber()));
		return "usr/member/myPage";
	}

	@GetMapping("/user/member/idDupCheck")
	@ResponseBody
	public ResultData<Boolean> idDupCheck(String loginId){
		
		Member member = memberService.getMemberByLoginId(loginId);
		
		if (member != null) {
			return ResultData.from("F-1", String.format("입력하신 [%s] 아이디는 이미 사용중인 아이디입니다.", loginId), true);
		}
		
		return ResultData.from("S-1", String.format("입력하신 [%s] 아이디는 사용하실 수 있습니다.", loginId), false);
	}
	
	@GetMapping("/user/member/nicknameDupCheck")
	@ResponseBody
	public ResultData<Boolean> nicknameDupCheck(String nickname){
		
		Member member = memberService.getMemberByNickname(nickname);
		
		if (member != null) {
			return ResultData.from("F-1", String.format("입력하신 [%s] 별명은 이미 사용중인 아이디입니다.", nickname), true);
		}
		
		return ResultData.from("S-1", String.format("입력하신 [%s] 별명은 사용하실 수 있습니다.", nickname), false);
	}
	
	@GetMapping("/user/member/cellphoneDupCheck")
	@ResponseBody
	public ResultData<Boolean> cellphoneDupCheck(String cellphone){
		
		Member member = memberService.getMemberByCellphone(cellphone);
		
		if (member != null) {
			return ResultData.from("F-1", String.format("입력하신 [%s] 번호는 이미 사용중입니다.", cellphone), true);
		}
		
		return ResultData.from("S-1", String.format("입력하신 [%s] 번호는 사용하실 수 있습니다.", cellphone), false);
	}
	
	@GetMapping("/user/member/emailDupCheck")
	@ResponseBody
	public ResultData<Boolean> emailDupCheck(String email){
		
		Member member = memberService.getMemberByEmail(email);
		
		if (member != null) {
			return ResultData.from("F-1", String.format("입력하신 [%s] 이메일은 이미 사용중인 아이디입니다.", email), true);
		}
		
		return ResultData.from("S-1", String.format("입력하신 [%s] 이메일은 사용하실 수 있습니다.", email), false);
	}
	
	@GetMapping("/user/member/loginPwDupCheck")
	@ResponseBody
	public ResultData<Boolean> loginPwDupCheck(String loginId, String loginPw){
		
		boolean check = memberService.passCheck(loginId, pwSecure(pwSecure(loginPw)));
		
		if (check == true) {
			return ResultData.from("F-1", "입력하신 암호는 이전 암호와 같습니다.", true);
		}
		
		return ResultData.from("S-1", "", false);
	}
	
	// 실제 암호화 담당
	public String pwSecure (String pwd) {
		
		String result = "";
		
		try {
			//1. SHA256 알고리즘 객체 생성
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			
			//2. 비밀번호와 salt 합친 문자열에 SHA 256 적용
			md.update((pwd+salt).getBytes());
			byte[] pwdsalt = md.digest();
			
			//3. byte To String (10진수의 문자열로 변경)
			StringBuffer sb = new StringBuffer();
			for (byte b : pwdsalt) {
				sb.append(String.format("%02x", b));
			}
			
			result=sb.toString();
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return result;
	}
	
}