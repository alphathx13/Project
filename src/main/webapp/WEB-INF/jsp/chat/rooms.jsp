<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ include file="../common/head.jsp" %>  

	<div class="container">
		<div>
		    <ul> 
		    	<c:forEach var="room" items="${list }">
					<li><a href="${pageContext.request.contextPath}/chat/room?roomId=${room.roomId}">${room.name}</a></li>
				</c:forEach>
		    </ul>
		</div>
	</div>
	<form action="/chat/room" method="post">
	    <input type="text" name="name" class="form-control" placeholder="방 이름">
	    <button type="submit" class="btn btn-secondary">개설하기</button>
	</form>

	<script>
	    $(document).ready(function(){

	        var roomName = '${roomName}';
	        
	        if(roomName.length != 0 && ${size } != 0)
	            alert("채팅방 " + roomName + "이 개설되었습니다.");
	
	        $(".btn-create").on("click", function (e){
	            e.preventDefault();
	
	            var name = $("input[name='name']").val();
	
	            if(name == "")
	                alert("Please write the name.")
	            else
	                $("form").submit();
	        });
	
	    });
	</script>

<%@ include file="../common/foot.jsp" %>  