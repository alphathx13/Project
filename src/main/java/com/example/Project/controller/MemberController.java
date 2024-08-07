package com.example.Project.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.Project.service.FileService;
import com.example.Project.service.MemberService;
import com.example.Project.util.Util;
import com.example.Project.vo.Member;
import com.example.Project.vo.ResultData;
import com.example.Project.vo.Rq;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class MemberController {

	private MemberService memberService;
	private FileService fileService;
	private Rq rq;
	
	// salt 키
	@Value("${custom.salt.key}")
	private String salt;
	
	// 네이버 관련 키
	@Value("${custom.naver.clientId}")
	private String clientId;

	@Value("${custom.naver.clientSecret}")
	private String clientSecret;

	@Value("${custom.naver.baseUrl}")
	private String baseUrl;

	public MemberController(MemberService memberService, FileService fileService, Rq rq) {
		this.memberService = memberService;
		this.fileService = fileService;
		this.rq = rq;
	}
	
	@GetMapping("/user/member/join")
	public String join() {
		return "/user/member/join"; 
	}

	@PostMapping("/user/member/checkJoin")
	@ResponseBody
	public String checkJoin(String loginId, String loginPw, String name, String nickname, String cellphone, String email, @RequestParam(defaultValue = "") MultipartFile file) {
		
		int memberImg = 1;
		
		if (!file.isEmpty()) {
			try {
				memberImg = fileService.saveFile(file, "member");
			} catch (IOException e) {
				Util.jsReplace("회원 가입 과정에서 문제가 발생하였습니다. 가입절차를 다시 진행해주세요.", "/user/home/main");
			}
		}
		
		memberService.checkJoin(loginId, pwSecure(pwSecure(loginPw)), name, nickname, cellphone, email, memberImg);

		try {
			memberService.sendCheckJoinEmail(memberService.getMemberByCellphone(cellphone).getId(), email);
		} catch (Exception e) {
			System.out.println("에러코드 : " + e);
			memberService.memberJoinFail(memberService.getMemberByCellphone(cellphone).getId());
			fileService.memberImgDelete(memberService.getMemberByCellphone(cellphone).getMemberImg());
			return Util.jsReplace("회원 가입 과정에서 문제가 발생하였습니다. 가입절차를 다시 진행해주세요.", "/user/home/main");
		}
		
		return Util.jsReplace("회원 가입 이메일이 전송되었습니다. 이메일을 확인해주세요.", "/user/home/main");
	}
	
	@GetMapping("/user/member/doJoin")
	@ResponseBody
	public String join(int id) {
		
		Member member = memberService.getMemberById(id);
		
		if (member.getCheckJoin() != 0) {
			return Util.jsReplace("비 정상적인 접근입니다.", "/");
		}
		
		memberService.doJoin(id);
		return Util.jsReplace("회원가입이 정상적으로 이루어졌습니다.", "/user/home/main");
	}

	@PostMapping("/user/member/firebaseLogin")
	@ResponseBody
	public ResultData<String> firebaseLogin(Model model, String uid, String email) {
		
		Member member = memberService.getMemberByUid(uid);
		
		if (member == null) {
			return ResultData.from("F-1", "false");
		} 
		
		rq.login(member);
		
		return ResultData.from("S-1", "true", member.getNickname());
	}
	
	@GetMapping("/user/member/naverLogin")
	@ResponseBody
	public void naverLogin(HttpServletRequest request, HttpServletResponse response) throws MalformedURLException, UnsupportedEncodingException, URISyntaxException {
        String url = getNaverCode("authorize");
        try {
            response.sendRedirect(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	@GetMapping("/user/member/firebaseJoin")
	public String firebaseJoin(Model model, String uid, String email) {
		model.addAttribute("uid", uid);
		model.addAttribute("email", email);
		return "/user/member/firebaseJoin";
	}
	
	@PostMapping("/user/member/firebaseCheckJoin")
	@ResponseBody
	public String firebaseCheckJoin(String name, String nickname, String cellphone, String email, String uid, @RequestParam(defaultValue = "") MultipartFile file) {
		
		int memberImg = 1;
		
		if (!file.isEmpty()) {
			try {
				memberImg = fileService.saveFile(file, "member");
			} catch (IOException e) {
				Util.jsReplace("회원 가입 과정에서 문제가 발생하였습니다. 가입절차를 다시 진행해주세요.", "/user/home/main");
			}
		}
		
		memberService.firebaseCheckJoin(name, nickname, cellphone, email, uid, memberImg);
		Member member = memberService.getMemberByUid(uid);
		
		rq.login(member);
		
		return Util.jsReplace("정상적으로 등록되었습니다. 모든 기능을 사용하실 수 있습니다.", "/user/home/main");
	}
	
	@PostMapping("/user/member/doLogin")
	@ResponseBody
	public String doLogin(String loginId, String loginPw, String uri) {
		
		Member member = memberService.getMemberByLoginId(loginId);
		
		if (member == null) 
			return Util.jsBack(String.format("[ %s ] 계정은 존재하지 않습니다.", loginId));
		
		if (member.getCheckJoin() == 0)  
			return Util.jsBack(String.format("[ %s ] 계정에 대한 이메일 확인이 이루어지지 않았습니다. 메일을 확인해주세요.", loginId));
		
		if (!member.getLoginPw().equals(pwSecure(pwSecure(loginPw)))) 
			return Util.jsBack(String.format("비밀번호가 일치하지 않습니다."));
		
		if (member.getDelStatus() == 1) {
			return Util.jsConfirm("탈퇴확인용 이메일이 전송된 상태입니다. 탈퇴를 취소하시겠습니까?", String.format("withdrawalCancel?id=%s", member.getId()), "/");
		} else if (member.getDelStatus() == 2) {
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
	public String withdrawal(Model model) {
		model.addAttribute("nickname", rq.getLoginMemberNn());
		return "/user/member/withdrawal";
	}
	
	@PostMapping("/user/member/checkWithdrawal")
	@ResponseBody
	public String checkWithdrawal(String reason) {
		Member member = memberService.getMemberById(rq.getLoginMemberNumber());
		rq.logout();
		
		try {
			memberService.sendWithdrawalEmail(member);
		} catch (Exception e) {
			System.out.println("에러코드 : " + e);
			return Util.jsReplace("탈퇴신청 메일 발송에 실패했습니다", "/");
		}

		memberService.checkWithdrawal(rq.getLoginMemberNumber(), reason);
		
		return Util.jsReplace("이메일로 탈퇴신청 메일이 전송되었습니다. 이메일 링크에서 승인하시면 탈퇴절차가 진행됩니다.", "/");
	}
	
	@GetMapping("/user/member/doWithdrawal")
	@ResponseBody
	public String doWithdrawal(int id) {
		
		Member member = memberService.getMemberById(id);
		
		if (member.getDelStatus() != 1) {
			return Util.jsReplace("비정상적인 접근입니다.", "/");
		}
		
		memberService.doWithdrawal(id);
		return Util.jsReplace("탈퇴신청 정상적으로 이루어졌습니다. 탈퇴후 일주일동안 탈퇴를 취소할 수 있습니다.", "/");
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
		return "user/member/myPage";
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
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			
			md.update((pwd+salt).getBytes());
			byte[] pwdsalt = md.digest();
			
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
	
	@GetMapping("/user/member/findLoginId")
	public String findLoginId() {
		return "user/member/findLoginId";
	}

	@PostMapping("/user/member/doFindLoginId")
	@ResponseBody
	public String doFindLoginId(String name, String cellphone, String email) {

		Member member = memberService.doFindLoginId(name, cellphone, email);

		if (member == null) {
			return Util.jsBack("입력하신 정보와 일치하는 회원이 없습니다");
		}

		return Util.jsReplace(String.format("회원님의 아이디는 [ %s ] 입니다", member.getLoginId()), "login");
	}
	
	@GetMapping("/user/member/findLoginPw")
	public String findLoginPw() {
		return "user/member/findLoginPw";
	}

	@PostMapping("/user/member/doFindLoginPw")
	@ResponseBody
	public String doFindLoginPw(String loginId, String email) {

		Member member = memberService.getMemberByLoginId(loginId);

		if (member == null) {
			return Util.jsBack("입력하신 아이디와 일치하는 회원이 없습니다");
		}

		if (member.getEmail().equals(email) == false) {
			return Util.jsBack("이메일이 일치하지 않습니다");
		}

		String tempPassword = Util.createTempPassword();

		try {
			memberService.sendPasswordRecoveryEmail(member.getEmail(), tempPassword);
		} catch (Exception e) {
			System.out.println("에러코드 : " + e);
			return Util.jsReplace("임시 패스워드 발송에 실패했습니다", "/");
		}
		memberService.doPasswordModify(member.getId(), pwSecure(pwSecure(tempPassword)));

		return Util.jsReplace("회원님의 이메일주소로 임시 패스워드가 발송되었습니다", "login");
	}
	
	// 네이버 코드 받아오기
	public String getNaverCode(String type) throws URISyntaxException, MalformedURLException, UnsupportedEncodingException {

        String redirectUrl = "http://localhost:8000/user/member/naverLoginCheck";

        UriComponents uriComponents = UriComponentsBuilder
                .fromUriString(baseUrl + "/" + type)
                .queryParam("response_type", "code")
                .queryParam("client_id", clientId)
                .queryParam("state", URLEncoder.encode("helloWorld", "UTF-8"))
                .queryParam("redirect_uri", URLEncoder.encode(redirectUrl, "UTF-8"))
                .build();

        return uriComponents.toString();
    }
	
	// 네이버 토큰 받아오기
	public String getNaverToken(String type, String code, String state) throws URISyntaxException, MalformedURLException, UnsupportedEncodingException {

        UriComponents uriComponents = UriComponentsBuilder
                .fromUriString(baseUrl + "/" + type)
                .queryParam("grant_type", "authorization_code")
                .queryParam("client_id", clientId)
                .queryParam("client_secret", clientSecret)
                .queryParam("code", code)
                .queryParam("state", URLEncoder.encode(state, "UTF-8"))
                .build();

        try {
            URL url = new URL(uriComponents.toString());
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");

            int responseCode = con.getResponseCode();
            BufferedReader br;

            if(responseCode==200) {
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else { 
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }

            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }

            br.close();
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return null;
        
    }
	
	@GetMapping("/user/member/naverLoginCheck")
	@ResponseBody
	public String naverLoginCheck(Model model, HttpServletRequest request, HttpServletResponse response, String code, String state) throws MalformedURLException, UnsupportedEncodingException, URISyntaxException, JsonMappingException, JsonProcessingException {
		
		String tokenList = getNaverToken("token", code, state);
		
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = objectMapper.readTree(tokenList);
		String accessToken = jsonNode.get("access_token").asText();
		
		try {
            String urlString = "https://openapi.naver.com/v1/nid/me";
            URL url = new URL(urlString);
            
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + accessToken);
            
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder user = new StringBuilder();
            
            while ((inputLine = in.readLine()) != null) {
                user.append(inputLine);
            }
            in.close();
            
            JsonNode rootNode = objectMapper.readTree(user.toString());
            
            JsonNode responseNode = rootNode.path("response");
            
            String uid = responseNode.path("id").asText();
            String email = responseNode.path("email").asText();
            
    		Member member = memberService.getMemberByUid(uid);
    		
    		if (member == null) {
    			return Util.jsReplace("처음 이용하시는 계정입니다. 사이트 이용을 위해 추가적인 정보를 입력하셔야 합니다.", String.format("/user/member/firebaseJoin?uid=%s&email=%s", uid, email));
    		} 
    		
    		rq.login(member);
    		
    		return Util.jsReplace(String.format("%s 님 로그인을 환영합니다.", member.getNickname()), "/");
    		
        } catch (Exception e) {
            e.printStackTrace();
        }
		
		return null;

	}
	
}