<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.ArrayList"%>
<%@page import="bean.Read_Board_List"%>

<!DOCTYPE html>
<html>
<head>
	<title></title>
	<script type="text/javascript" src="http://code.jquery.com/jquery-latest.min.js"></script>
	<script type="text/javascript" src="js/bootstrap.min.js"></script>
	<script type="text/javascript" src="js/main.js"></script>
	<link rel="stylesheet" type="text/css" href="css/bootstrap.css">
	<link rel="stylesheet" type="text/css" href="css/main.css">
	
	<% 
		ArrayList<Read_Board_List> arr = new ArrayList<Read_Board_List>();
		arr = (ArrayList<Read_Board_List>) request.getAttribute("rbl");
		
		System.out.println(session.getAttribute("id"));
		
		System.out.println(arr.size());
	%>
	
	
</head>
<body>

	<!-- 해더 부분 -->
	<header class="header">
		<nav class="navbar navbar-inverse">
		  <div class="container-fluid">
		    <div class="navbar-header">
		    	<!-- 로고 부분 ( 브랜드 명 )-->
		     	<a class="navbar-brand" href="#"> SNS </a>
		    </div>
		    
		    <!-- 메뉴 부분 
		    <ul class="nav navbar-nav">
		      <li><a href="#"> </a></li>
		    </ul>
			-->

		    <!-- 우측 nav -->
		  	<ul class="nav navbar-nav navbar-right">
		  	   <li> <input> </input> </li>
  			   <li> <a href="#"> <img src="icon/search.png"> </a></li>
		  	   <li> <a href="#"> 글쓰기 </a></li>
  			   <li> <a href="#"> Login </a></li>
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
				<h6> 위치 ( 주소 ) </h6> <!-- 주소 -->
				<div> 
					<!-- 사용자 프로필 사진 -->
					<div class="profile"> 
					<img src="icon/user.png"> 
					</div> 
					<!-- 사용자 이름 -->
					<h5> store_name </h5> 
				</div>
			</div>
			
			<!-- class card 부분이 게시글 하나 --> 
			<div class="card">
				<img src="http://placehold.it/300x100" /> <!-- 대표 사진 -->
				<p> Roasted chicken breast with sage and spinach </p> <!-- 내용 -->
				<h6> 위치 ( 주소 ) </h6> <!-- 주소 -->
				<div> 
					<!-- 사용자 프로필 사진 -->
					<div class="profile"> 
					<img src="icon/user.png"> 
					</div> 
					<!-- 사용자 이름 -->
					<h5> store_name </h5> 
				</div>
			</div>
			
			<!-- class card 부분이 게시글 하나 --> 
			<div class="card">
				<img src="http://placehold.it/300x100" /> <!-- 대표 사진 -->
				<p> Roasted chicken breast with sage and spinach </p> <!-- 내용 -->
				<h6> 위치 ( 주소 ) </h6> <!-- 주소 -->
				<div> 
					<!-- 사용자 프로필 사진 -->
					<div class="profile"> 
					<img src="icon/user.png"> 
					</div> 
					<!-- 사용자 이름 -->
					<h5> store_name </h5> 
				</div>
			</div>
			
			<!-- class card 부분이 게시글 하나 --> 
			<div class="card">
				<img src="http://placehold.it/300x300" /> <!-- 대표 사진 -->
				<p> Roasted chicken breast with sage and spinach </p> <!-- 내용 -->
				<h6> 위치 ( 주소 ) </h6> <!-- 주소 -->
				<div> 
					<!-- 사용자 프로필 사진 -->
					<div class="profile"> 
					<img src="icon/user.png"> 
					</div> 
					<!-- 사용자 이름 -->
					<h5> store_name </h5> 
				</div>
			</div>
			
			<!-- class card 부분이 게시글 하나 --> 
			<div class="card">
				<img src="http://placehold.it/300x100" /> <!-- 대표 사진 -->
				<p> Roasted chicken breast with sage and spinach </p> <!-- 내용 -->
				<h6> 위치 ( 주소 ) </h6> <!-- 주소 -->
				<div> 
					<!-- 사용자 프로필 사진 -->
					<div class="profile"> 
					<img src="icon/user.png"> 
					</div> 
					<!-- 사용자 이름 -->
					<h5> store_name </h5> 
				</div>
			</div>
		</div>
	</div>
</body>
</html>