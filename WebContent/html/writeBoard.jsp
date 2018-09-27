<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<%
	String id = (String)session.getAttribute("id");
	//String id = request.getParameter("id");
%>
<head>
	<title></title>
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
			     	<a class="navbar-brand" href="#"> SNS </a>
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
				<input id="lat" type="hidden" value="36.322473">
                <input id="lng" type="hidden" value="127.412501">
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
</body>
</html>

<script>
	var openWin;

	function openChild()
	{
	    // window.name = "부모창 이름"; 
	    window.name = "parentForm";
	    // window.open("open할 window", "자식창 이름", "팝업창 옵션");
	    openWin = window.open("writeMap.jsp",
	            "childForm", "width=1280, height=960, resizable = yes, scrollbars = no");    
	    
	    
	}


	$(function(){
		$('.location_section button').click(function(){
			//openChild();
			location.href = "writeMap.jsp";
		});
	});
</script>