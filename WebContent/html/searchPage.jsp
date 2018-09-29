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
	<link rel="stylesheet" type="text/css" href="css/bootstrap.css">
	<link rel="stylesheet" type="text/css" href="css/searchPage.css">
	
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
		     	<a class="navbar-brand" href="mainPageAction.bo"> SNS </a>
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
				
				int boardNum =  arr.get(i).getBoardNum();
				String userId =  arr.get(i).getUserId();
				
				double lat = arr.get(i).getBoardLatitude();
				double lng = arr.get(i).getBoardLongitude();
				String address = "주소 없음";
				// 지오코더 이용하여 주소 값 가져오기
				if( lat != 0.0 && lng != 0.0 )
				{
					GpsToAddress GTA = new GpsToAddress( lat, lng );
					address = GTA.getAddress().replace("대한민국 ", "");
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
							<input class="send" type='submit'>
						</form>
							<img src=<%= userPhoto %> onclick=userIconButtonClick(<%= i %>) > 
						</div> 
						<!-- 사용자 이름 -->
						<h5 onclick=userIconButtonClick(<%= i %>)> <%= arr.get(i).storeName %>  </h5> 
					</div>
				</div>
		<% 
			} 
		%>
		</div>
	</div>
	<form action='searchPage.bo' accept-charset='utf-8' method='POST'>
		<input id='searchStr' type='hidden' name='searchStr'>
		<input id="searchBtn" type='submit' style='display:none;'>
	</form>
</body>
</html>

<script>
	function userIconButtonClick( i ){
		var inputs = document.getElementsByClassName("send");
		$(inputs[i]).click();
	}

	$(function(){
		var onClick = false;
		var $input = $(".navbar-right input");
		// search 버튼 클릭시
		$(".navbar-right").children().eq(1).click(function(){

			if(onClick == false)
			{
				onClick = true;
				$input.css({ width: "200px", paddingLeft: "10px" });
			}
			else
			{
				var str = $input.val();
				if( $input.val() != "" ){ // 검색어를 입력 했으면
					$('#searchStr').val( $input.val() ); // input 태그에 검색할 단어 넣고
					$('#searchBtn').click(); // searchPage.bo 호출
					return;
				}
				
				onClick = false;
				$input.css({ width: "0px", paddingLeft: "0px" });
				$input.val('');
				
			}
			
		})
	})
</script>