<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<script src="https://cdnjs.cloudflare.com/ajax/libs/gsap/3.11.3/gsap.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/gsap/3.11.3/ScrollTrigger.min.js"></script>
  
<%@ include file="../../common/head.jsp" %>  

	<div class="font-bold text-3xl bg-purple-500">
		<button onclick="location.href='../festival/festivalUpdate'" class="btn btn-outline btn-info mt-4">행사 가져오기</button>	
		<button onclick="location.href='../festival/list'" class="btn btn-outline btn-info mt-4">행사 보기</button>	
		<button onclick="location.href='/weatherMidUpdate'" class="btn btn-outline btn-info mt-4">중기 날씨 가져오기</button>	
		<button onclick="location.href='/weatherShortUpdate'" class="btn btn-outline btn-info mt-4">단기 날씨 가져오기</button>
	</div>
    
      
<%@ include file="../../common/foot.jsp" %>  
