<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:set var="pageTitle" value="마이페이지" />

<%@ include file="../../common/head.jsp"%>

<section class = "ml-4">
	<div>가입일 : ${member.regDate }</div>
	<div>아이디 : ${member.loginId }</div>
	<div>이름 : ${member.name }</div>
	<div>별명 : ${member.nickname }</div>
	<div>핸드폰 : ${member.cellphone }</div>
	<div>이메일 : ${member.email }</div>
</section>

<div class="flex justify-between mx-4">
	<div class="tooltip" data-tip="뒤로 가기">
		<button class="btn btn-outline btn-info" onclick="history.back();">			
			<i class="fa-solid fa-arrow-left-long"></i>
		</button>
	</div>
	<div>
		<button class="btn btn-outline btn-info" onclick="my_modal_1.showModal()">회원 정보 수정</button>
	</div>
</div>
<dialog id="my_modal_1" class="modal">
 		<div class="modal-box">
   		<h3 class="text-lg font-bold">비밀번호를 입력해주세요</h3>
   		<div class="container mx-auto px-3 w-72">
			<form action="passCheck" method="post" onsubmit="check(this); return false;">
				<label class="mt-1 input input-bordered flex items-center gap-2">
					<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 16 16"
						fill="currentColor" class="h-4 w-4 opacity-70">
		   				<path fill-rule="evenodd"
							d="M14 6a4 4 0 0 1-4.899 3.899l-1.955 1.955a.5.5 0 0 1-.353.146H5v1.5a.5.5 0 0 1-.5.5h-2a.5.5 0 0 1-.5-.5v-2.293a.5.5 0 0 1 .146-.353l3.955-3.955A4 4 0 1 1 14 6Zm-4-2a.75.75 0 0 0 0 1.5.5.5 0 0 1 .5.5.75.75 0 0 0 1.5 0 2 2 0 0 0-2-2Z"
							clip-rule="evenodd" />
		  			</svg> 
		  			<input maxlength="12" type="password" class="pw grow" placeholder="비밀번호" name="pw"/> 
		  			<button class="change" type="button"><i class="see fa-solid fa-eye"></i><i class="notSee hidden fa-solid fa-eye-slash"></i></button>
				</label>
				<div class="tooltip w-full" data-tip="암호 확인">
					<button class="mt-5 w-full text-xl btn btn-outline">
						<i class="fa-solid fa-right-to-bracket text-black"></i>
					</button>
				</div>
			</form>
		</div>
     		<form method="dialog">
       		<button class="btn">Close</button>
     		</form>
   	</div>
</dialog>

<script>
		function check(form) {
			let pw = form.pw.value.trim();
			let loginPw = '${member.loginPw}';
	
			if (pw.length == 0) {
				alert('비밀번호를 입력해주세요.');
				form.pw.focus();
				return;
			}
			
			if (pw != loginPw) {
				alert('암호가 일치하지 않습니다.');
				form.pw.focus();
				location.reload(true);
				return;
			}
	
			form.submit();
		}

		$('.change').click(function() {
			if ($('.pw').attr('type') == 'password') {
				$('.pw').attr('type', 'text');
				$('.see').css('display', 'none');
				$('.notSee').css('display', 'block');
			} else {
				$('.pw').attr('type', 'password');
				$('.see').css('display', 'block');
				$('.notSee').css('display', 'none');
			}
		})
</script>


<%@ include file="../../common/foot.jsp"%>