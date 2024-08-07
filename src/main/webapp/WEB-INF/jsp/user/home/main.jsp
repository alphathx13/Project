<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<script src="https://cdnjs.cloudflare.com/ajax/libs/gsap/3.11.3/gsap.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/gsap/3.11.3/ScrollTrigger.min.js"></script>

<style>
	body {
		--bgImage: url('/resource/mainPageImage/01.jpg');
	}
	
	body::before {
		content: "";
		position: fixed;
		top: 0;
		left: 0;
		width: 100%;
		height: 100%;
		filter: brightness(40%);
		background-image: var(--bgImage);
		background-size: cover;
		background-position: center;
		background-repeat: no-repeat;
		z-index: -1;
		transition: background-image 1.5s ease-in-out;
	}
</style>
  
<%@ include file="../../common/head.jsp" %>  


    <section class="py-16 mx-72 text-white">
        <div class="container mx-auto text-center flex flex-col">
            <h1 class="text-4xl font-bold mb-4"> 대전광역시 행사 한눈에 보기</h1>
            <p class="text-lg">대전에서 진행되는 행사에 대한 정보를 제공합니다.</p>
            <p class="text-lg">서로서로 정보를 나눌 수 있는 공간이며,</p>
            <p class="text-lg mb-8">누구나 자유롭게 이용할 수 있습니다.</p>
        </div>
        
        <div class="item-1 mt-32 flex"></div>
        <div class="item-2 mt-32 flex"></div>
        <div class="item-3 mt-32 flex"></div>
        <div class="item-4 mt-32 flex"></div>
        <div class="item-5 mt-32 flex"></div>
        <div class="item-6 mt-32 flex"></div>
        <div class="item-7 mt-32 flex"></div>
        <div class="item-8 mt-32 flex"></div>

    </section>
    
    <script>
    	// gsap 설정
	    gsap.registerPlugin(ScrollTrigger);
		
		function gsapFunction(item) {
			
			var className = 'item-' + item;
			var direction = item % 2 == 0 ? 500: -500 

			gsap.fromTo('.' + className,
					{ autoAlpha: 0, x: direction },
		            {
						autoAlpha: 1,
		                x: 0, 
		                duration: 1, 
		                scrollTrigger: {
		                    trigger: '.' + className, 
		                    start: "top 80%", 
		                    end: "top 50%", 
		                }
		            }
			);
		}
    
    	// 소개 항목 그리는 함수
    	function mainPageItem(index, title, body, imageLink, link) {
    		if (index % 2 == 0) {
				$('.item-' + index).append(image(imageLink,link)).append(arrow('left')).append(contents(title,body));
    		} else {
				$('.item-' + index).append(contents(title,body)).append(arrow('right')).append(image(imageLink,link));
    		}
    		
    		gsapFunction(index);
    	}
    	
    	// 내용 채우기
    	function contents(title, body) {
    		return `<div style="height:400px;" class="mx-2 w-1/2"> 
						<div class="mt-12 flex justify-between">
							<div class="font-bold text-5xl mr-4"> \${title}</div>
						</div>
						<div class="mt-12 text-2xl"> \${body}</div>
					</div>
					`;
    	}
    	
    	// 이미지 넣기
    	function image(imageLink, link) {
    		return `<div style="height:400px;" class="mx-2 w-1/2 flex items-center"> 
						<a href="\${link}"><img class="image transition-transform duration-300 ease-in-out hover:scale-125 rounded-xl" src="\${imageLink}" alt="fallback_image" /></a> 
					</div>
					`;
    	}
    	
    	// 화살표
    	function arrow(direction) {
    		if (direction == 'left') {
    			return `<span class="mx-16 flex"><i class="w-full fa-solid fa-chevron-left text-6xl self-center"></i></span>`;
    		} else {
    			return `<span class="mx-16 flex"><i class="w-full fa-solid fa-chevron-right text-6xl self-center"></i></span>`;
    		}
    	}
    	
    	// 소개 항목 그리기
    	$(document).ready(function() {
    		mainPageItem(1, '행사 일정 확인', '현재 진행중인 행사들을 확인하실 수 있습니다. <br/> 행사에 대한 기본적인 정보를 볼 수 있습니다.', '/resource/homeContentsImg/1.png', '/user/festival/list');
    		mainPageItem(2, '진행 예정 / 종료 행사', '진행 예정인 행사 및 과거에 진행된 행사를 확인하실 수 있습니다. <br/>  ', '/resource/homeContentsImg/2.png', '/user/festival/list');
    		mainPageItem(3, '행사 검색', '검색을 통해 관심있는 행사를 찾으실 수 있습니다.', '/resource/homeContentsImg/3.png', '/user/festival/list');
    		mainPageItem(4, '행사 상세정보', '상세페이지에서 장소, 시간, 세부사항을 확인하실 수 있습니다.', '/resource/homeContentsImg/4.png', '/');
    		mainPageItem(5, '날씨 정보', '방문하려고하는 행사 날짜의 날씨를 한눈에 확인할 수 있습니다.', '/resource/homeContentsImg/5.png', '/');
    		mainPageItem(6, '장소, 추천, 공유', '행사장 위치를 지도로 보실 수있으며, 해당 행사에 대한 추천 및 공유가 가능합니다.', '/resource/homeContentsImg/6.png', '/user/festival/list');
    		mainPageItem(7, '의견 나누기', '댓글을 통해 해당 행사에 대한 의견을 나눌 수 있습니다. 이용자들간 댓글 추천이 가능하며, 많은 사람들이 추천한 댓글을 확인할 수 있습니다.', '/resource/homeContentsImg/7.png', '/user/festival/list');
    		mainPageItem(8, '실시간 채팅', '실시간 채팅을 통해 이용자분들간 대화가 가능합니다.', '/resource/homeContentsImg/8.png', '/user/festival/list');

    		$('.image').on('error', function() {
	                $(this).attr('src', '/resource/images/imageLoadingError.png');
	        });
    		
    		// 배경 이미지 변경
    		const images = [
       			'/resource/mainPageImage/01.jpg',  
       			'/resource/mainPageImage/02.jpg',  
       			'/resource/mainPageImage/03.jpg',  
       			'/resource/mainPageImage/04.jpg',  
       			'/resource/mainPageImage/05.jpg',  
       			'/resource/mainPageImage/06.jpg',  
  			];

    		let currentIndex = 0;

    	    function changeBackgroundImage() {
    	    	$('body').css('--bgImage', `url('\${images[currentIndex]}')`);
    	        currentIndex = (currentIndex + 1) % images.length;
    	    }

    	    // 초기 배경 이미지 설정
    	    changeBackgroundImage();

    	    // 2.5초마다 배경 이미지 변경
    	    setInterval(changeBackgroundImage, 2500);
		
		})
    	
    </script>
      
<%@ include file="../../common/foot.jsp" %>  

<!-- 
행사 예시는 12968
 -->