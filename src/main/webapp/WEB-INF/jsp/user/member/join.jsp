<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:set var="pageTitle" value="회원가입" />

<%@ include file="../../common/head.jsp"%>

<section class="mt-8 text-lg">
	<div class="container mx-auto px-3">
		<form name="memberForm" action="checkJoin" method="POST" onsubmit="check(this); return false;" enctype="multipart/form-data">
			<label class="input input-bordered flex items-center gap-2">
				<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 16 16"
					fill="currentColor" class="h-4 w-4 opacity-70">
	   				<path d="M8 8a3 3 0 1 0 0-6 3 3 0 0 0 0 6ZM12.735 14c.618 0 1.093-.561.872-1.139a6.002 6.002 0 0 0-11.215 0c-.22.578.254 1.139.872 1.139h9.47Z" />
	  			</svg> 
	  			<input maxlength="20" type="text" id="loginId" class="grow" placeholder="아이디" name="loginId"/>
	  			<div class="idCheck"></div>
			</label> 
			<label class="mt-1 input input-bordered flex items-center gap-2">
				<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 16 16"
					fill="currentColor" class="h-4 w-4 opacity-70">
	   				<path fill-rule="evenodd"
						d="M14 6a4 4 0 0 1-4.899 3.899l-1.955 1.955a.5.5 0 0 1-.353.146H5v1.5a.5.5 0 0 1-.5.5h-2a.5.5 0 0 1-.5-.5v-2.293a.5.5 0 0 1 .146-.353l3.955-3.955A4 4 0 1 1 14 6Zm-4-2a.75.75 0 0 0 0 1.5.5.5 0 0 1 .5.5.75.75 0 0 0 1.5 0 2 2 0 0 0-2-2Z"
						clip-rule="evenodd" />
	  			</svg> 
	  			<input maxlength="12" type="password" class="pw grow" placeholder="비밀번호" name="loginPw"/> 
	  			<button class="change" type="button"><i class="see fa-solid fa-eye"></i><i class="notSee hidden fa-solid fa-eye-slash"></i></button>
			</label>
			<label class="mt-1 input input-bordered flex items-center gap-2">
				<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 16 16"
					fill="currentColor" class="h-4 w-4 opacity-70">
	   				<path fill-rule="evenodd"
						d="M14 6a4 4 0 0 1-4.899 3.899l-1.955 1.955a.5.5 0 0 1-.353.146H5v1.5a.5.5 0 0 1-.5.5h-2a.5.5 0 0 1-.5-.5v-2.293a.5.5 0 0 1 .146-.353l3.955-3.955A4 4 0 1 1 14 6Zm-4-2a.75.75 0 0 0 0 1.5.5.5 0 0 1 .5.5.75.75 0 0 0 1.5 0 2 2 0 0 0-2-2Z"
						clip-rule="evenodd" />
	  			</svg> 
	  			<input maxlength="12" type="password" class="pw grow" placeholder="비밀번호 확인" name="loginPwRe"/> 
			</label>
			<label class="mt-1 input input-bordered flex items-center gap-2">
	  			<input maxlength="12" type="text" class="grow" placeholder="이름" name="name"/> 
			</label>
			<label class="mt-1 input input-bordered flex items-center gap-2">
	  			<input maxlength="12" type="text" id="nickname" class="grow" placeholder="닉네임" name="nickname"/> 
	  			<div class="nicknameCheck"></div>
			</label>
			<label class="mt-1 input input-bordered flex items-center gap-2">
	  			<input maxlength="13" type="text" id="cellphone" class="grow" placeholder="핸드폰번호" name="cellphone"/> 
	  			<div class="cellphoneCheck"></div>
			</label>
			<label class="mt-1 input input-bordered flex items-center gap-2">
	  			<input maxlength="30" type="text" id="email" class="grow" placeholder="이메일" name="email"/> 
	  			<div class="emailCheck"></div>
			</label>
			<br/>
			<div>회원 이미지 (128x128 사이즈까지 지원됩니다.)</div>
			<input id=upload type="file" name="file" value="/resource/images/defaultImg.png"/>
			<div id='preview' class="flex"> 기본 이미지 : &nbsp;<img class="h-16 w-16 rounded-full" src="/resource/images/defaultImg.png"> </div>
			
			<div class="mt-4 text-center">
				<div class="tooltip mx-4" data-tip="뒤로 가기">
					<button class="btn btn-outline btn-info" type="button" onclick="history.back();">
					<i class="fa-solid fa-arrow-left-long"></i>
					</button>
				</div>
				<div class="tooltip mx-4" data-tip="회원 가입">
					<button class="btn btn-outline btn-info"> 회원가입 </button>
				</div>
			</div>
		</form>
	</div>
</section>

