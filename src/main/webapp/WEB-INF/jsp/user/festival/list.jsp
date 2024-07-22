<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ include file="../../common/head.jsp"%>

<c:set var="pageTitle" value="Main Page" />

<section class="mt-8 text-lg text-center">

	<div id="currentFestival"> 현재 진행중인 행사 </div> <br/>
	<div id="futureFestival"> 진행예정 행사 </div> <br/>
	<div id="pastFestival"> 종료된 행사 </div>
	
	<script>
	    function festivalTable(id, festivals) {
	        var html = `
	            <div class="container flex flex-col mx-auto px-3 w-3/5">
	                <div class="flex">
	                    <table class="table">
	                        <colgroup>
	                            <col width="80" />
	                            <col width="" />
	                            <col width="200" />
	                            <col width="200" />
	                            <col width="100" />
	                            <col width="100" />
	                        </colgroup>
	                        <thead>
	                            <tr>
	                                <th>행사 테마</th>
	                                <th>행사 제목</th>
	                                <th>시작 날짜</th>
	                                <th>종료 날짜</th>
	                                <th>추천수</th>
	                                <th>조회수</th>
	                            </tr>
	                        </thead>
	                        <tbody>
	        `;
	
	        $.each(festivals, function(index, festival) {
	            html += `
	                <tr>
	                    <td>\${festival.themeCdNm}</td>
	                    <td><a href="detail?eventSeq=\${festival.eventSeq}">\${festival.title}</a></td>
	                    <td>\${festival.beginDt}</td>
	                    <td>\${festival.endDt}</td>
	                    <td><i class="star fa-solid fa-star"></i> \${festival.likePoint}</td>
	                    <td>\${festival.viewCount}</td>
	                </tr>
	            `;
	        });
	
	        html += `
	                        </tbody>
	                    </table>
	                </div>
	            </div>
	        `;
	
	        $("#" + id).append(html);
	    }
	
 	    var currentFestival = ${currentFestival};
 	    var futureFestival = ${futureFestival}
 	    var pastFestival = ${pastFestival}; 
	    
 	    if (currentFestival.length === 0) {
 	        $("#currentFestival").append('<div class="text-red-500"> 현재 진행중인 행사가 없습니다. </div>');
 	    } else {
 	    	festivalTable('currentFestival', currentFestival);
 	    }
	
 	    if (futureFestival.length === 0) {
 	        $("#futureFestival").append('<div class="text-red-500"> 진행예정 행사가 없습니다. </div>');
 	    } else {
 	    	festivalTable('futureFestival', futureFestival);
 	    }
	
 	    if (pastFestival.length === 0) {
 	        $("#pastFestival").append('<div class="text-red-500"> 진행한 행사가 없습니다. </div>');
 	    } else {
 	    	festivalTable('pastFestival', pastFestival);
 	    }
	</script>

</section>

<%@ include file="../../common/foot.jsp"%>