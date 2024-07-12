<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ include file="../../common/head.jsp" %>  

<c:set var="pageTitle" value="행사 소개" />

	<section class="mt-8 text-lg flex flex-col text-center">
		<div class="flex justify-center">
			<table class="w-full table table-xl table-pin-rows table-pin-cols text-xl">
				<colgroup>
					<col width=200/>
					<col width=200/>
					<col width=200/>
					<col width=200/>
					<col width=220/>
				</colgroup>
				<tr>
					<td colspan="5" class="text-center">${festival.title }</td>
				</tr>
				<tr class="text-base">
					<td>시작일 : ${festival.beginDt }</td>
					<td>종료일 : ${festival.endDt }</td>
					<td>시작시간 : ${festival.beginTm }</td>
					<td>종료시간 : ${festival.endTm }</td>
					<td><i class="fa-solid fa-eye"></i> ${festival.viewCount } </td>
				</tr>
				<tr>
					<td colspan="3">장소 : ${festival.placeCdNm }</td>
					<td colspan="2">세부장소 : ${festival.placeDetail }</td>
				</tr>
				<tr class="">
					<td colspan="4" class="">${festival.contents }</td>
				</tr>
			</table>
		</div>
		
		<!-- 카카오맵 -->
		<div id="map" style="width:600px; height:350px;"></div>
		<script>
			const kakaoKey = '${kakaoKey}';
		</script>
		<script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=${kakaoKey}&libraries=services,clusterer,drawing"></script>
		<script>
			var infowindow = new kakao.maps.InfoWindow({zIndex:1});
			
			var mapContainer = document.getElementById('map'), 
			    mapOption = { 
			        center: new kakao.maps.LatLng(36.351064, 127.379726), 
			        level: 3
			    };
			
			var map = new kakao.maps.Map(mapContainer, mapOption); 
			
			var ps = new kakao.maps.services.Places(); 
			var place = '${festival.placeCdNm}';
			
			if (place == '기타' || place == '대전시청 행사') {
				place = '${festival.placeDetail}';
			} else if (place == '도서관') {
				place = '대전광역시청 하늘도서관';
			}
			
			if (place.startsWith('대전시청 ')) {
				place = '대전시청';
			} else if (place == '대전예술의전당' || place == '유성온천') {
				place += ' ${festival.placeDetail}';
			} else if (place.startsWith('대전MBC')) {
				place = '대전MBC';
			} else if (place.startsWith('한밭도서관 별관')) {
				place = '한밭도서관 별관';
			} else if (place.startsWith('대전선사박물관')) {
				place = '대전선사박물관';
			} else if (place.startsWith('다온아트')) {
				place = '갤러리 다온';
			}
			
			if (place == '중구 문화원') {
				place += ' 뿌리홀';
			} 
		
			ps.keywordSearch(place, placesSearchCB); 
			
			function placesSearchCB (data, status, pagination) {
			    if (status === kakao.maps.services.Status.OK) {
			
			        var bounds = new kakao.maps.LatLngBounds();
		            displayMarker(data[0]);    
			        bounds.extend(new kakao.maps.LatLng(data[0].y, data[0].x));
			
			        map.setBounds(bounds);
			        map.setLevel(map.getLevel() + 2);
			    } 
			}
			
			function displayMarker(place) {
			    
			    var marker = new kakao.maps.Marker({
			        map: map,
			        position: new kakao.maps.LatLng(place.y, place.x) 
			    });
			
			    kakao.maps.event.addListener(marker, 'click', function() {
			        infowindow.setContent('<div style="padding:5px;font-size:12px;">' + place.place_name + '</div>');
			        infowindow.open(map, marker);
			    });
			}
			
		</script>
		
		<!-- 추천관련 -->
		<script>
			$(document).ready(function(){
				
				if (${rq.loginMemberNumber == 0 }) {
					$('.festivalLikeTooltip').attr('data-tip', '추천수');
					$('.festivalStar').addClass('fa-solid fa-splotch');
				} else {
					$.ajax({
						url : '../likePoint/likeCheck',
						type : 'GET',
						data : {
							relTypeCode : 'festival',
							relId : ${festival.eventSeq }
						},
						dataType : 'json',
						success : function(result) {
							if (result.resultCode == "S-1") {
								$('.festivalLikeTooltip').attr('data-tip', '추천취소');
								$('.festivalLikeBtn').html(`
										<i class="festivalStar star fa-solid fa-star"></i>
										<div class="festivalLikePoint">${festival.likePoint }</div>
										`);
							} else {
								$('.festivalLikeTooltip').attr('data-tip', '추천하기');
								$('.festivalLikeBtn').html(`
										<i class="festivalStar star fa-regular fa-star"></i>
										<div class="festivalLikePoint">${festival.likePoint }</div>
										`);
							}
						},
						error : function(xhr, status, error) {
							console.log(error);
						}
					})
				}
			})
			
			const festivalLikeBtnChange = function() {
				if(${rq.loginMemberNumber != 0}) {
					let likeCheck = true;
					if($('.festivalStar').hasClass('fa-regular')) {
						likeCheck = false;
					}
			
					$.ajax({
						url : '../likePoint/doLike',
						type : 'GET',
						data : {
							relTypeCode : 'festival',
							relId : ${festival.eventSeq },
							likeCheck : likeCheck
						},
						dataType : 'json',
						success : function(result) {
							$('.festivalLikePoint').text(result.data);
							if (result.resultCode == 'undoLike') {
								$('.festivalStar').attr('class','festivalStar star fa-regular fa-star');
								$('.festivalLikeTooltip').attr('data-tip', '추천하기');
							} else {
								$('.festivalStar').attr('class','festivalStar star fa-solid fa-star');
								$('.festivalLikeTooltip').attr('data-tip', '추천취소');
							}
						},
						error : function(xhr, status, error) {
							console.log(error);
						}
					})
				}
			}
		</script>

		<div class="h-16">
			<div class="festivalLikeTooltip tooltip w-20 h-full" data-tip="">
				<button class="festivalLikeBtn btn btn-outline w-full h-full text-xl" onclick = "festivalLikeBtnChange();" type="button">
					<i class="festivalStar"><div class="festivalLikePoint text-xl">${festival.likePoint }</div></i>
				</button>
			</div>
		</div>	
	
	
	
		<!-- 날씨표기 -->
		
		<div class="flex justify-center">
			<table class="table">
				<colgroup>
					<col width="60"/>
					<col width="60"/>
					<col width="60"/>
					<col width="60"/>
				</colgroup>
			    <thead>
		     		<tr>
		     			<td></td>
		     			<td>오늘</td>
		     			<td>내일</td>
		     			<td>모레</td>
		     		</tr>
		    	</thead>
		    	<tbody>
		    		<tr class="6시"></tr>
		   			<tr class="9시"></tr>
		   			<tr class="12시"></tr>
		   			<tr class="15시"></tr>
		   			<tr class="18시"></tr>
		    	</tbody>
			</table>
		</div>
		
		<div class="flex justify-center">
			<table class="table">
				<colgroup>
					<col width="60"/>
					<col width="60"/>
					<col width="60"/>
					<col width="60"/>
					<col width="60"/>
					<col width="60"/>
				</colgroup>
			    <thead>
		     		<tr class="midDate"><td></td></tr>
		    	</thead>
		    	<tbody>
		    		<tr class="temp"></tr>
		   			<tr class="am"></tr>
		   			<tr class="cloud"></tr>
		    	</tbody>
			</table>
		</div>
	</section>
	<script>
		$(document).ready(function(){
			
	  		var today = new Date();
	  		
	  		for (var i = 3; i <= 7; i++) {
	  		    var futureDate = new Date(today);
	  		    futureDate.setDate(today.getDate() + i);

	  		    var month = futureDate.getMonth() + 1;
	  		    var day = futureDate.getDate();

	  		  	$('.midDate').append(`<td>\${month}/\${day}</td>`);
	  		}
	  		
	  		// 단기 날씨
	  		$.ajax({
				url : '/user/weather/weatherShortView',
				type : 'GET',
				dataType : 'json',
				success : function(data) {
					var dataNum = 0;
					for (var num = 6; num <= 18; num+=3) {
						$('.' + num + '시').html(`
								<td>\${num}시</td>
								<td>\${data[dataNum].fcstValue}도/\${data[dataNum+2].fcstValue}%/\${data[dataNum+1].fcstValue}</td>
								<td>\${data[dataNum+15].fcstValue}도/\${data[dataNum+17].fcstValue}%/\${data[dataNum+16].fcstValue}</td>
								<td>\${data[dataNum+30].fcstValue}도/\${data[dataNum+32].fcstValue}%/\${data[dataNum+31].fcstValue}</td>
								`);
						dataNum += 3;
					}
				},
				error : function(xhr, status, error) {
					console.log(error);
				}
			})
	  		
			// 중기 날씨
			$.ajax({
				url : '/user/weather/weatherMidView',
				type : 'GET',
				dataType : 'json',
				success : function(data) {
					
					$('.temp').html(`
						<th> 최저/최고 온도</th>
						<th>\${data.taMin3}도 / \${data.taMax3}도</th>
						<th>\${data.taMin4}도 / \${data.taMax4}도</th>
						<th>\${data.taMin5}도 / \${data.taMax5}도</th>
						<th>\${data.taMin6}도 / \${data.taMax6}도</th>
						<th>\${data.taMin7}도 / \${data.taMax7}도</th>
							`);

					$('.am').html(`
						<th> 오전/오후 강수확률 </th>
						<th>\${data.rnSt3Am} % / \${data.rnSt3Pm} %</th>
						<th>\${data.rnSt4Am} % / \${data.rnSt4Pm} %</th>
						<th>\${data.rnSt5Am} % / \${data.rnSt5Pm} %</th>
						<th>\${data.rnSt6Am} % / \${data.rnSt6Pm} %</th>
						<th>\${data.rnSt7Am} % / \${data.rnSt7Pm} %</th>
							`);
					
					$('.cloud').html(`
						<th> 오전/오후 날씨 </th>
						<th>\${data.wf3Am} / \${data.wf3Pm}</th>
						<th>\${data.wf4Am} / \${data.wf4Pm}</th>
						<th>\${data.wf5Am} / \${data.wf5Pm}</th>
						<th>\${data.wf6Am} / \${data.wf6Pm}</th>
						<th>\${data.wf7Am} / \${data.wf7Pm}</th>
							`);
				},
				error : function(xhr, status, error) {
					console.log(error);
				}
			})

		})
	</script>
	
	
	<script>
		$(document).ready(function(){
			replyLoad('festival', ${festival.eventSeq });
			replyLikeLoad();
		})
		
		// 댓글 불러오기
		const replyLoad = function(relTypeCode, relId) {
			$.ajax({
				url : '../reply/viewReply',
				type : 'POST',
				data : {
					relTypeCode : relTypeCode,
					relId : relId
				},
				async: false,
				dataType : 'json',
				success : function(result) {
					let loginMemberNumber = ${rq.loginMemberNumber};
					let content = '';
					if (result.data.length != 0) {
						$('.reply').html(`
								<div class="container mx-auto px-3>
									<div class="">
										<table class="table table-lg">
											<colgroup>
												<col width="30"/>
												<col width="30"/>
												<col width=""/>
												<col width="200"/>
												<col width="10"/>
												<col width="10"/>
											</colgroup>
										<thead>
											<tr>
												<th>추천수</th>
												<th>작성자</th>
												<th>내용</th>
												<th>작성일시</th>
												<th></th>
												<th></th>
											</tr>
										</thead>
											<tbody class="replyList">
											</tbody>
										</table>
									</div>
								</div>
								`);
					
						$.each(result.data, function(index, item) {
							let date = item.updateDate.substr(5);
							content += `
									<tr>
										<td>
											<div class="replyLikeTooltip tooltip w-20 h-full" data-tip="">
												<button check="\${item.id}" class="replyLikeBtn btn btn-outline w-full h-full text-xl" onclick = "replyLikeBtnChange();" type="button">
													<i class="replyStar"><div class="replyLikePoint text-xl">\${item.likePoint }</div></i>
												</button>
											</div>
										</td>
										<td>\${item.nickname}</td>
										<td>
											<div class="\${item.id}R">\${item.body}	</div>
											<div class="\${item.id}"></div>
										</td>
										<td>\${date}
									`;
							if (item.regDate != item.updateDate) {
								content += `(수정됨)`;
							}
							content += `</td>`;
							$('.replyList').append('</td>');		
							if(item.memberId == loginMemberNumber ) {
								content += `<td><div class="tooltip" data-tip="댓글 수정"><button onclick="replyModify(\${item.id})"><i class="fa-solid fa-pen-to-square"></i></button></div></td>`;
								content += `<td><div class="tooltip" data-tip="댓글 삭제"><button onclick="replyDelete(\${item.id}); return false;"><i class="fa-solid fa-trash-can"></i></button></div></td>`;
							}
						})
						$('.replyList').append(content);
					}
				},
				error : function(xhr, status, error) {
					console.log(error);
				}
			})
		}
		
		// 댓글 좋아요 불러오기
		const replyLikeLoad = function(relTypeCode, relId) {
			if (${rq.loginMemberNumber == 0 }) {
				$('.replyLikeTooltip').attr('data-tip', '추천수');
				$('.replyStar').addClass('fa-solid fa-splotch');
			} else {
				$('.replyLikeBtn').each(function() {
					var reply = $(this);
					$.ajax({
						url : '../likePoint/likeCheck',
						type : 'GET',
						data : {
							relTypeCode : 'reply',
							relId : $(this).attr('check')
						},
						dataType : 'json',
						success : function(result) {
							if (result.resultCode == "S-1") {
								reply.html(`
										<i class="replyStar star fa-solid fa-star"></i>
										<div class="replyLikePoint">\${result.data }</div>
										`);
								reply.parent().attr('data-tip', '추천취소');
							} else {
								reply.html(`
										<i class="replyStar star fa-regular fa-star"></i>
										<div class="replyLikePoint">\${result.data }</div>
										`);
								reply.parent().attr('data-tip', '추천하기');
							}
						},
						error : function(xhr, status, error) {
							console.log(error);
						}
					})
				})
			}
		}
		
		// 댓글 좋아요 / 좋아요취소
		const replyLikeBtnChange = function() {
			var $button = $(event.target);
			
			if(${rq.loginMemberNumber != 0}) {
				let likeCheck = true;
				if($button.children('i.replyStar').hasClass('fa-regular')) {
					likeCheck = false;
				}
				
				$.ajax({
					url : '../likePoint/doLike',
					type : 'GET',
					data : {
						relTypeCode : 'reply',
						relId : $button.attr('check'),
						likeCheck : likeCheck
					},
					dataType : 'json',
					success : function(result) {
						$button.children('div.replyLikePoint').text(result.data);
						if (result.resultCode == 'undoLike') {
							$button.children('i.replyStar').attr('class','replyStar star fa-regular fa-star');
							$button.parent().attr('data-tip', '추천하기');
						} else {
							$button.children('i.replyStar').attr('class','replyStar star fa-solid fa-star');
							$button.parent().attr('data-tip', '추천취소');
						}
					},
					error : function(xhr, status, error) {
						console.log(error);
					}
				})
			}
			
			
		}
		
		// 댓글 수정란
		const replyModify = function(id) {
			let body;
			$.ajax({
				url : '../reply/getReplyBody',
				type : 'POST',
				data : {
					id : id
				},
				dataType : 'text',
				success : function(result) {
					body = result;
					if(!$('div.' + id).hasClass('replyModifyOpen')) {
						replyFormClose('replyModifyOpen');
						
						$('div.' + id + 'R').css('display', 'none');
						$('div.' + id).html(`
								<form class="replyForm" onsubmit="if(replyForm_onSubmit(this) == true) { if(confirm('댓글을 수정하시겠습니까?')) replySend(this);} return false;">
									<input type="hidden" name="id" value="\${id}"/>
									<textarea maxlength=300 class="textarea textarea-bordered textarea-lg w-full" name="body" placeholder="\${body}"></textarea>
									<div class="flex justify-end"><button class="btn btn-outline btn-sm">댓글 수정</button></div>
								</form>
								`);
						$('div.' + id).addClass('replyModifyOpen');
					} else {
						replyFormClose(id);
					}
				},
				error : function(xhr, status, error) {
					console.log(error);
				}
			})
		}
		
		const replyFormClose = function(id) {
			$('div.' + id).siblings().css('display', 'inline-block');
			$('div.' + id).html('');
			$('div.' + id).removeClass('replyModifyOpen');
		}
		
		// 댓글 수정
		function replySend(){
			let data = $(".replyForm").serialize();
			
			$.ajax({
				url : '../reply/replyModify',
				type : 'POST',
				data : data,
				dataType : 'json',
				success : function(result) {
					location.href='/user/festival/detail?eventSeq=${festival.eventSeq}';
				},
				error : function(xhr, status, error) {
					console.log(error);
				}
			})
		}
		
		// 댓글 삭제
		const replyDelete = function(id) {
			if(confirm('정말 삭제하시겠습니까?')) {
				location.href="/user/reply/replyDelete?id=" + id + "&eventSeq=" + ${festival.eventSeq};
			}
		}
		
		// 댓글 체크
		const replyForm_onSubmit = function(form){
			let body = form.body.value.trim();
		
			if (body.length == 0) {
				alert('비어있는 댓글은 작성할 수 없습니다');
				form.body.focus();
				return false;
			}
			
			return true;
		}
		
	</script>	
		
	<section class="reply mt-8 text-lg"></section>	
		
 	<c:if test="${rq.loginMemberNumber != 0 }">
		<div class="container mx-auto px-3">
			<form action="../reply/doWrite" method="post" onsubmit="if(replyForm_onSubmit(this)) { if(confirm('댓글을 작성하시겠습니까?')) form.submit();} return false;">
				<input type="hidden" name="relId" value="${festival.eventSeq }"/>
				<input type="hidden" name="relTypeCode" value="festival"/>
				<div class="mt-4 reply-border p-4 text-left">
					<div class="mb-2">${rq.loginMemberNn }</div>
					<textarea maxlength=300 class="textarea textarea-bordered textarea-lg w-full" name="replyBody" placeholder="댓글을 입력하세요."></textarea>
					<div class="flex justify-end"><button class="btn btn-outline btn-sm">댓글 작성</button></div>
				</div>
			</form>
		</div>
	</c:if>
	
	<script src="https://developers.kakao.com/sdk/js/kakao.js"></script>
	<script type="text/javascript">
		Kakao.init('${kakaoKey}');
		
		function kakaoShare() {
			Kakao.Link.sendDefault({
			  objectType: 'feed',
			  content: {
			    title: '${festival.title}',
			    description: '테마 : ${festival.themeCdNm} / 장소 : ${festival.placeCdNm}',
			    imageUrl:
			      'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQa6Lqd0DpY_4JKFhXxaZq0nt5iImKM5D2Gcg&s',
			    link: {
			      mobileWebUrl: 'https://www.naver.com',
			      webUrl: 'https://www.naver.com',
			    },
			  },

			  buttons: [
			    {
			      title: '웹으로 이동',
			      link: {
			        mobileWebUrl: 'https://www.naver.com',
			        webUrl: 'https://www.naver.com',
			      },
			    }
			  ],
			});
		}
	</script>
	
	<div>
		<a id="kakao-link-btn" href="javascript:kakaoShare()">
    	<img src="https://developers.kakao.com/assets/img/about/logos/kakaolink/kakaolink_btn_medium.png" />
    	</a>
    </div>
	
	<button onclick="history.back()" class="btn btn-outline btn-info mt-4">뒤로 가기</button>	
	
<%@ include file="../../common/foot.jsp" %>  

<!-- 

 -->
