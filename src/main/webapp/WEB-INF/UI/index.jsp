<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!DOCTYPE html >
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script src="https://cdn.bootcss.com/jquery/1.12.4/jquery.min.js"></script>
<title>Insert title here</title>
<c:set var="path" value="${pageContext.request.contextPath }"></c:set>
</head>
<body>

	<video id="video" width="300" height="250"></video>
	<canvas id="canvas" width="300" height="225"></canvas>
	用户名：
	<input type="text" name="phoneNum" id="phoneNum">
	<button id="snap">拍照</button>
	<button id="submit">提交</button>
	<script type="text/javascript">
		var context = canvas.getContext("2d");
		//当DOM树构建完成的时候就会执行DOMContentLoaded事件  
		window.addEventListener("DOMContentLoaded", function() {
			//获得Canvas对象  
			var canvas = document.getElementById("canvas");
			//获得video摄像头区域  
			var video = document.getElementById("video");
			var videoObj = {
				"video" : true
			};
			var errBack = function(error) {
				console.log("Video capture error: ", error.code);
			};
			//获得摄像头并显示到video区域  
			if (navigator.getUserMedia) { // Standard  
				navigator.getUserMedia(videoObj, function(stream) {
					video.src = stream;
					video.play();
				}, errBack);
			} else if (navigator.webkitGetUserMedia) { // WebKit-prefixed  
				navigator.webkitGetUserMedia(videoObj, function(stream) {
					video.src = window.webkitURL.createObjectURL(stream);
					video.play();
				}, errBack);
			} else if (navigator.mozGetUserMedia) { // Firefox-prefixed  
				navigator.mozGetUserMedia(videoObj, function(stream) {
					video.src = window.URL.createObjectURL(stream);
					video.play();
				}, errBack);
			}
		}, false);
		// 触发拍照动作  
		document.getElementById("snap").addEventListener("click", function() {
			context.drawImage(video, 0, 0, 300, 225);
		});

		$("#submit").click(function() {
			var canvas = document.getElementById("canvas");
			var dataUrl = canvas.toDataURL();
			var phoneNum = $('#phoneNum').val();
			$.ajax({
				type : "POST",
				url : "url",
				data : {
					"imgBase46" : dataUrl,
					"phoneNum" : phoneNum
				},
				dataType : "json",
				success : function(data) {
				},
				error : function() {
				}
			});
		});
	</script>

</body>
</html>