<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ include file="../../common/head.jsp" %>      
	
	<section class="mt-8 text-lg text-center">
		<div class="container flex flex-col mx-auto px-3 w-3/5">
			<div class="list_top flex">
				<c:choose>
					<c:when test="${searchType != 0 }">
						<div>검색 결과 : ${articleCount }</div>
					</c:when>
					<c:otherwise>
						<div>게시글 : ${articleCount }</div>
					</c:otherwise>
				</c:choose>
				<div class ="grow"></div> 
				<c:if test="${articleCount != 0 }">
					<div>${cPage } 페이지 / ${tPage } 페이지 &nbsp;&nbsp;</div>
				</c:if>
				<div>
					<form action="" method="get"> 
						<input type="hidden" name ="boardId" value="${param.boardId }"/>
						<input type="hidden" name ="searchType" value="${param.searchType }"/>
						<input type="hidden" name ="searchText" value="${param.searchText }"/>
						<select class="" onchange="this.form.submit()" name="itemsInPage">
							<option value="" selected disabled> 페이지내 게시글 수
							<option value="10"> 10개
							<option value="20"> 20개
							<option value="30"> 30개
							<option value="50"> 50개
						</select>
					</form>
				</div>
			</div>
			
			<div class="flex">
				<table class="table">
					<colgroup>
						<col width="100"/>
						<col width=""/>
						<col width="80"/>
						<col width="200"/>
						<col width="60"/>
						<col width="60"/>
					</colgroup>
				    <thead>
			     		<tr>
			        		<th>글 번호</th>
			        		<th>글 제목</th>
					        <th>작성자</th>
					        <th>작성일시</th>
					        <th>조회수</th>
					        <th>추천수</th>
			   			</tr>
			    	</thead>
			    	<tbody>
			    		<c:forEach var="article" items="${articles }">
			      			<tr>
						        <td>${article.id } </td>
						        <td class="hover:underline"><a href="detail?id=${article.id }">${article.title } </a></td>
						        <td>
						          <div class="flex items-center gap-3">
						            <div class="avatar">
						              <div class="mask mask-squircle h-6 w-6">
						                <img src="/user/file/memberImg/${article.memberNumber }"/>
						              </div>
						            </div>
						            <div>
						              <div class="font-bold">${article.writerName }</div>
						            </div>
						          	</div>
								</td>
			        			<th>${article.regDate.substring(2, 16) }</th>
			        			<th>${article.viewCount }</th>
			        			<th><i class="fa-solid fa-star"></i> ${article.likePoint }</th>
			      			</tr>
			   			</c:forEach>  
			    	</tbody>
				</table>
			</div>
			
			<div class="mt-4 w-full flex justify-between">
				<div></div>
				<div>
					<form class = "flex" action="" method="get"> 
						<input type="hidden" name ="boardId" value="${param.boardId }"/>
						<input type="hidden" name ="itemsInPage" value="${param.itemsInPage }"/>
						<select data-value="${searchType }" class="select select-bordered h-4" name="searchType">
							<option value="" selected disabled> 검색항목 </option>
							<option value="1"> 제목 </option>
							<option value="2"> 내용 </option>
							<option value="3"> 제목+내용 </option>
						</select>
						&nbsp;
						<label class="input input-bordered flex items-center gap-2">
							<input maxlength="20" type="text" value="${searchText }" class="grow" name ="searchText" placeholder="Search" />
							<button>
							<svg
							    xmlns="http://www.w3.org/2000/svg"
							    viewBox="0 0 14 14"
							    fill="currentColor"
							    class="h-4 w-4 opacity-70">
							<path
						    	fill-rule="evenodd"
						    	d="M9.965 11.026a5 5 0 1 1 1.06-1.06l2.755 2.754a.75.75 0 1 1-1.06 1.06l-2.755-2.754ZM10.5 7a3.5 3.5 0 1 1-7 0 3.5 3.5 0 0 1 7 0Z"
						    	clip-rule="evenodd" />
						  	</svg>
						  	</button>
						</label>
					</form>
				</div>
				
				<c:choose>
					<c:when test="${rq.loginMemberNumber != 0 }">
						<div class="tooltip" data-tip="글 작성">
							<a class="btn btn-outline btn-info mr-4" href="write?boardId=${param.boardId}">
								<i class="fa-solid fa-pen"></i>
							</a>
						</div>
					</c:when>
					<c:otherwise>
						<div></div>
					</c:otherwise>
				</c:choose>
			</div>
			
			<div class="mt-4 mb-4 flex justify-center">
			
				<c:set var="baseUri" value="&boardId=${param.boardId}&itemsInPage=${itemsInPage}&searchType=${param.searchType}&searchText=${param.searchText}"/>
				
				<c:if test="${cPage > 5 and tPage > 9}">
					<a class="join-item btn btn-sm" href="?cPage=1${baseUri }"> 처음으로 </a> &nbsp;&nbsp;
				</c:if>
				<c:forEach begin="${startPage }" end="${endPage }" var="i">
					<a class="join-item btn btn-sm ${i == cPage? 'btn-active' : ''}"  href="?cPage=${i }${baseUri }"> ${i } </a>
				</c:forEach>
				<c:if test="${cPage < tPage - 4 and tPage > 9}">
					&nbsp;&nbsp;<a class="join-item btn btn-sm" href="?cPage=${tPage }${baseUri }"> 끝으로</a>&nbsp;&nbsp;
				</c:if>

				<form action="" method="get" onsubmit="pageCheck(this); return false;"> 
					<input type="hidden" name ="boardId" value="${param.boardId }"/>
					<input type="hidden" name ="itemsInPage" value="${itemsInPage }"/>
					<input type="hidden" name ="searchType" value="${searchType }"/>
					<input type="hidden" name ="searchText" value="${searchText }"/>
					<label class="input input-bordered flex items-center gap-2">
						<input maxlength="10" type="text" class="input grow" name ="cPage" placeholder="페이지" />
						<button> 이동 </button>
					</label>
				</form>
				
			</div>
		</div>
	</section>
	
	<script>

	function pageCheck(form) {
		let cPage = form.cPage.value.trim();

		if (cPage <= 0 || cPage > ${tPage}) {
			alert('해당 페이지는 존재하지 않습니다.');
			form.cPage.focus();
			return;
		}
		
		if (isNaN(cPage)) {
			alert('숫자만 입력해주세요.');
			form.cPage.focus();
			return;
		}
		
		form.submit();
	}
	
	// 배경 이미지 삭제
	$('body').css('--bgImage', `url('')`);
	</script>

	
<%@ include file="../../common/foot.jsp" %>  