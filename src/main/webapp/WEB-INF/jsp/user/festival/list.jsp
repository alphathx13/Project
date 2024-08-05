<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ include file="../../common/head.jsp"%>

<style>
	.custom-bg {
	    background-image: url('/resource/images/festivalListMainImage.jpg');
	}
</style>

<section class="mx-56 mt-8 text-lg text-center">
	<div style="height: 400px;" class="visual custom-bg flex flex-col text-white">
		<div class="grow"></div>
		<div class="text-6xl mb-4 outfitFont"> 즐거운 문화생활의 시작</div>
	</div>
	
	<div class="mt-4 flex justify-end">
		<div class="tooltip w-8 font-bold text-center" data-tip="갤러리 형태로 보기">
			<button class="galleryIcon" onclick="galleryMode('change')" type="button"><img class="img h-8" src=""></button>
		</div>
		<div class="w-4"></div>
		<div class="tooltip w-8 font-bold text-center" data-tip="리스트 형태로 보기">
			<button class="listIcon" onclick="listMode('change')" type="button"><img class="img h-8" src=""></button>
		</div>
	</div>

	<div class="font-bold text-blue-500 text-2xl text-left mb-4">- 현재 진행중인 행사
		<c:if test="${currentFestivalCount != 0}">
			<span> : ${currentFestivalCount }개 </span>
		</c:if>
	</div>
	<div id="currentFestival"></div>
	<br/><br/>

	<div class="font-bold text-purple-500 text-2xl text-left mb-4 ">- 진행예정 행사
		<c:if test="${futureFestivalCount != 0}">
			<span> : ${futureFestivalCount }개 </span>
		</c:if>
	</div>
	<div id="futureFestival"></div>
	<br/><br/>
	
	<div class="font-bold text-red-500 text-2xl text-left mb-4">- 종료된 행사</div>
	<div id="pastFestival"></div>
	
	<div class="search text-black mt-4">
		<form class = "flex justify-center" action="" method="get" onsubmit="emptyCheck(this); return false;"> 
			<select data-value="${searchType }" class="select select-bordered h-4 mr-1" name="searchType">
				<option value="" selected disabled> 검색항목 </option>
				<option value="1"> 제목 </option>
				<option value="2"> 내용 </option>
				<option value="3"> 제목+내용 </option>
			</select>
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
		        html += `<button class="btn btn-outline w-full" onclick="viewMoreFestivalList('\${id}');" type="button"> 더보기 </button>`;
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
	 	        $('#currentFestival').html(`<div class="flex justify-center"><img class="h-32" src="/resource/images/festivalListX.png" alt="/resource/images/imageLoadingError.png" /></div><div class="text-red-500 text-3xl"> 현재 진행중인 행사가 없습니다. </div>`);
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
 	    	var html = `<div class="container gallery flex flex-wrap mx-auto">`;
	            
	        $.each(festivals, function(index, festival) {
	        	if (index < show) {
	        		html += `<span class="flex flex-col basis-1/3 px-4 mb-4"><span class="border border-gray-400 rounded-2xl">`;
		        	
		        	if (index % 6 == 0) {
		        		html += `<a href="detail?eventSeq=\${festival.eventSeq}"><img class="rounded-2xl" src="/resource/festivalTempImg/festivalTempImg1.jpg" />`;
		        	} else if (index % 6 == 1) {
		        		html += `<a href="detail?eventSeq=\${festival.eventSeq}"><img class="rounded-2xl" src="/resource/festivalTempImg/festivalTempImg2.jpg" />`;
		        	} else if (index % 6 == 2) {
		        		html += `<a href="detail?eventSeq=\${festival.eventSeq}"><img class="rounded-2xl" src="/resource/festivalTempImg/festivalTempImg3.jpg" />`;
		        	} else if (index % 6 == 3) {
		        		html += `<a href="detail?eventSeq=\${festival.eventSeq}"><img class="rounded-2xl" src="/resource/festivalTempImg/festivalTempImg4.jpg" />`;
		        	} else if (index % 6 == 4) {
		        		html += `<a href="detail?eventSeq=\${festival.eventSeq}"><img class="rounded-2xl" src="/resource/festivalTempImg/festivalTempImg5.jpg" />`;
		        	} else {
		        		html += `<a href="detail?eventSeq=\${festival.eventSeq}"><img class="rounded-2xl" src="/resource/festivalTempImg/festivalTempImg6.jpg" />`;
		        	}
		        	html += `<br/><span class="mt-3 text-2xl">\${festival.title}</span><br/>`
		        	
		        	if(festival.beginDt == festival.endDt) {
		        		html += `<span class="text-xl"><i class="fa-regular fa-clock"></i> \${festival.beginDt}</span>`;
		        	} else {
		        		html += ` <span class="text-xl"><i class="fa-regular fa-clock"></i> \${festival.beginDt} ~ \${festival.endDt} </span> `;
		        	}
		        	
		        	html += `
	        			<span class="mt-3 mb-3 text-xl flex justify-center"><span><span><i class="fa-solid fa-eye">&nbsp;\${festival.viewCount}</i><i class="ml-4 fa-solid fa-star">&nbsp;\${festival.likePoint}</i></span></span></span></a></span>
	        			</span>`;
        			
	        	} else {
			        return false;
	        	}
	        })
	        
	        if (festivals.length > show) {
		        html += `<button class="btn btn-outline w-full" onclick="viewMoreFestivalGallery('\${id}');" type="button"> 더보기 </button>`;
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
 	 	        $("#currentFestival").html(`<div class="flex justify-center"><img class="h-32" src="/resource/images/festivalListX.png" alt="/resource/images/imageLoadingError.png" /></div><div class="text-red-500 text-3xl">  현재 진행중인 행사가 없습니다. </div>`);
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
 		
 		// 배경 이미지 삭제
 		$('body').css('--bgImage', `url('')`);
 		
	</script>

</section>

<%@ include file="../../common/foot.jsp"%>