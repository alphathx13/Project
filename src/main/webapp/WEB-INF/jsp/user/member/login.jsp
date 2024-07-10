<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:set var="pageTitle" value="로그인" />

<%@ include file="../../common/head.jsp"%>

<section class="mt-8 text-lg text-center">
	<div class="container mx-auto px-3 w-72">
		<div>
			<form action="doWrite" method="GET" onsubmit="check(this); return false;">
			<input type="hidden" value="${boardId }" name="boardId">
			<label class="input input-bordered flex items-center gap-2">
				아이디 : <input type="text" class="grow" name="title"
				placeholder="${article.title } " />
			</label> <label class="input input-bordered flex items-center gap-2">
				비밀번호 : <input type="text" class="grow h-20" name="body"
				placeholder="${article.body }" />
			</label>
			<div class="tooltip" data-tip="뒤로 가기">
				<button class="btn btn-outline btn-info" onclick="history.back();" type="button">
				<i class="fa-solid fa-arrow-left-long"></i>
				</button>
			</div>
			<button class="mt-5 btn btn-outline btn-info">회원가입</button>
		</form>
		</div>
	</div>
</section>

<%@ include file="../../common/foot.jsp"%>