<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:set var="pageTitle" value="게시글 수정" />

<%@ include file="../../common/head.jsp"%>
<%@ include file="../../common/toastUiLib.jsp"%>

	<section class="mt-8 text-lg">
		<div class="container mx-auto px-3">
			<form action="doModify" method="POST" onsubmit="check(this); return false;">
				<input type="hidden" value="${article.id }" name="id">
				<input type="hidden" value="" name="body">
				<input type="text" maxlength=100 class="input input-bordered w-full mb-4" name="title" value="${article.title }"></input>
				<div class="toast-ui-editor"><script type="text/x-template"> ${article.body } </script></div>
				<div class="tooltip" data-tip="뒤로 가기">
					<button class="btn btn-outline btn-info" type="button" onclick="history.back();">
					<i class="fa-solid fa-arrow-left-long"></i>
					</button>
				</div>
				
				<button class="mt-5 btn btn-outline btn-info">글 수정하기</button>
			</form>
		</div>
	</section>
	
<%@ include file="../../common/foot.jsp"%>
