<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ include file="../../common/head.jsp"%>
<%@ include file="../../common/toastUiEditor.jsp"%>

	<section class="mt-8 text-lg">
		<div class="container mx-auto px-3">
			<form action="doWrite" method="GET" onsubmit="imgNumber(); fileNumber(); check(this); return false;">
				<input type="file" id="fileInput" style="display: none;" multiple>
				<input type="file" id="imageInput" style="display: none;" multiple accept="image/*">
				<input type="hidden" name="fileUpload">
				<input type="hidden" name="imgUpload">
				<input type="hidden" value="${boardId }" name="boardId">
				<input type="hidden" value="" name="body">
				<input type="text" maxlength=100 class="input input-bordered w-full mb-4" name="title" placeholder="글 제목"></input>
				<div class="toast-ui-editor"></div>
				<div class="tooltip" data-tip="뒤로 가기">
					<button class="btn btn-outline btn-info" onclick="history.back();">
					<i class="fa-solid fa-arrow-left-long"></i>
					</button>
				</div>
				<div id="fileNames" class="file-names"></div>
				<button class="mt-5 btn btn-outline btn-info">글 작성하기</button>
			</form>
		</div>
	</section>
	
	<script>
		function fileNumber() {
			var fileNumber = JSON.stringify(fileArray); 
			document.querySelector('input[name="fileUpload"]').value = fileNumber;
		}
		
		function imgNumber() {
			var imgNumber = JSON.stringify(imgArray); 
			document.querySelector('input[name="imgUpload"]').value = imgNumber;
		}	
		
	</script>
	
<%@ include file="../../common/foot.jsp"%>