<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.ArrayList"%>
<%@page import="bean.Read_Board_List"%>
<%@page import="db.GpsToAddress"%>

<!DOCTYPE html>
<html>
<head>
<META HTTP-EQUIV="contentType" CONTENT="text/html;charset=UTF-8">
	<title></title>
	<script type="text/javascript" src="http://code.jquery.com/jquery-latest.min.js"></script>
	<script type="text/javascript" src="js/bootstrap.min.js"></script>
	<script type="text/javascript" src="js/main.js"></script>
	<link rel="stylesheet" type="text/css" href="css/bootstrap.css">
	<link rel="stylesheet" type="text/css" href="css/main.css">
	
	<% 
		request.setCharacterEncoding("utf-8");
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
		String loginStat;
		HttpSession ses = request.getSession();
		
		ArrayList<Read_Board_List> arr = new ArrayList<Read_Board_List>();
		arr = ( ArrayList<Read_Board_List> )request.getAttribute("rbl");
		
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
		  	   <li> <a href="./html/writeBoard.jsp"> 글쓰기 </a></li>
		  	   <%
		  	   		if( ses.getAttribute("id") == null )
		  	   			loginStat = "Login";
		  	   		else
		  	   			loginStat = "Logout";
		  	   %>
		  	   		<li> <a href="SignIn.me"> <%= loginStat %> </a></li>
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
				String userPhoto = arr.get(i).getUserPhoto(); // 유저이미지
				userPhoto = ( ( !userPhoto.equals("No Photo") ) ? userPhoto : "./icon/user.png" ); // 있으면 , userPhoto, 없으면 기본 user.png
				
				String content = arr.get(i).getContent().replaceAll("\\r\\n|\\r|\\n","<br>"); // text에서 줄바꿈 문자 <br>로 변경
				
				String boardPhoto = arr.get(i).getBoardPhoto(); // 게시글 작성 글
				boardPhoto = new String(boardPhoto.getBytes("8859_1"),"utf-8");

				
				int boardNum =  arr.get(i).getBoardNum();
				String userId =  arr.get(i).getUserId();
				
				double lat = arr.get(i).getBoardLatitude();
				double lng = arr.get(i).getBoardLongitude();
				String address = "주소 없음";
				// 지오코더 이용하여 주소 값 가져오기
				if( lat != 0.0 && lng != 0.0 )
				{
					GpsToAddress GTA = new GpsToAddress( lat, lng );
					address = GTA.getAddress();
				}
		%>
				<div class="card">
		<% 
				if( boardPhoto != null )
				{
		%>
					<img src='<%=boardPhoto%>'> <!-- 대표 사진 -->
		<% 
				}
		%>
					<p> <%= content %>  </p> <!-- 내용 -->
					<h6> <%= address %> </h6> <!-- 주소(위도,경도로 찾아야함) -->
					<div> 
						<!-- 사용자 프로필 사진 -->
						<div class="profile"> 
						<form action='storePage.bo' accept-charset='utf-8' method='POST'>
							<input type='hidden' name='userId' value=<%= userId %>>
							<input type='submit'>
						</form>
							<img src=<%= userPhoto %> onclick=userIconButtonClick() > 
						</div> 
						<!-- 사용자 이름 -->
						<h5> <%= userId %>  </h5> 
					</div>
				</div>
		<% 
			} 
		%>
		</div>
	</div>
</body>
</html>

<script>
	function userIconButtonClick(){
		$('input[type=submit]').click();
	}
</script>