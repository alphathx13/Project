<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ include file="../../common/head.jsp"%>

<c:set var="pageTitle" value="Main Page" />

<section class="mx-56 mt-8 text-lg text-center">

	<div class="flex justify-end">
		<button class="listIcon" onclick="listMode('change')" type="button"><img class="img" src=""></button>
		<button class="galleryIcon ml-4" onclick="galleryMode('change')" type="button"><img class="img" src=""></button>
	</div>

	<div class="font-bold text-blue-500">현재 진행중인 행사
		<c:if test="${currentFestivalCount != 0}">
			<span> ${currentFestivalCount }개 </span>
		</c:if>
	</div>
	<div id="currentFestival"></div>
	<br/><br/>

	<div class="font-bold text-purple-500">진행예정 행사
		<c:if test="${futureFestivalCount != 0}">
			<span> ${futureFestivalCount }개 </span>
		</c:if>
	</div>
	<div id="futureFestival"></div>
	<br/><br/>
	
	<div class="font-bold text-red-500">종료된 행사</div>
	<div id="pastFestival"></div>
	
	<div>
		<form class = "flex justify-center" action="" method="get" onsubmit="emptyCheck(this); return false;"> 
			<select data-value="${searchType }" class="select select-bordered h-4" name="searchType">
				<option value="" selected disabled> 검색항목 </option>
				<option value="1"> 제목 </option>
				<option value="2"> 내용 </option>
				<option value="3"> 제목+내용 </option>
			</select>
			&nbsp;
			<input type="hidden" value="helloWorld" name ="test"/>
			<label class="input input-bordered flex items-center gap-2">
				<input maxlength="20" type="text" value="${searchText }" class="grow" name ="searchText" placeholder="Search" />
				<button>
					<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 14 14" fill="currentColor" class="h-4 w-4 opacity-70">
						<path fill-rule="evenodd" d="M9.965 11.026a5 5 0 1 1 1.06-1.06l2.755 2.754a.75.75 0 1 1-1.06 1.06l-2.755-2.754ZM10.5 7a3.5 3.5 0 1 1-7 0 3.5 3.5 0 0 1 7 0Z"  clip-rule="evenodd" />
				  	</svg>
			  	</button>
			</label>
		</form>
	</div>

	<script>
		// 전역으로 사용할 데이터
	    var currentFestival;
 	    var currentFestivalview ;
 	    var futureFestival;
 	    var futureFestivalview;
 	    var pastFestival; 
 	    var pastFestivalview;
 	    var mode;
 	    
		// 초기 행사데이터 설정
		$(document).ready(function() {
			
			$.ajax({
				url : 'listMode',
				type : 'GET',
				dataType : 'text',
				success : function(result) {
					mode = result;
					
					if (mode == 'list') {
						listMode('');
					} else {
						galleryMode('');
					}
					
				},
				error : function(xhr, status, error) {
					console.log(error);
				}
			})
		})
		
		// 행사 리스트형
	    function viewFestivalList(id, festivals, show) {
	        var html = `
	            <div class="container flex flex-col mx-auto px-3">
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
	                        <tbody>`;
	
	        $.each(festivals, function(index, festival) {
	        	if (index < show) {
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
	        	} else {
			        return false;
	        	}
	        });
	
	        html += `
	                        </tbody>
	                    </table>
	                </div>
	            </div>
	        `;
	        
	        if (festivals.length > show) {
		        html += `<button class="btn btn-outline" onclick="viewMoreFestivalList('\${id}');" type="button"> 더보기 </button>`;
	        }
	
	        $("#" + id).html(html);
	    }
		
		// 행사 리스트형 추가보여주기
	    function viewMoreFestivalList(id) {
	    	if (id == 'currentFestival') {
	    		currentFestivalview = currentFestivalview + 3;
	    		viewFestivalList('currentFestival', currentFestival, currentFestivalview);
	    	} else if (id == 'futureFestival') {
	    		futureFestivalview = futureFestivalview + 3;
	    		viewFestivalList('futureFestival', futureFestival, futureFestivalview);
	    	} else {
	    		pastFestivalview = pastFestivalview + 3;
	    		viewFestivalList('pastFestival', pastFestival, pastFestivalview);
	    	}
        }
		
		// 행사 리스트형 초기
		function listMode(mode) {
			$('.listIcon').find('.img').attr('src', '/resource/images/icon_list_on.png');
			$('.galleryIcon').find('.img').attr('src', '/resource/images/icon_gallery.png');
			
 			if (mode == 'change') 
 				cookieChange();
			
		    currentFestival = ${currentFestival};
	 	    currentFestivalview = 6;
	 	    futureFestival = ${futureFestival}
	 	    futureFestivalview = 6;
	 	    pastFestival = ${pastFestival}; 
	 	    pastFestivalview = 6;
	 	    
	 	    if (currentFestival.length === 0) {
	 	        $('#currentFestival').html(`<div class="flex justify-center"><img class="h-32" src="/resource/images/festivalListX.png" alt="/resource/images/imageLoadingError.png" /></div><div class="text-red-500"> 현재 진행중인 행사가 없습니다. </div>`);
	 	    } else {
	 	    	viewFestivalList('currentFestival', currentFestival, currentFestivalview);
	 	    }
		
	 	    if (futureFestival.length === 0) {
	 	        $('#futureFestival').html(`<div class="flex justify-center"><img class="h-32" src="/resource/images/festivalListX.png" alt="/resource/images/imageLoadingError.png" /></div><div class="text-red-500"> 진행예정 행사가 없습니다. </div>`);
	 	    } else {
	 	    	viewFestivalList('futureFestival', futureFestival, futureFestivalview);
	 	    }
		
	 	    if (pastFestival.length === 0) {
	 	        $('#pastFestival').html(`<div class="flex justify-center"><img class="h-32" src="/resource/images/festivalListX.png" alt="/resource/images/imageLoadingError.png" /></div><div class="text-red-500"> 진행한 행사가 없습니다. </div>`);
	 	    } else {
	 	    	viewFestivalList('pastFestival', pastFestival, pastFestivalview);
	 	    }
		}

		// 행사 갤러리형
 	    function viewFestivalGallery(id, festivals, show) {
 	    	var html = `<div class="container gallery flex flex-wrap mx-auto px-3">`;
	            
	        $.each(festivals, function(index, festival) {
	        	if (index < show) {
	        		html += `<span class="flex flex-col basis-1/3 px-4 mb-4">`;
		        	
		        	if (festival.themeCdNm == '공연') {
		        		html += `<a href="detail?eventSeq=\${festival.eventSeq}"><img class="rounded-2xl" src="/resource/images/festival.jpg" /></a>`;
		        	} else if (festival.themeCdNm == '전시') {
		        		html += `<a href="detail?eventSeq=\${festival.eventSeq}"><img class="rounded-2xl" src="/resource/images/exhibition.jpg" /></a>`;
		        	} else {
		        		html += `<a href="detail?eventSeq=\${festival.eventSeq}"><img class="rounded-2xl" src="/resource/images/etc.jpg" /></a>`;
		        	}
		        	html += `<span>\${festival.title}</span>
		        			<span>시작일 : \${festival.beginDt}</span>
		        			<span>종료일 : \${festival.endDt}</span>
		        			</span>`;
	        	} else {
			        return false;
	        	}
	        })
	        
	        if (festivals.length > show) {
		        html += `<button class="btn btn-outline" onclick="viewMoreFestivalGallery('\${id}');" type="button"> 더보기 </button>`;
	        }
	            
	        $("#" + id).html(html);
		}
 	    
 		// 행사 갤러리형 추가보여주기
 		function viewMoreFestivalGallery(id) {
	    	if (id == 'currentFestival') {
	    		currentFestivalview = currentFestivalview + 3;
	    		viewFestivalGallery('currentFestival', currentFestival, currentFestivalview);
	    	} else if (id == 'futureFestival') {
	    		futureFestivalview = futureFestivalview + 3;
	    		viewFestivalGallery('futureFestival', futureFestival, futureFestivalview);
	    	} else {
	    		pastFestivalview = pastFestivalview + 3;
	    		viewFestivalGallery('pastFestival', pastFestival, pastFestivalview);
	    	}
        }
 	    
 		 // 행사 갤러리형 초기
 	    function galleryMode(mode) {
			$('.listIcon').find('.img').attr('src', '/resource/images/icon_list.png');
			$('.galleryIcon').find('.img').attr('src', '/resource/images/icon_gallery_on.png');
 			 
 			if (mode == 'change') 
 				cookieChange();
 			
		    currentFestival = ${currentFestival};
	 	    currentFestivalview = 6;
	 	    futureFestival = ${futureFestival}
	 	    futureFestivalview = 6;
	 	    pastFestival = ${pastFestival}; 
	 	    pastFestivalview = 6;
 	    	
 	    	if (currentFestival.length === 0) {
 	 	        $("#currentFestival").html(`<div class="flex justify-center"><img class="h-32" src="/resource/images/festivalListX.png" alt="/resource/images/imageLoadingError.png" /></div><div class="text-red-500">  현재 진행중인 행사가 없습니다. </div>`);
 	 	    } else {
 	 	    	viewFestivalGallery('currentFestival', currentFestival, currentFestivalview);
 	 	    }
 		
 	 	    if (futureFestival.length === 0) {
 	 	        $("#futureFestival").html(`<div class="flex justify-center"><img class="h-32" src="/resource/images/festivalListX.png" alt="/resource/images/imageLoadingError.png" /></div><div class="text-red-500">  진행예정 행사가 없습니다. </div>`);
 	 	    } else {
 	 	    	viewFestivalGallery('futureFestival', futureFestival, futureFestivalview);
 	 	    }
 		
 	 	    if (pastFestival.length === 0) {
 	 	        $("#pastFestival").html(`<div class="flex justify-center"><img class="h-32" src="/resource/images/festivalListX.png" alt="/resource/images/imageLoadingError.png" /></div><div class="text-red-500">  진행한 행사가 없습니다. </div>`);
 	 	    } else {
 	 	    	viewFestivalGallery('pastFestival', pastFestival, pastFestivalview);
 	 	    }
 	 	    
 	    }
 		
 		 
 		// 쿠키 변경
 		function cookieChange() {
 			$.ajax({
				url : 'listMode',
				type : 'GET',
				data : {
					change : 'true'
				},
				dataType : 'text',
				success : function(result) {
				},
				error : function(xhr, status, error) {
					console.log(error);
				}
			})
 		}
 	    
 		// 행사 검색 공백체크
 		function emptyCheck(form) {
 			let searchType = form.searchType.value;
			let searchText = form.searchText.value.trim();
	
			if (searchText.length == 0) {
				alert('검색하실 내용을 입력해 주세요.');
				form.searchText.focus();
				return;
			}
			
			if (searchType == 0) {
				alert('검색하실 항목을 선택해주세요');
				form.searchType.focus();
				return;
			}
			
			form.submit();
		}
	</script>

</section>

<%@ include file="../../common/foot.jsp"%>