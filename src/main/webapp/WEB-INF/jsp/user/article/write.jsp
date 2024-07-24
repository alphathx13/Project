<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:set var="pageTitle" value="게시글 작성" />

<%@ include file="../../common/head.jsp"%>
<%@ include file="../../common/toastUiLib.jsp"%>

	<section class="mt-8 text-lg">
		<div class="container mx-auto px-3">
			<form action="doWrite" method="GET" onsubmit="check(this); return false;">
				<input type="hidden" value="${boardId }" name="boardId">
				<input type="hidden" value="" name="body">
				<input type="text" maxlength=100 class="input input-bordered w-full mb-4" name="title" placeholder="글 제목"></input>
				<div class="toast-ui-editor"></div>
				<div class="tooltip" data-tip="뒤로 가기">
					<button class="btn btn-outline btn-info" onclick="history.back();" type="button">
					<i class="fa-solid fa-arrow-left-long"></i>
					</button>
				</div>
				<button class="mt-5 btn btn-outline btn-info">글 작성하기</button>
			</form>
		</div>
	</section>

<%@ include file="../../common/foot.jsp"%>


<!-- <textarea maxlength=300 class="textarea textarea-bordered textarea-lg w-full h-72" name="body" placeholder="글 내용"></textarea> -->