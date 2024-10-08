<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ include file="../../common/head.jsp" %>  



<c:set var="pageTitle" value="행사 소개" />

	<section class="mt-8 mx-56 text-lg flex flex-col border border-gray-300 rounded-xl ownglyphFont">
		<div class="flex justify-center">
			<table class="w-full table table-xl table-pin-rows table-pin-cols text-xl">
				<colgroup>
					<col width=/>
					<col width=/>
					<col width=/>
				</colgroup>
				<tr>
					<td colspan="5" class="text-center text-2xl font-bold">${festival.title }</td>
				</tr>
				<tr class="text-xl font-bold">
					<td>
						<c:choose>
						    <c:when test="${festival.beginDt == festival.endDt }">
						    	행사일 : ${festival.beginDt }
						    </c:when> 
						    <c:otherwise>
						    	행사기간 : ${festival.beginDt } ~ ${festival.endDt }
						    </c:otherwise>
						</c:choose>
					</td>
					<td>행사시간 : ${festival.beginTm } ~ ${festival.endTm }</td>
					<td><i class="fa-solid fa-eye"></i> ${festival.viewCount } </td>
				</tr>
				<tr class="text-xl font-bold">
					<td colspan="3">장소 : ${festival.placeCdNm }</td>
					<td colspan="2">
						<c:if test="${festival.placeDetail != null }">
							세부장소 : ${festival.placeDetail }
						</c:if>
					</td>
				</tr>
				<tr class="">
			    	<td colspan="4" class="">${festival.contents }</td>
				</tr>
			</table>
		</div>
		
		<br/><br/>
		
		<!-- 카카오맵 -->
		<div class="flex flex-col items-center">
			<span class="font-bold"> 찾아오시는 길</span>
			<div id="map" class="border border-black rounded-xl" style="width:600px; height:350px;"></div>
		</div>
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
		
		<!-- 행사 추천, 카카오톡 링크 -->
		<div class="mt-8 h-16 flex justify-center">
			<div class="festivalLikeTooltip tooltip w-20 h-full" data-tip="">
				<button class="festivalLikeBtn btn btn-outline w-full h-full text-xl" onclick = "festivalLikeBtnChange();" type="button">
					<i class="festivalStar"><div class="festivalLikePoint text-xl">${festival.likePoint }</div></i>
				</button>
			</div>
			<div class="ml-8 tooltip" data-tip="카카오톡 공유하기">
				<a id="kakao-link-btn" href="javascript:kakaoShare()">
		    	<img class="w-16" src="https://developers.kakao.com/assets/img/about/logos/kakaolink/kakaolink_btn_medium.png" />
		    	</a>
		    </div>
		</div>	
		<script src="https://developers.kakao.com/sdk/js/kakao.js"></script>
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
		
			Kakao.init('${kakaoKey}');
			const currentUrl = window.location.href;
			
			function kakaoShare() {
				Kakao.Link.sendDefault({
				  objectType: 'feed',
				  content: {
				    title: '${festival.title}',
				    description: '테마 : ${festival.themeCdNm} / 장소 : ${festival.placeCdNm}',
				    imageUrl:
				      'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQa6Lqd0DpY_4JKFhXxaZq0nt5iImKM5D2Gcg&s',
				    link: {
				      mobileWebUrl: currentUrl,
				      webUrl: currentUrl,
				    },
				  },
	
				  buttons: [
				    {
				      title: '웹으로 이동',
				      link: {
				        mobileWebUrl: currentUrl,
				        webUrl: currentUrl,
				      },
				    }
				  ],
				});
				
			}
		</script>
	
	
	
		<!--  댓글 기능 -->		
		<section class="reply mt-8 text-lg"></section>	
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
													<col width="300"/>
													<col width=""/>
													<col width="200"/>
													<col width="10"/>
													<col width="10"/>
												</colgroup>
											<thead>
												<tr class="text-xl font-bold text-center">
													<th>댓글 추천</th>
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
													<button check="\${item.id}" class="replyLikeBtn btn btn-outline h-full text-xl" onclick = "replyLikeBtnChange();" type="button">
														<i class="replyStar"><div class="replyLikePoint text-xl">\${item.likePoint }</div></i>
													</button>
												</div>
											</td>
											<td><span class="flex text-xl items-center font-bold"><img class="w-10 h-10 rounded" src="/user/file/memberImg/\${item.memberId}"/> &nbsp;&nbsp; \${item.nickname}</td></span>
											<td>
												<div class="\${item.id}R text-xl">\${item.body}	</div>
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
				
				console.log('hell');
				let body = form.body.value.trim();
			
				if (body.length == 0) {
					alert('비어있는 댓글은 작성할 수 없습니다');
					form.body.focus();
					return false;
				}
				
				return true;
			}
			
		</script>	
		
	 	<c:if test="${rq.loginMemberNumber != 0 }">
			<div class="container mx-auto px-3">
				<form action="../reply/doWrite" method="post" onsubmit="if(replyForm_onSubmit(this)) { if(confirm('댓글을 작성하시겠습니까?')) form.submit();} return false;">
					<input type="hidden" name="relId" value="${festival.eventSeq }"/>
					<input type="hidden" name="relTypeCode" value="festival"/>
					<div class="mt-4 reply-border p-4 text-left">
						<div class="mb-2 font-bold">${rq.loginMemberNn }</div>
						<textarea maxlength=300 class="textarea textarea-bordered textarea-lg w-full" name="replyBody" placeholder="댓글을 입력하세요."></textarea>
						<div class="flex justify-end"><button class="btn btn-outline text-xl">댓글 작성</button></div>
					</div>
				</form>
			</div>
		</c:if>
	
		<!-- 채팅창 및 날씨 -->
		<div class="flex">
			<div class="w-1/3">
				<div class="container overflow-auto max-h-96 text-center" id="chat"></div>
				<div class="chatOther"></div>
			</div>
			<div class="w-1/5">
				<div class="userList text-center text-3xl font-bold"></div>
				<ul id="onlineUsers" class="text-2xl font-bold text-center"></ul>
			</div>
			<div class="grow text-center font-bold text-2xl">날씨
				<div class="flex justify-center">
					<table class="table">
						<colgroup>
							<col width="60"/>
							<col width="60"/>
							<col width="60"/>
							<col width="60"/>
						</colgroup>
					    <thead>
				     		<tr class="text-xl">
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
							<col width="70"/>
							<col width="70"/>
							<col width="70"/>
							<col width="70"/>
							<col width="70"/>
							<col width="70"/>
						</colgroup>
					    <thead>
				     		<tr class="midDate text-xl">
				     			<td></td>
				     		</tr>
				    	</thead>
				    	<tbody>
				    		<tr class="temp"></tr>
				   			<tr class="am"></tr>
				   			<tr class="cloud"></tr>
				    	</tbody>
					</table>
				</div>
			</div>
		</div>
		
		<script src="/webjars/sockjs-client/1.5.1/sockjs.min.js"></script>
		<script src="/webjars/stomp-websocket/2.3.4/stomp.min.js"></script>
		<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
		<script>
			var roomId = '${festival.eventSeq}';
			var sender = '${rq.loginMemberNumber}';
			var messageInputElement;
			var messageListElement;
			var sock = new SockJS("/ws-stomp");
			var ws = Stomp.over(sock);
			var reconnect = 0;
			var onlineNickname;
			var chatNickname;
			
			// 로그인 확인
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
						
						console.log(data);
						
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
							<th>\${data.wf3Am}/\${data.wf3Pm}</th>
							<th>\${data.wf4Am}/\${data.wf4Pm}</th>
							<th>\${data.wf5Am}/\${data.wf5Pm}</th>
							<th>\${data.wf6Am}/\${data.wf6Pm}</th>
							<th>\${data.wf7Am}/\${data.wf7Pm}</th>
								`);
					},
					error : function(xhr, status, error) {
						console.log(error);
					}
				})
				
				updateOnlineUsers();
				
				if (${rq.loginMemberNumber == 0}) {
					$('#chat').append('<div> 채팅방 기능은 로그인후에만 사용하실 수 있습니다. </div>');
					return;
				}
				
				chatBox();
				connect();
				
				setInterval(function() {
	                updateOnlineUsers();
	            }, 500);
				
			})
			
			// 채팅창 불러오기
			function chatBox(){
				$('#chat').html(`
						<div class="font-bold text-4xl text-center"> 채팅방 </div>
						<ul id="messageList" class="list-group">
						</ul>
						<br/><br/>
						`);
				$('.chatOther').html(`
						<div class="input-group">
							<textarea maxlength=300 id="messageInput" onkeypress="handleKeyPress(event)" class="form-control textarea textarea-bordered textarea-lg w-full" name="replyBody" placeholder="채팅을 입력하세요."></textarea>
							<button class="mt-4 btn btn-outline w-full" type="button" onclick="sendMessage()">보내기</button>
						</div>
						`);
				$('.userList').html(`
						<h1> 해당 행사를 보고있는 이용자</h1>
						`);
				messageInputElement = document.getElementById('messageInput');
				messageListElement = document.getElementById('messageList');
			}
	
			// 채팅보내기
			function sendMessage() {
				var message = messageInputElement.value.trim();
				if (message === "") {
					alert("메시지를 입력해 주세요.");
					return;
				}
	
				ws.send("/pub/chat/message", {}, JSON.stringify({ type : 'TALK', roomId : roomId, sender : sender, message : message }));
				messageInputElement.value = '';
			}
	
			// 채팅받기
			function recvMessage(recv) {
				if (recv.type != 'TALK') {
					var li = $('<li>').addClass('list-group-item text-2xl font-bold text-center').text(recv.nickname + ' ' + recv.message);
				    
					if (recv.type == 'ENTER') {
				    	li.addClass('text-blue-500');
				    } else {
				    	li.addClass('text-purple-500');
				    }
					$(messageListElement).append(li);
					return;
				}
				
				if (recv.sender != '${rq.loginMemberNumber}') {
					var li = $('<li>').addClass('list-group-item').html(`
							<div class="chat chat-start">
								<div class="chat-image avatar">
							    <div class="w-10 rounded-full">
							    	<img src="/user/file/memberImg/\${recv.sender }" />
							    </div>
							  </div>
							  <div class="chat-header">
							    \${recv.nickname}
							    <time class="text-xs opacity-50">\${recv.timestamp.substring(5)}</time>
							  </div>
							  <div class="chat-bubble">\${recv.message}</div>
							</div>
							`);
				} else {
					var li = $('<li>').addClass('list-group-item').html(`
							<div class="chat chat-end">
							  <div class="chat-image avatar">
							    <div class="w-10 rounded-full">
							  	  <img src="/user/file/memberImg/\${recv.sender }" />
							    </div>
							  </div>
							  <div class="chat-header">
							  \${recv.nickname}
							    <time class="text-xs opacity-50">\${recv.timestamp.substring(5)}</time>
							  </div>
							  <div class="chat-bubble">\${recv.message}</div>
							</div>
							`);
				}
				
			    if (recv.type == 'ENTER') {
			    	li.addClass('text-blue-500');
			    } else if (recv.type == 'LEAVE') {
			    	li.addClass('text-purple-500');
			    }
			    
			    $(messageListElement).append(li);
			    scrollToBottom();
			}
	
			// 채팅방 접속시 pub로 연결알리기
			function connect() {
				ws.connect({}, function(frame) {
					ws.send("/pub/chat/lastMessage", {}, JSON.stringify({ roomId : roomId, sender : sender }));
					
					$.ajax({
						type: "GET",
						url: "/chat/room/" + roomId + "/connect",
						data : {
							userId : sender
						},
						success: function(response) {
						},
						error: function(error) {
							console.error(error);
						}
					});	

					// 이전 채팅내역 불러오기
					pastMessage();
					
					// 채팅방 sub
					ws.subscribe("/sub/chat/room/" + roomId, function(message) {
						var recv = JSON.parse(message.body);
						recvMessage(recv);
					});
					
				}, function(error) {
					if (reconnect++ <= 5) {
						setTimeout(function() {
							console.log("connection reconnect");
							sock = new SockJS("/ws-stomp");
							ws = Stomp.over(sock);
							connect();
						}, 10 * 1000);
					}
				});
			}
	
			// 이전 채팅내역 가져오기
			function pastMessage() {
				$.ajax({
	                type: "GET",
	                url: "/pub/chat/lastMessage",
	                data : {
	                	userId : sender,
	                	roomId : roomId
	                },
	                success: function(response) {
	                	$.each(response, function(index, recv) {
	                		if (recv.sender != '${rq.loginMemberNumber}') {
	        					var li = $('<li>').addClass('list-group-item').html(`
	        							<div class="chat chat-start">
	        								<div class="chat-image avatar">
	        							    <div class="w-10 rounded-full">
	        							    	<img src="/user/file/memberImg/\${recv.sender }" />
	        							    </div>
	        							  </div>
	        							  <div class="chat-header text-lg">
	        							    \${recv.nickname}
	        							    <time class="text-xs">\${recv.timestamp.substring(5)}</time>
	        							  </div>
	        							  <div class="chat-bubble">\${recv.message}</div>
	        							</div>
	        							`);
	        				} else {
	        					var li = $('<li>').addClass('list-group-item').html(`
	        							<div class="chat chat-end">
	        							  <div class="chat-image avatar">
	        							    <div class="w-10 rounded-full">
	        							   		<img src="/user/file/memberImg/\${recv.sender }" />
	        							    </div>
	        							  </div>
	        							  <div class="chat-header text-lg">
	        							  \${recv.nickname}
	        							    <time class="text-xs">\${recv.timestamp.substring(5)}</time>
	        							  </div>
	        							  <div class="chat-bubble">\${recv.message}</div>
	        							</div>
	        							`);
	        				}
	        				
	        			    $(messageListElement).append(li);
	                	});
	                	ws.send("/pub/chat/message", {}, JSON.stringify({ type : 'ENTER', roomId : roomId, sender : sender }));
	                	scrollToBottom();
	                },
	                error: function(error) {
	                    console.error(error);
	                }
	            });
			}
			
			// Enter키로 채팅전송
			function handleKeyPress(event) {
				if (event.keyCode === 13) { 
					sendMessage();
				}
			}
			
			// 최신 채팅내역 보여주기
			function scrollToBottom() {
			    var chatElement = $('#chat');
			    if (chatElement.length > 0) {
			        var scrollHeight = chatElement[0].scrollHeight;
			        chatElement.scrollTop(scrollHeight);
			    }
			}
			
			// 실시간 접속 유저
			function updateOnlineUsers() {
	            $.ajax({
	                type: "GET",
	                url: "/chat/room/" + roomId + "/online-users",
	                success: function(response) {
	                	var onlineUser = Array.from(response);
	                    $("#onlineUsers").empty();
	                    $.each(onlineUser, function(index, user) {
	                    	if(user == '${rq.loginMemberNn}') {
			                    var li = `<li class="text-blue-500">\${user} <나></li>`;
	                    	} else {
			                    var li = `<li>\${user}</li>`;
	                    	}
	                    	
	                        $("#onlineUsers").append(li);
	                    });
	                },
	                error: function(error) {
	                    console.error(error);
	                }
	            });
	        }
			
			// 채팅방 퇴장 처리
			function leaveRoom(roomId) {
				ws.send("/pub/chat/message", {}, JSON.stringify({ type : 'LEAVE', roomId : roomId, sender : sender }));
				
				$.ajax({
					type: "GET",
					url: "/chat/room/" + roomId + "/disconnect",
					data : {
						userId : sender
					},
					success: function(response) {
					 },
					 error: function(error) {
					     console.error(error);
					 }
				});	
				
			}
			
			// ws 연결 끊김시 퇴장처리
			window.onbeforeunload = function() {
			    leaveRoom(roomId);
			};

			connect();

			
			
		</script>
	
	</section>
	
	<div class="mx-56">
		<button onclick="history.back()" class="btn btn-outline btn-info mt-4 w-full"><i class="fa-solid fa-arrow-left-long"></i></button>
	</div>	
		
<%@ include file="../../common/foot.jsp" %>  

<!-- 
// stompClient.send("/pub/chat/" + roomId + "/disconnect", {}, JSON.stringify({ sender: sender }));
// ws.send("/pub/chat/message", {}, JSON.stringify({ type : 'LEAVE', roomId : roomId, sender : sender }));
-->