<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<title></title>
	<script type="text/javascript" src="http://code.jquery.com/jquery-latest.min.js"></script>
	<script type="text/javascript" src="../js/bootstrap.min.js"></script>
	<script type="text/javascript" src="../js/storeSubPage.js"></script>
	<link rel="stylesheet" type="text/css" href="../css/bootstrap.css">
	<link rel="stylesheet" type="text/css" href="../css/storeSubPage.css">
</head>
<body>
	<div id="frontground"></div>
	<header>
		<nav class="navbar navbar-inverse">
		  <div class="container-fluid">
		    <div class="navbar-header">
		      <a class="navbar-brand" href="../html/storePage.html"> <img src="../icon/back.png" width="18px" padding="15px"> </a> <!-- 뒤로 가기 -->
		      <a class="navbar-brand" href="#"> review board </a>
		    </div>
		    <ul class="nav navbar-nav navbar-right">
		      <li><a href="#"> storeName </a></li>
		      <li><a href="#"> 리뷰 작성 </a></li>
		    </ul>
		  </div>
		</nav>
	</header> 

	<div id="wrapper">
		<div id="columns">
		<!-- class card 부분이 게시글 하나 --> 
			<div class="card">
				<img src="http://placehold.it/300x100" /> <!-- 대표 사진 -->
				<p> Roasted chicken breast with sage and spinach </p> <!-- 내용 -->
				<div> 
					<!-- 사용자 프로필 사진 -->
					<div class="profile"> 
					<img src="../icon/user.png"> 
					</div> 
					<!-- 사용자 이름 -->
					<h5> store_name </h5> 
				</div>
			</div>
	</div>

	<footer>
		<img src="../icon/up-arrow.png">
	</footer>
</body>
</html>