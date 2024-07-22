<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:set var="pageTitle" value="비밀번호 찾기" />

<%@ include file="../../common/head.jsp" %>

	<script>
		const findLoginPwForm_onSubmit = function(form) {
			let loginId = form.loginId.value.trim();
			let email = form.email.value.trim();
			
			if (loginId.length == 0) {
				alert('아이디를 입력해주세요');
				form.loginId.focus();
				return;
			}
			
			if (email.length == 0) {
				alert('이메일을 입력해주세요');
				form.email.focus();
				return;
			}
			
			form.submit();
		}
	</script>

	<section class="mt-8 text-lg">
		<div class="container mx-auto px-3 pb-8">
			<form action="/user/member/doFindLoginPw" method="post" onsubmit="findLoginPwForm_onSubmit(this); return false;">
				<div class="table-box-type">
					<table class="table table-lg">
						<tr>
							<th>아이디</th>
							<td><input class="input input-bordered w-9/12" type="text" name="loginId" /></td>
						</tr>
						<tr>
							<th>이메일</th>
							<td><input class="input input-bordered w-9/12" type="text" name="email" /></td>
						</tr>
						<tr>
							<td colspan="2">
								<div class="btns flex justify-center">
									<button class="btn btn-active btn-wide">비밀번호 찾기</button>
								</div>
							</td>
						</tr>
					</table>
				</div>
			</form>

			<div class="mt-3 flex justify-between">
				<button class="btn btn-active btn-sm" onclick="history.back();">뒤로가기</button>
				<div>
					<a class="btn btn-active btn-sm" href="/user/member/findLoginId">아이디 찾기</a>
				</div>
			</div>
		</div>
	</section>
	
<%@ include file="../../common/foot.jsp" %>