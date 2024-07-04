<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:set var="pageTitle" value="Main Page" />
  
	<section class="mt-8 text-lg text-center">
		<div class="flex justify-center">
			<table class="w-full table table-xl table-pin-rows table-pin-cols text-xl">
				<colgroup>
					<col width=150/>
					<col width=150/>
					<col/>
					<col width=220/>
				</colgroup>
				<tr>
					<td colspan="4" class="text-center">${festival.title }</td>
				</tr>
				<tr class="text-base">
					<td>${festival.eventSeq }</td>
					<td><i class="fa-solid fa-eye"></i> ????? </td>
					<td class="grow"></td>
					<td>${ festival.dataStnDt}</td>
				</tr>
				<tr class="text-base">
					<td>시작일 : ${festival.beginDt }</td>
					<td>종료일 : ${festival.endDt }</td>
				</tr>
				<tr class="">
					<td colspan="4" class="">${festival.contents }</td>
				</tr>
			</table>
		</div>
	</section>
	
	<button onclick="history.back()" class="btn btn-outline btn-info mt-4">뒤로 가기</button>	
	<!-- 12917  -->