<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<%
	String id = (String)session.getAttribute("id");
	//String id = request.getParameter("id");
%>
<head>
	<title></title>
	<script type="text/javascript" src="http://maps.googleapis.com/maps/api/js?sensor=false"></script>
	<script async defer src="https://maps.googleapis.com/maps/api/js?key=AIzaSyB1O3_xjyaGDGbrQ38g-i3kjUpCgjuWEWw&callback=initMap"
  	type="text/javascript"></script>
  
	<link rel="stylesheet" type="text/css" href="../css/bootstrap.css">
	<link rel="stylesheet" type="text/css" href="../css/writeBoard.css">
	<script type="text/javascript" src="http://code.jquery.com/jquery-latest.min.js"></script>
	<script type="text/javascript" src="../js/bootstrap.js"></script>
	<script type="text/javascript" src="../js/writeBoard.js"></script>
</head>
<body>
	<input id="id_value" type="hidden" value=<%= id %>>

	<form action="./writeBoard.bo" method="post">
		<!-- 해더 부분 -->
		<header class="header scrollNav">
			<nav class="navbar navbar-inverse">
			  <div class="container-fluid">
			    <div class="navbar-header">
			    	<!-- 로고 부분 ( 브랜드 명 )-->
			     	<a class="navbar-brand" href="../mainPageAction.bo"> SNS </a>
			    </div>

			    <!-- 우측 nav -->
			  	<ul class="nav navbar-nav navbar-right">
	  			   <li> <input id="cancel" type="button" value="작성 취소"></li>
	  			   <li> <input id="submit" type="button" value="작성 완료"> </li>      
			  	</ul>
			  </div>
			</nav>
		</header>

		<div class="wrap">
			<!-- 위치 부분 -->
			<div class="location_section">
				<div>위치 입력...</div>
				<button id="location_button" type="button" value="위치받아오기"> </button>
				<input id="address" type="hidden" value="서울시">
				<input id="lat" type="hidden" value="0.0">
                <input id="lng" type="hidden" value="0.0">
			</div>

			<div class="content_section" id="text_box"> 
				<textarea id="text_area"></textarea>
			</div>

			<div class="media_section">
				<input id="upload" type="file" accept="image/*" name="boardPhoto[]" value="사진 불러오기" multiple enctype="multipart/form-data" >
				<button class="btn" id="upload_click"> 사진 추가 </button>
				<button class="btn"> 동영상 추가 </button>
	            <div id="image_box"></div>
			</div>
			
		</div>
	</form>
	
	<input type="hidden" id="sendEvent" onclick="sendEvent()"> <!-- 지도에서 전송 누를시 발생하는 이벤트를 위한 button -->
</body>
</html>

<script>
	var openWin;

	$(function(){
		// 구글 Map 열기
		$('.location_section button').click(function(){
			openMap();
		});
	});
	
	function openMap()
	{
	    // window.name = "부모창 이름"; 
	    window.name = "parentForm";
	    // window.open("open할 window", "자식창 이름", "팝업창 옵션");
	    openWin = window.open("writeMap.jsp",
	            "childForm", "width=" + (window.innerWidth/2) + "px, height=" + (window.innerHeight/2) + "px, resizable = yes, scrollbars = no");    
	}
	
	// Map에서 좌표 찍고, click줘서 발생시키는 이벤트
	function sendEvent(){
		var address = document.getElementById("address").value;
		
		document.querySelector(".location_section div").innerText = address; // 화면에 주소 보여주기
		//alert("address : " + address + "\n, lat : " + lat + "\n, lng : " + lng );
	}

</script>