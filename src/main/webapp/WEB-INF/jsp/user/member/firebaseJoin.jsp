<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ include file="../../common/head.jsp"%>

<section class="mt-8 text-lg">
	<div class="container mx-auto px-3">
		<form name="memberForm" action="firebaseCheckJoin" method="POST" onsubmit="check(this); return false;" enctype="multipart/form-data">
			<input type="hidden" name="email" value="${email }" />
			<input type="hidden" name="uid" value="${uid }" />
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
			이메일 : <label class="mt-1 input input-bordered flex items-center gap-2">
	  			<div>${email }</div> 
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
		let cellphoneDupCheck = false;
		let emailDupCheck = false;
		let nicknameDupCheck = false;
	})
	
	// 입력란 공백체크
	function check(form) {
		let name = form.name.value.trim();
		let nickname = form.nickname.value.trim();
		let cellphone = form.cellphone.value.trim();
	
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
		
		form.submit();
	}
	
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
	
	// 업로드 이미지 미리보기
	$('#upload').change(function(e) {
        var get_file = e.target.files;
        var image = $('<img>');
        
        image.attr('width', 64);
        image.attr('height', 64);

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

</script>

<%@ include file="../../common/foot.jsp"%>
