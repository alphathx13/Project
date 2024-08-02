<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<script src="https://cdnjs.cloudflare.com/ajax/libs/gsap/3.11.3/gsap.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/gsap/3.11.3/ScrollTrigger.min.js"></script>
  
<%@ include file="../../common/head.jsp" %>  

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


    <section class="py-16 mx-72 text-white">
        <div class="container mx-auto text-center flex flex-col">
            <h1 class="text-4xl font-bold mb-4"> 대전광역시 행사 한눈에 보기</h1>
            <p class="text-lg">저희 사이트는 대전에서 진행되는 행사에 대한 정보를 제공하고, 사용자들이 정보를 나눌 수 있는 공간입니다.</p>
            <p class="text-lg mb-8">누구나 자유롭게 이용할 수 있습니다.</p>
        </div>
        
        <div class="item-1 mt-32 flex"></div>
        <div class="item-2 mt-32 flex"></div>
        <div class="item-3 mt-32 flex"></div>
        <div class="item-4 mt-32 flex"></div>
        <div class="item-5 mt-32 flex"></div>
        <div class="item-6 mt-32 flex"></div>

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
				$('.item-' + index).append(image(imageLink,link)).append(contents(title,body));
    		} else {
				$('.item-' + index).append(contents(title,body)).append(image(imageLink,link));
    		}
    		
    		gsapFunction(index);
    	}
    	
    	// 내용 채우기
    	function contents(title, body) {
    		return `<div style="height:400px;" class="mx-2 w-1/2"> 
						<div class="mt-12 flex justify-between">
							<div class="font-bold text-4xl mr-4"> \${title}</div>
							<div> ------------------- </div>
						</div>
						<div class="mt-12 text-3xl"> \${body}</div>
					</div>
					`;
    	}
    	
    	// 이미지 넣기
    	function image(imageLink, link) {
    		return `<div style="height:400px;" class="mx-2 w-1/2"> 
						<a href="\${link}"><img class="h-full image" src="\${imageLink}" alt="fallback_image" /></a> 
					</div>
					`;
    	}
    	
    	// 소개 항목 그리기
    	$(document).ready(function() {
    		mainPageItem(1, '행사 일정 확인', '현재 진행중인 행사에 대한 정보를 제공합니다.', 'https://flexible.img.hani.co.kr/flexible/normal/970/646/imgdb/original/2023/0131/20230131501593.jpg', '/');
    		mainPageItem(2, '행사 내용 확인', '행사 상세페이지에서는 진행중인 설명과 장소, 시간를 확인하실 수 있습니다.', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTo4AQOUitqLbtcEvdfbB9cFryEQCES3TQf6w&s', '/');
    		mainPageItem(3, '날씨 확인', '향후 일주일간 날씨를 보여줘서 방문시 날씨를 확인할 수 있습니다.', 'https://newsimg.hankookilbo.com/2019/04/29/201904291390027161_3.jpg', '/');
    		mainPageItem(4, '의견 나누기', '댓글을 통해 해당 행사에대한 의견을 나눌 수 있습니다.', 'https://m.segye.com/content/image/2024/04/17/20240417511930.JPG', '/');
    		mainPageItem(5, '실시간 채팅', '실시간 채팅방으로 이용자분들과 의견을 나눌 수 있습니다.', 'https://www.palnews.co.kr/news/photo/201801/92969_25283_5321.jpg', '/');
    		mainPageItem(6, 'TestTestTest', 'TestTestTest', 'aa', '/');

    		$('.image').on('error', function() {
	                $(this).attr('src', '/resource/images/imageLoadingError.png');
	        });
    		
    		// 메인 페이지 이미지 변경
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

    	    // 3초마다 배경 이미지 변경
    	    setInterval(changeBackgroundImage, 3000);
		
		})
    	
    </script>
      
<%@ include file="../../common/foot.jsp" %>  


<!-- 

 -->