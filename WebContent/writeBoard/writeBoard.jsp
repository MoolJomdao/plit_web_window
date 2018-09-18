<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
	<title></title>
	<link rel="stylesheet" type="text/css" href="css/bootstrap.css">
	<link rel="stylesheet" type="text/css" href="css/writeBoard.css">
	<script type="text/javascript" src="http://code.jquery.com/jquery-latest.min.js"></script>
	<script type="text/javascript" src="js/bootstrap.js"></script>
</head>
<body>
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
  			   <li> <a href="#"> 작성 취소 </a></li>
  			   <li> <a href="#"> 작성 완료 </a></li>
		  	</ul>
		  </div>
		</nav>
	</header>

	<div class="wrap">
		<!-- 위치 부분 -->
		<div class="location_section">
			<input placeholder="위치 입력..."> </input>
			<button> </button>
		</div>

		<div class="content_section"> 
			<textarea></textarea>
		</div>

		<div class="media_section">
			<button class="btn"> 사진 추가 </button>
			<button class="btn"> 동영상 추가 </button>
		</div>
	</div>

</body>
</html>