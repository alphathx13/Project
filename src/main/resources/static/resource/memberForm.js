// 별명 중복 체크
$('#nickname').change(function() {
	
	if($('[name=memberForm] #nickname').val().trim() == '${member.nickname }') {
		$('.nicknameCheck').html('');
		emailDupCheck = false;
		return;
	}
	
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
	
	if($('[name=memberForm] #cellphone').val().trim() == '${member.cellphone }') {
		$('.cellphoneCheck').html('');
		cellphoneDupCheck = false;
		return;
	}
	
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
	
	if($('[name=memberForm] #email').val().trim() == '${member.email }') {
		$('.emailCheck').html('');
		emailDupCheck = false;
		return;
	}
	
	if($('[name=memberForm] #email').val().trim().length == 0) {
		emailDupCheck = true;
		$('.emailCheck').html('');
		return;
	}
		
	$.ajax({
		url : 'emailDupCheck',
		type : 'GET',
		data : {
			email : $('[name=memberForm] #email').val().trim()  
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