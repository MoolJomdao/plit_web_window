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
		/*
			private int boardNum;
			private String content;
			private String dateBoard;
			private int good;
			private double boardLatitude;
			private double boardLongitude;
			private String userId;
			private String userPhoto;
			private int category;
			private int commentCnt;
		*/
		HttpSession ses = request.getSession();
		
		ArrayList<Read_Board_List> arr = new ArrayList<Read_Board_List>();
		arr = ( ArrayList<Read_Board_List> )request.getAttribute("rbl");
		
		System.out.println( arr.size() );
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
		  	   <%
		  	   		if( ses.getAttribute("id") == null )
		  	   		{
		  	   %>
  			   		<li> <a href="#"> Login </a></li>
		  	   <%
		  	   		}
		  	   		else
		  	   		{
		  	   %>
		  	   		<li> <a href="#"> Logout </a></li>
		  	   <% 
		  	   		}
		  	   %>
		  	</ul>
		  </div>
		</nav>
	</header>
	<div id="wrapper">
		<div id="columns">
		
		<!-- class card 부분이 게시글 하나 --> 
		<% 
			for( int i=0; i<arr.size(); i++ )
			{
		%>
				<div class="card">
					<img src=<%= arr.get(i).getBoardPhoto() %> /> <!-- 대표 사진 -->
					<p> <%= arr.get(i).getContent() %>  </p> <!-- 내용 -->
					<h6> 위치 ( 주소 ) </h6> <!-- 주소(위도,경도로 찾아야함) -->
					<div> 
						<!-- 사용자 프로필 사진 -->
						<div class="profile"> 
						<img src=<%= arr.get(i).getUserPhoto() %> > 
						</div> 
						<!-- 사용자 이름 -->
						<h5> <%= arr.get(i).getUserId() %>  </h5> 
					</div>
				</div>
		<% 
			} 
		%>
		</div>
	</div>
</body>
</html>