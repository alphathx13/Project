<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:set var="pageTitle" value="Main Page" />
  
<%@ include file="../../common/head.jsp" %>  

	<button onclick="location.href='../festival/festivalUpdate'" class="btn btn-outline btn-info mt-4">행사 가져오기</button>	
	<button onclick="location.href='../festival/list'" class="btn btn-outline btn-info mt-4">행사 보기</button>	
	<br/><br/>
	
	<button onclick="location.href='../weatherMidUpdate'" class="btn btn-outline btn-info mt-4">중기 날씨 가져오기</button>	
	<button onclick="location.href='/weatherShortUpdate'" class="btn btn-outline btn-info mt-4">단기 날씨 가져오기</button>	
	<br/><br/>
	
	<button onclick="location.href='/chat'" class="btn btn-outline btn-info mt-4">채팅방</button>	
	<br/><br/>
	
<%@ include file="../../common/foot.jsp" %>  
	