<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title> Project</title>
	
	<!-- 파비콘 넣기 -->
	<link rel="shortcut icon" href="/resource/favicon/favicon.ico" />
	 
	<!-- tailwind CSS -->
	<script src="https://cdn.tailwindcss.com"></script>
	
	<!-- daisyUI -->
	<link href="https://cdn.jsdelivr.net/npm/daisyui@4.12.8/dist/full.min.css" rel="stylesheet" type="text/css" />
	<link href="https://fonts.googleapis.com/css2?family=Outfit:wght@100;200;400;700&display=swap" rel="stylesheet">
	
	<!-- jQuery -->
	<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
	
	<!-- font awesome -->
	<link rel="stylesheet"
		href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
	
	<!-- 공통 CSS -->
	<link rel="stylesheet" href="/resource/common.css" />
	
	<!-- 공통 javascript -->
	<script src="/resource/common.js" defer="defer"></script>
	
	<!-- firebase 설정 -->
	<script type="module" src="/resource/firebase.js"></script>

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
	
</head>
<body>
	<div class="head h-20 flex mx-auto text-3xl bg-white outfitFont">
		<div class="flex ml-4 logo text-6xl">
			<a href="/" class="h-full flex items-center"> <img class="w-12 h-12" src="/resource/images/homeButton.png" /> </a>
			<div class="mt-6 ml-2 text-2xl font-bold text-white"></div>
		</div>
		<div class="grow"></div>
		<ul class="flex">
			<c:choose>
				<c:when test="${rq.loginMemberNumber == 0 }">
					<li class="hover:underline p-4 text-black">
						<button class="rounded-xl h-12 w-20 bg-white font-bold text-sm py-2 px-4 border border-black rounded hover:scale-105 transition-transform duration-300" onclick="my_modal_1.showModal()">로그인</button>
						<dialog id="my_modal_1" class="modal">
					  		<div class="modal-box">
					    		<h3 class="text-lg font-bold">Login</h3>
					    		<div class="container mx-auto px-3 w-72">
									<form action="/user/member/doLogin" method="post" onsubmit="check(this); return false;">
										<input type="hidden" class="uri" name="uri"/>
										<label class="input flex items-center gap-2 border border-black">
											<div>
												<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 14 14" fill="currentColor" class="h-5 w-5 opacity-70">
								   					<path d="M8 8a3 3 0 1 0 0-6 3 3 0 0 0 0 6ZM12.735 14c.618 0 1.093-.561.872-1.139a6.002 6.002 0 0 0-11.215 0c-.22.578.254 1.139.872 1.139h9.47Z" />
								  				</svg> 
							  				</div> 
								  			<input maxlength="20" type="text" class="" placeholder="아이디" name="loginId"/>
										</label> 
										<label class="mt-1 input flex items-center gap-2 border border-black">
											<div>
												<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 14 14" fill="currentColor" class="h-5 w-5 opacity-70">
								   					<path d="M14 6a4 4 0 0 1-4.899 3.899l-1.955 1.955a.5.5 0 0 1-.353.146H5v1.5a.5.5 0 0 1-.5.5h-2a.5.5 0 0 1-.5-.5v-2.293a.5.5 0 0 1 .146-.353l3.955-3.955A4 4 0 1 1 14 6Zm-4-2a.75.75 0 0 0 0 1.5.5.5 0 0 1 .5.5.75.75 0 0 0 1.5 0 2 2 0 0 0-2-2Z" />
								  				</svg> 
							  				</div>
								  			<input maxlength="12" type="password" class="pw grow" placeholder="비밀번호" name="loginPw"/> 
								  			<button class="change" type="button"><i class="see fa-solid fa-eye"></i><i class="notSee hidden fa-solid fa-eye-slash"></i></button>
										</label>
										<div class="tooltip w-full" data-tip="로그인">
											<button class="mt-5 w-full text-xl btn btn-outline border border-black">
												<i class="fa-solid fa-right-to-bracket text-black"></i>
											</button>
										</div>
										<div class="firebaseLogin flex mt-4">
											<div class="tooltip w-full font-bold text-center" data-tip="구글로 로그인하기">
												<button type="button" onclick="googleSignIn()">
													<img class="h-10" src="/resource/images/googleIcon.webp" />
												</button>
											</div>
											<div class="tooltip w-full font-bold text-center" data-tip="카카오로 로그인하기">
												<button type="button" onclick="kakaoLogin()">
													<img class="h-9" src="/resource/images/kakaoIcon.png" />
												</button>
											</div>
											<div class="tooltip w-full font-bold text-center" data-tip="네이버로 로그인하기">
												<button type="button" onclick="naverLogin()">
													<img class="h-9" src="/resource/images/naverIcon.png" />
												</button>
											</div>
										</div>
									</form>
									<div class="mt-4 font-bold text-xs"><a href="/user/member/findLoginId" class = "hover:text-sky-500 font-extrabold">아이디를 잊어버리셨나요?</a></div>
									<div class="mt-4 font-bold text-xs"><a href="/user/member/findLoginPw" class = "hover:text-sky-500 font-extrabold">비밀번호를 잊어버리셨나요?</a></div>
									<div class="mt-4 font-bold text-xs"><a href="/user/member/join" class = "hover:text-sky-500 font-extrabold">계정이 없으신가요?</a></div>
								</div>
					      		<form method="dialog">
					        		<button class="btn text-black">Close</button>
					      		</form>
					    	</div>
						</dialog>
					</li>
				</c:when>
				<c:otherwise>
					<div class="mt-4 dropdown dropdown-end">
						<div tabindex="0" role="button">
							<img class="h-12 w-12 rounded-full" src="${rq.loginMemberImgPath }" />						
						</div>
						<ul tabindex="0"
							class="menu menu-sm dropdown-content bg-base-100 rounded-box z-[1] mt-3 shadow w-32 text-black font-bold mt-">
							<li><button class="h-full flex items-center" onclick="location.href='/user/member/myPage'"> 마이 페이지 </button></li>
							<li><button class="h-full flex items-center" onclick="if(confirm('로그아웃 하시겠습니까?') == false) return false; logout();">로그아웃</button></li>
						</ul>
					</div>
				</c:otherwise>
			</c:choose>

			<li class="hover:underline p-4 mt-3 ml-4"><a class="h-full px-3 flex items-center" href="/user/festival/list"><span>행사</span></a>
			<li class="hover:underline p-4 mt-3 ml-4"><a class="h-full px-3 flex items-center" href="/user/article/list?boardId=1"><span>게시판</span></a>
		</ul>
	</div>

	<script type="text/javascript" src="https://developers.kakao.com/sdk/js/kakao.js"></script>
	<script>
		var currentPageUrl = window.location.href;
		$('.uri').val(currentPageUrl);
		
		function logout() {
			location.href='/user/member/doLogout?uri=' + currentPageUrl;
		}
		
		function check(form) {
			let loginId = form.loginId.value.trim();
			let loginPw = form.loginPw.value.trim();
	
			if (loginId.length == 0) {
				alert('id는 필수입력 정보입니다.');
				form.loginId.focus();
				return;
			}
	
			if (loginPw.length == 0) {
				alert('암호는 필수입력 정보입니다.');
				form.loginPw.focus();
				return;
			}
	
			form.submit();
		}

		$('.change').click(function() {
			if ($('.pw').attr('type') == 'password') {
				$('.pw').attr('type', 'text');
				$('.see').css('display', 'none');
				$('.notSee').css('display', 'block');
			} else {
				$('.pw').attr('type', 'password');
				$('.see').css('display', 'block');
				$('.notSee').css('display', 'none');
			}
		})
		
		// firebase 로그인
		function firebaseLogin(uid, email) {
		
			$.ajax({
				url : '/user/member/firebaseLogin',
				type : 'POST',
				data : {
					uid : uid,
					email : email
				},
				dataType : 'json',
				success : function(result) {
					if (result.resultMsg == 'false') {
						alert('처음 이용하시는 계정입니다. 사이트 이용을 위해 추가적인 정보를 입력하셔야 합니다.');
						window.location.href = `/user/member/firebaseJoin?uid=\${uid}&email=\${email}`;
					} else {
						alert(result.data + '님 로그인을 환영합니다.');
						window.location.reload();
					}
				},
				error : function(xhr, status, error) {
					console.log(error);
				}
			})
		}
		
		// 카카오 로그인
	    Kakao.init('45c9a06d40a4e3bff0f18e6318a5b541');
	    function kakaoLogin() {
	        Kakao.Auth.login({
	            success: function (response) {
	                Kakao.API.request({
	                    url: '/v2/user/me',
	                    success: function (response) {
	                    	firebaseLogin(response.id, response.kakao_account.email);
	                    },
	                    fail: function (error) {
	                        alert(JSON.stringify(error));
	                    },
	                })
	            },
	            fail: function (error) {
	                alert(JSON.stringify(error))
	            },
	        })
	    }
	    
	    // 네이버 로그인
	    function naverLogin() {
			window.location.href = '/user/member/naverLogin';
		}
	    
	    // memberImg 가져오기
	    function naverLogin() {
			window.location.href = '/user/member/naverLogin';
		}

	</script>
	
	
	