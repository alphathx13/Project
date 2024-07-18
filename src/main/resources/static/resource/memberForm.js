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