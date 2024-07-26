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
	
	// image 저장할 변수
	let imageArray = [];
	
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
				hooks: {
		            addImageBlobHook(blob, callback) { 

		            	const formData = new FormData();
		                formData.append('file', blob);
		                
		                $.ajax({
		                    url: '/user/file/upload',
		                    type: 'POST',
		                    enctype: 'multipart/form-data',
		                    data: formData,
		                    dataType: 'json',
		                    processData: false,
		                    contentType: false,
		                    cache: false,
		                    timeout: 600000,
		                    success: function(data) {
		                    	imageArray.push(data.id);
		                    	var imgPath = '/user/file/images/' + data.id;
		                        callback(imgPath, '${data.originName}');
		                    },
		                    error: function(e) {
		                        callback('image_load_fail', '이미지 업로드가 실패했습니다. 다시 업로드해주세요.');
		                    }
		                });
		                
		            }
		        }
		    });
		    
		    editorItem.data('data-toast-editor', editor);

	 	});
	  	
	});
	
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
