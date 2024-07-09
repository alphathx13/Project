<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:set var="pageTitle" value="Main Page" />
  
	<section class="mt-8 text-lg text-center">
		<div class="container flex flex-col mx-auto px-3 w-3/5">
			<div class="flex">
				<table class="table">
					<colgroup>
						<col width="10"/>
						<col width="10"/>
						<col width=""/>
						<col width="200"/>
						<col width="200"/>
					</colgroup>
				    <thead>
			     		<tr>
			        		<th>행사 번호</th>
			        		<th>행사 내용</th>
					        <th>행사 제목</th>
					        <th>시작 날짜</th>
					        <th>종료 날짜</th>
			   			</tr>
			    	</thead>
			    	<tbody>
			    		<c:forEach var="festival" items="${festivalList }">
			      			<tr>
						        <td>${festival.eventSeq } </td>
						        <td>${festival.themeCdNm } </td>
						        <td><a href="/festivalDetail?eventSeq=${festival.eventSeq}">${festival.title }</a></td>
			        			<td>${festival.beginDt }</td>
			        			<td>${festival.endDt }</td>
			      			</tr>
			   			</c:forEach>  
			    	</tbody>
				</table>
			</div>
		</div>
	</section>
