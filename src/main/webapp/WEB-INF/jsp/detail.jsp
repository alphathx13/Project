<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ include file="common/head.jsp" %>  

<c:set var="pageTitle" value="행사 소개" />
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
		<div id="map" style="width:100%;height:350px;"></div>
	
		<script>
			const kakaoKey = '${kakaoKey}';
		</script>
		<script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=${kakaoKey}&libraries=services,clusterer,drawing"></script>
		
		<script>
		
			// 지도 생성
			var infowindow = new kakao.maps.InfoWindow({zIndex:1});
			
			var mapContainer = document.getElementById('map'), 
			    mapOption = { 
			        center: new kakao.maps.LatLng(36.351064, 127.379726), 
			        level: 3
			    };
			var map = new kakao.maps.Map(mapContainer, mapOption); 
			
			// 장소 위치 조정
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
		
			// 장소검색
			ps.keywordSearch(place, placesSearchCB); 
			
			function placesSearchCB (data, status, pagination) {
			    if (status === kakao.maps.services.Status.OK) {
			
			        // LatLngBounds 객체에 좌표를 추가
			        var bounds = new kakao.maps.LatLngBounds();
		            displayMarker(data[0]);    
			        bounds.extend(new kakao.maps.LatLng(data[0].y, data[0].x));
			
			        // 검색된 장소 위치를 기준으로 지도 범위를 재설정
			        map.setBounds(bounds);
			    } 
			}
			
			// 마커 생성 및 클릭
			function displayMarker(place) {
			    
			    var marker = new kakao.maps.Marker({
			        map: map,
			        position: new kakao.maps.LatLng(place.y, place.x) 
			    });
			
			    // 마커에 클릭이벤트 추가
			    kakao.maps.event.addListener(marker, 'click', function() {
			        infowindow.setContent('<div style="padding:5px;font-size:12px;">' + place.place_name + '</div>');
			        infowindow.open(map, marker);
			    });
			}
			
		</script>
	
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
		    		<tr class="6"></tr>
		   			<tr class="9"></tr>
		   			<tr class="12"></tr>
		   			<tr class="15"></tr>
		   			<tr class="18"></tr>
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
				url : '/weatherShortView',
				type : 'GET',
				dataType : 'json',
				success : function(data) {
					console.log(data);
					var dataNum = 0;
					for (var num = 6; num <= 18; num+=3) {
						$('.' + num).html(`
								<td>6시</td>
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
				url : '/weatherMidView',
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
	
	<button onclick="history.back()" class="btn btn-outline btn-info mt-4">뒤로 가기</button>	
	
	<script>
  		
  	</script>
		
<%@ include file="common/foot.jsp" %>  

<!--  					$('.temp').html(`
						<th>\${data.taMin3} 도 / \${data.taMax3} 도</th>
						<th>\${data.taMin4} 도 / \${data.taMax4} 도</th>
						<th>\${data.taMin5} 도 / \${data.taMax5} 도</th>
						<th>\${data.taMin6} 도 / \${data.taMax6} 도</th>
						<th>\${data.taMin7} 도 / \${data.taMax7} 도</th>
							`); -->
