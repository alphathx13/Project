<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:set var="pageTitle" value="회원 정보 수정" />

<%@ include file="../../common/head.jsp"%>

<section class = "ml-4">

	<div>${member.nickname }님 지금까지 저희 사이트를 이용해주셔서 감사합니다.</div>
	<div>사이트를 탈퇴하시는 이유에 대해서 적어주시면 더 좋은 사이트를 만드는데 도움이 됩니다.</div>
	<form action="checkWithdrawal" name="withdrawal" method="POST">
		<textarea maxlength=300 class="textarea textarea-bordered textarea-lg w-full" name="reason" placeholder="탈퇴 사유를 입력하세요."></textarea>
		<button class="btn btn-outline btn-info"> 회원 탈퇴 신청 </button>
	</form>
</section>

<%@ include file="../../common/foot.jsp"%>