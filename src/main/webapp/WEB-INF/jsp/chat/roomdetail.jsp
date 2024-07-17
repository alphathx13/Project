<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!doctype html>
<html lang="en">
<head>
</head>
<body>
	<div class="container" id="app">
		<div>
			<h2 id="roomName"></h2>
		</div>
		<div class="input-group">
			<div class="input-group-prepend">
				<label class="input-group-text">내용</label>
			</div>
			<input id="messageInput" type="text" class="form-control"
				onkeypress="handleKeyPress(event)">
			<div class="input-group-append">
				<button class="btn btn-primary" type="button"
					onclick="sendMessage()">보내기</button>
			</div>
		</div>
		<ul id="messageList" class="list-group">
			<!-- Chat messages will be dynamically added here -->
		</ul>
	</div>
	<!-- JavaScript -->
	<script src="/webjars/sockjs-client/1.5.1/sockjs.min.js"></script>
	<script src="/webjars/stomp-websocket/2.3.4/stomp.min.js"></script>
	<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

	<script>
		var roomId = localStorage.getItem('wschat.roomId');
		var sender = localStorage.getItem('wschat.sender');
		var roomNameElement = document.getElementById('roomName');
		var messageInputElement = document.getElementById('messageInput');
		var messageListElement = document.getElementById('messageList');
		var sock = new SockJS("/ws-stomp");
		var ws = Stomp.over(sock);
		var reconnect = 0;

		function findRoom() {
			$.ajax({
				type : "GET",
				url : '/chat/room/' + roomId,
				success : function(response) {
					roomNameElement.textContent = response.name;
				},
				error : function(error) {
					console.error(error);
				}
			});
		}

		function sendMessage() {
			var message = messageInputElement.value.trim();
			if (message === "") {
				alert("메시지를 입력해 주세요.");
				return;
			}

			ws.send("/pub/chat/message", {}, JSON.stringify({ type : 'TALK', roomId : roomId, sender : sender, message : message }));
			messageInputElement.value = '';
		}

		function recvMessage(recv) {
			var li = document.createElement('li');
			li.className = 'list-group-item';
			li.textContent = recv.sender + ' - ' + recv.message;
			messageListElement.insertBefore(li, messageListElement.firstChild);
		}

		// 채팅방 접속시 pub로 연결알리기
		function connect() {
			ws.connect({}, function(frame) {
				ws.subscribe("/sub/chat/room/" + roomId, function(message) {
					var recv = JSON.parse(message.body);
					recvMessage(recv);
				});
				ws.send("/pub/chat/message", {}, JSON.stringify({ type : 'ENTER', roomId : roomId, sender : sender }));
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

		function handleKeyPress(event) {
			if (event.keyCode === 13) { // Enter key
				sendMessage();
			}
		}

		findRoom();
		connect();
	</script>
</body>
</html>

