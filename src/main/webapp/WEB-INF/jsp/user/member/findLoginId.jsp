<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:set var="pageTitle" value="아이디 찾기" />

<%@ include file="../../common/head.jsp" %>

	<script>
		const findLoginId_onSubmit = function(form) {
			let name = form.name.value.trim();
			let cellphone = form.cellphone.value.trim();
			let email = form.email.value.trim();
			
			if (form.name.value.length == 0) {
				alert('이름을 입력해주세요');
				form.name.focus();
				return;
			}
			
			if (form.cellphone.value.length == 0) {
				alert('전화번호를 입력해주세요');
				form.cellphone.focus();
				return;
			}
			
			if (form.email.value.length == 0) {
				alert('이메일을 입력해주세요');
				form.email.focus();
				return;
			}
			
			form.submit();
		}
	</script>

	<section class="mt-8 text-lg">
		<div class="container mx-auto px-3 pb-8">
			<form action="doFindLoginId" method="post" onsubmit="findLoginId_onSubmit(this); return false;">
				<div class="table-box-type">
					<table class="table table-lg">
						<tr>
							<th>이름</th>
							<td><input class="input input-bordered w-9/12" type="text" name="name" /></td>
						</tr>
						<tr>
							<th>전화번호</th>
							<td><input class="input input-bordered w-9/12" type="text" name="cellphone" /></td>
						</tr>
						<tr>
							<th>이메일</th>
							<td><input class="input input-bordered w-9/12" type="text" name="email" /></td>
						</tr>
						<tr>
							<td colspan="2">
								<div class="btns flex justify-center">
									<button class="btn btn-active btn-wide">아이디 찾기</button>
								</div>
							</td>
						</tr>
					</table>
				</div>
			</form>

			<div class="mt-3 flex justify-between">
				<button class="btn btn-active btn-sm" onclick="history.back();">뒤로가기</button>
				<div>
					<a class="btn btn-active btn-sm" href="findLoginPw">비밀번호 찾기</a>
				</div>
			</div>
		</div>
	</section>
	
	<script>
		// 배경 이미지 삭제
		$('body').css('--bgImage', `url('')`);
	</script>
<%@ include file="../../common/foot.jsp" %>