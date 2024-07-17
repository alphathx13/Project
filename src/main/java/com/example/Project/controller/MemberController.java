package com.example.Project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.Project.service.MemberService;
import com.example.Project.util.Util;
import com.example.Project.vo.Member;
import com.example.Project.vo.ResultData;
import com.example.Project.vo.Rq;

@Controller
public class MemberController {

	private MemberService memberService;
	private Rq rq;

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
		memberService.memberJoin(loginId, loginPw, name, nickname, cellphone, email);

		return Util.jsReplace("정상적으로 회원가입 되었습니다.", "/user/home/main");
	}

	@GetMapping("/user/member/login")
	public String login() {
		return "user/member/login";
	}
	
	@PostMapping("/user/member/doLogin")
	@ResponseBody
	public String doLogin(String id, String pw, String uri) {
		
		Member member = memberService.getMemberByLoginId(id);
		
		if (member == null) { 
			return Util.jsBack(String.format("[ %s ] 계정은 존재하지 않습니다.", id));
		}
		
		if (!member.getLoginPw().equals(pw)) { 
			return Util.jsBack(String.format("비밀번호가 일치하지 않습니다."));
		}
		
		if (member.getDelStatus() != 0) { 
			return Util.jsBack(String.format("해당 계정은 탈퇴진행중인 계정입니다."));
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
	
	@PostMapping("/user/member/passCheck")
	public String passCheck(Model model) {
		model.addAttribute("member", memberService.getMemberById(rq.getLoginMemberNumber()));
		return "user/member/config";
	}

	@GetMapping("/user/member/myPage")
	public String myPage(Model model) {
		model.addAttribute("member", memberService.getMemberById(rq.getLoginMemberNumber()));
		return "user/member/myPage";
	}
	
	
	@PostMapping("/user/member/memberModify")
	public String memberModify(Model model, String nickname, String loginPw, String cellphone, String email){
		
		memberService.change(rq.getLoginMemberNumber(), loginPw, nickname, cellphone, email);
		
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
	
}