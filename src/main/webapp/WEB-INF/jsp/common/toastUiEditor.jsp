<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<link rel="stylesheet"
	href="https://uicdn.toast.com/editor/latest/toastui-editor.min.css" />
<link rel="stylesheet"
	href="https://uicdn.toast.com/tui-color-picker/latest/tui-color-picker.min.css" />
<link rel="stylesheet"
	href="https://uicdn.toast.com/editor-plugin-color-syntax/latest/toastui-editor-plugin-color-syntax.min.css" />
<script
	src="https://uicdn.toast.com/tui-color-picker/latest/tui-color-picker.min.js"></script>
<script
	src="https://uicdn.toast.com/editor/latest/toastui-editor-all.min.js"></script>
<script
	src="https://uicdn.toast.com/editor-plugin-color-syntax/latest/toastui-editor-plugin-color-syntax.min.js"></script>

<script>
	// image, file 목록 저장
	let imgArray = [];
	let fileArray = [];
	
	// toastui editor 설정
	const { Editor } = toastui;
	const { colorSyntax } = Editor.plugin;
	
	$(function(){
	  	$('.toast-ui-editor').each(function(index, item) {
			const editorItem = $(item);
			const initialValueEl = editorItem.find(' > script');
			const initialValue = initialValueEl.length == 0 ? '' : initialValueEl.html().trim();
			
		    const editor = new toastui.Editor({
				el: item,
				height: '600px',
				initialEditType: 'wysiwyg',
				initialValue: initialValue,
				previewStyle: 'vertical',
				hideModeSwitch: true,
				plugins: [colorSyntax],
		    });
		    
            $('.toastui-editor-context-menu').before(`<div><button class="imageUpload btn btn-info" type="button">이미지 업로드</button> <button class="fileUpload btn btn-info" type="button">파일 업로드</button></div>`);

            $('.imageUpload').on('click', function() {
            	$('#imageInput').click();
            });
            
            $('.fileUpload').on('click', function() {
            	$('#fileInput').click();
            });

		    editorItem.data('data-toast-editor', editor);
		    
		    loadFileList().done(function() {
		        fileList();
		    });
		    
		    //$('.image.toastui-editor-toolbar-icons').remove();
		    
	 	});
	  	
	});
	
	$(document).ready(function() {
		
		// 파일 업로드
		$('#fileInput').on('change', function(event) {
		    const files = event.target.files;
		    let fileNames = [];

		    for (let i = 0; i < files.length; i++) {
		        fileNames.push(files[i].name);
		    }
		
		    handleFileUpload(files).done(function() {
		        fileList();
		    });
		    
		});
		
		// 이미지 업로드
		$('#imageInput').on('change', function(event) {
			const files = event.target.files;
		    const formData = new FormData();
			
			for (let i = 0; i < files.length; i++) {
		        var file = files[i];
		        formData.append('file', file);
			}

	        $.ajax({
	            url: '/user/file/imageUpload',
	            type: 'POST',
	            enctype: 'multipart/form-data',
	            data: formData,
	            dataType: 'json',
	            processData: false,
	            contentType: false,
	            cache: false,
	            timeout: 600000,
	            success: function(result) {
	            	$.each(result, function(index, item) {
		                $('.toastui-editor-contents').append(`<img src="/user/file/images/\${item.id}" contenteditable="false"></p>`);
		                imgArray.push(item.id);
	            	})
	            	
	            },
	            error: function(e) {
	                console.error('이미지 업로드에 실패했습니다.', e);
	            }
	        });
		    
		});
		
		// 실제 파일 업로드
		function handleFileUpload(files) {
			
	        const formData = new FormData();
	        for (let i = 0; i < files.length; i++) {
	            formData.append('files[]', files[i]);
	        }
	        
	        return $.ajax({
	            url: '/user/file/fileUpload',
	            type: 'POST',
	            data: formData,
	            processData: false,
	            contentType: false,
	            success: function(response) {
	            	fileArray = fileArray.concat(response);
	            },
	            error: function(error) {
	                console.error('파일 업로드에 문제가 발생했습니다.', error);
	            }
	        });
	    }
		
	})
	
	// 글 수정창 열었을때 첨부파일 가져오기
	function loadFileList() {
		
		const id = new URLSearchParams(window.location.search).get('id');
		
		return $.ajax({
            url: '/user/article/getFileList',
            type: 'POST',
            data: {id : id},
            dataType : 'text',
            success: function(response) {
            	if (response != '') {
            		fileArray = response.split(',').map(item => item.trim());
            	}
            },
            error: function(error) {
                console.error('', error);
            }
        });
	}
	
	// 첨부파일 리스트 갱신
	function fileList() {
		
		$('#fileNames').empty();
		
		if (fileArray.length != 0) {
			var fileNumber = fileArray.join(', ');
			
			$.ajax({
	            url: '/user/file/getFileById',
	            type: 'POST',
	            data: {file : fileNumber},
	            dataType : 'json',
	            success: function(response) {
	            	$.each(response, function(index, item) {
	        	    	$('#fileNames').append(`\${item.originName} <button type="button" class="text-red-500" onclick="uploadFileDelete(\${item.id});"> X </button><br>`);
	            	});
	            },
	            error: function(error) {
	                console.error('', error);
	            }
	        });
		
		}
	}
   
	// 업로드 파일 삭제
	function uploadFileDelete(id) {
		$.ajax({
            url: '/user/file/uploadFileDelete',
            type: 'POST',
            data: {id : id},            
            success: function(response) {
				fileArray = fileArray.filter(item => item !== id.toString());
				fileList();
            },
            error: function(error) {
                console.error('', error);
            }
        });
		
	}
	
	// 공백체크
	function check(form){
		const title = form.title.value.trim();
		const body = form.body.value.trim();
		const editorData = $(form).find('.toast-ui-editor').data('data-toast-editor');
		const markdown = editorData.getMarkdown().trim();
	
		if (title.length == 0) {
			alert('제목을 입력해주세요.');
			form.title.focus();
			return;
		}
		
		if(markdown.length == 0){
			alert('내용을 입력해주세요');
			editorData.focus();
			return;
		}
	
		form.body.value = markdown;
		
		form.submit();
	}
	
</script>