<script>

	$(document).ready(function(){
		let idDupCheck = false;
		let cellphoneDupCheck = false;
		let emailDupCheck = false;
		let nicknameDupCheck = false;
	})
	
	// 입력란 공백체크
	function check(form) {
		let loginId = form.loginId.value.trim();
		let loginPw = form.loginPw.value.trim();
		let loginPwRe = form.loginPwRe.value.trim();
		let name = form.name.value.trim();
		let nickname = form.nickname.value.trim();
		let cellphone = form.cellphone.value.trim();
		let email = form.email.value.trim();
	
		if (loginId.length == 0) {
			alert('아이디는 필수입력 정보입니다.');
			form.loginId.focus();
			return;
		}
	
		if (loginPw.length == 0) {
			alert('비밀번호는 필수입력 정보입니다.');
			form.loginPw.focus();
			return;
		}
		
		if (loginPw != loginPwRe) {
			alert('비밀번호와 비밀번호 확인이 일치하지 않습니다.');
			form.loginPw.value = '';
			form.loginPwRe.value = '';
			form.loginPw.focus();
			return;
		}
		
		if (name.length == 0) {
			alert('이름은 필수입력 정보입니다.');
			form.name.focus();
			return;
		}
	
		if (nickname.length == 0) {
			alert('닉네임은 필수입력 정보입니다.');
			form.nickname.focus();
			return;
		}
		
		if (cellphone.length == 0) {
			alert('핸드폰번호는 필수입력 정보입니다.');
			form.cellphone.focus();
			return;
		}
	
		if (email.length == 0) {
			alert('이메일은 필수입력 정보입니다.');
			form.email.focus();
			return;
		}
		
		if (idDupCheck == true) {
			alert('이미 사용중인 아이디입니다.');
			form.loginId.focus();
			return;
		}

		if (nicknameDupCheck == true) {
			alert('이미 사용중인 별명입니다.');
			form.nickname.focus();
			return;
		}
		
		if (cellphoneDupCheck == true) {
			alert('이미 사용중인 핸드폰번호입니다.');
			form.cellphone.focus();
			return;
		}
		
		if (emailDupCheck == true) {
			alert('이미 사용중인 이메일입니다.');
			form.email.focus();
			return;
		}
	
		form.submit();
	}
	
	// 암호 보기/숨기기
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
	
	// 아이디 중복 체크
	$('#loginId').change(function() {
		if($('[name=memberForm] #loginId').val().trim().length == 0) {
			idDupCheck = true;
			$('.idCheck').html('');
			return;
		}
			
		$.ajax({
			url : 'idDupCheck',
			type : 'GET',
			data : {
				loginId : $('[name=memberForm] #loginId').val().trim()  
			},
			dataType : 'json',
			success : function(result) {
				if (result.data == true) {
					$('.idCheck').css('color', 'red');
					idDupCheck = true;
				} else {
					$('.idCheck').css('color', 'black');
					idDupCheck = false;
				}
				
				$('.idCheck').html(result.resultMsg);
			},
			error : function(xhr, status, error) {
				console.log(error);
			}
		})
	});
	
	// 별명 중복 체크
	$('#nickname').change(function() {
		
		if($('[name=memberForm] #nickname').val().trim().length == 0) {
			emailDupCheck = true;
			$('.nicknameCheck').html('');
			return;
		}
			
		$.ajax({
			url : 'nicknameDupCheck',
			type : 'GET',
			data : {
				nickname : $('[name=memberForm] #nickname').val().trim()  
			},
			dataType : 'json',
			success : function(result) {
				if (result.data == true) {
					$('.nicknameCheck').css('color', 'red');
					nicknameDupCheck = true;
				} else {
					$('.nicknameCheck').css('color', 'black');
					nicknameDupCheck = false;
				}
				
				$('.nicknameCheck').html(result.resultMsg);
			},
			error : function(xhr, status, error) {
				console.log(error);
			}
		})
	});
	
	// 핸드폰 중복 체크
	$('#cellphone').change(function() {
		
		if($('[name=memberForm] #cellphone').val().trim().length == 0) {
			cellphoneDupCheck = true;
			$('.cellphoneCheck').html('');
			return;
		}
		
		$.ajax({
			url : 'cellphoneDupCheck',
			type : 'GET',
			data : {
				cellphone : $('[name=memberForm] #cellphone').val().trim()  
			},
			dataType : 'json',
			success : function(result) {
				if (result.data == true) {
					$('.cellphoneCheck').css('color', 'red');
					cellphoneDupCheck = true;
				} else {
					$('.cellphoneCheck').css('color', 'black');
					cellphoneDupCheckDupCheck = false;
				}
				
				$('.cellphoneCheck').html(result.resultMsg);
			},
			error : function(xhr, status, error) {
				console.log(error);
			}
		})
	});
	
	// 이메일 중복 체크
	$('#email').change(function() {
		
		if($('[name=joinForm] #email').val().trim().length == 0) {
			emailDupCheck = true;
			$('.emailCheck').html('');
			return;
		}
			
		$.ajax({
			url : 'emailDupCheck',
			type : 'GET',
			data : {
				email : $('[name=joinForm] #email').val().trim()  
			},
			dataType : 'json',
			success : function(result) {
				if (result.data == true) {
					$('.emailCheck').css('color', 'red');
					idDupCheck = true;
				} else {
					$('.emailCheck').css('color', 'black');
					idDupCheck = false;
				}
				
				$('.emailCheck').html(result.resultMsg);
			},
			error : function(xhr, status, error) {
				console.log(error);
			}
		})
	});
	
	// 업로드 이미지 미리보기
	$('#upload').change(function(e) {
        var get_file = e.target.files;
        var image = $('<img>');
        
        image.attr('width', 32);
        image.attr('height', 32);

        var reader = new FileReader();
        reader.onload = (function(img) {
            return function(e) {
                img.attr('src', e.target.result);
            };
        })(image);

        if (get_file) {
            reader.readAsDataURL(get_file[0]);
        }

        $('#preview').empty().html('업로드 이미지 : &nbsp;');
        $('#preview').append(image);
    });
	
	// 배경 이미지 삭제
	$('body').css('--bgImage', `url('')`);

</script>

<%@ include file="../../common/foot.jsp"%>
