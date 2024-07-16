<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!doctype html>
<html lang="en">
<head>
</head>
<body>
  <div class="container" id="app" v-cloak>
      <div class="row">
          <div class="col-md-12">
              <h3>채팅방 리스트</h3>
          </div>
      </div>
      <div class="input-group">
          <div class="input-group-prepend">
              <label class="input-group-text">방제목</label>
          </div>
          <input id="room_name" type="text" class="form-control">
          <div class="input-group-append">
              <button class="btn btn-primary" type="button" onclick="createRoom()">채팅방 개설</button>
          </div>
      </div>
      <ul id="chatrooms" class="list-group">
          <!-- Chat rooms will be dynamically added here -->
      </ul>
  </div>
  <!-- JavaScript -->
  <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
  <script>
      // 채팅방 만들기
      function createRoom() {
          var roomNameInput = document.getElementById('room_name');
          var roomName = roomNameInput.value.trim();
          if (roomName === "") {
              alert("방 제목을 입력해 주세요.");
              return;
          }

          $.ajax({
              url: '/chat/room',
              type: "POST",
              data: {
            	  name : roomName
              },
              dataType: "json",
              success: function(response) {
                  alert(response.name + " 방 개설에 성공하였습니다.");
                  roomNameInput.value = ''; 
                  findAllRooms(); 
              },
              error: function(error) {
                  alert("채팅방 개설에 실패하였습니다.");
                  console.error(error);
              }
          });
      }

      // 모든 채팅방 가져오기
      function findAllRooms() {
          $.ajax({
              type: "GET",
              url: '/chat/rooms',
              success: function(response) {
                  displayRooms(response);
              },
              error: function(error) {
                  console.error(error);
              }
          });
      }

	// 모든 채팅방 보여주기
      function displayRooms(rooms) {
          var chatroomsList = document.getElementById('chatrooms');
          chatroomsList.innerHTML = ''; 

          rooms.forEach(function(room) {
              var li = document.createElement('li');
              li.className = 'list-group-item list-group-item-action';
              li.textContent = room.name;
              li.onclick = function() {
                  enterRoom(room.roomId);
              };
              chatroomsList.appendChild(li);
          });
      }

      // 채팅방 들어가기
      function enterRoom(roomId) {
          var sender = prompt('대화명을 입력해 주세요.');
          if (sender !== null && sender.trim() !== "") {
              localStorage.setItem('wschat.sender', sender);
              localStorage.setItem('wschat.roomId', roomId);
              location.href = "/chat/room/enter/" + roomId;
          }
      }

      // 초기 채팅방 불러오기
      findAllRooms();
  </script>
</body>
</html>

