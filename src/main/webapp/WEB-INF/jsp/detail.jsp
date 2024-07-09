<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:set var="pageTitle" value="행사 소개" />
	<section class="mt-8 text-lg text-center w-72">
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
	</section>
	<button onclick="history.back()" class="btn btn-outline btn-info mt-4">뒤로 가기</button>	
	

<script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=45c9a06d40a4e3bff0f18e6318a5b541&libraries=services,clusterer,drawing"></script>

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
		
	<!-- 12917  -->