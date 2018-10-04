<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="bean.Comment" %>
<%@page import="java.util.ArrayList"%>
<!DOCTYPE html>
<html>
<%
	String id = (String)session.getAttribute("id");

	String prevPage = null;
	prevPage = (String)request.getAttribute("prevPage");
	
	String boardNickname = (String)request.getAttribute("boardNickname");
	String userNickname = (String)request.getAttribute("userNickname");
	String userId = (String)request.getAttribute("userId");
	
	ArrayList<Comment> commentList = new ArrayList<Comment>();
	commentList = (ArrayList<Comment>)request.getAttribute("commentList");
	
	String background = null;
	background = (String)request.getAttribute("background");
%>
<head>
	<title></title>
	<script type="text/javascript" src="http://code.jquery.com/jquery-latest.min.js"></script>
	<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/bootbox.js/4.4.0/bootbox.min.js"></script>
	<script type="text/javascript" src="js/bootstrap.min.js"></script>
	<link rel="stylesheet" type="text/css" href="css/bootstrap.css">
	<link rel="stylesheet" type="text/css" href="css/storeSubPage.css">
</head>
<body>
	<div id="frontground"></div>
	<header>
		<nav class="navbar navbar-inverse">
		  <div class="container-fluid">
		    <div class="navbar-header">
		      <a class="navbar-brand" href="javascript:clickBack()"> <img src="icon/back.png" width="18px" padding="15px"> </a> <!-- 뒤로 가기 -->
		      <a class="navbar-brand" href="#"> review board </a>
		    </div>
		    <ul class="nav navbar-nav navbar-right">
		      <li><a href="#"> <%=boardNickname%> </a></li>
		    </ul>
		  </div>
		</nav>
	</header> 

	<div id="wrapper">
		<div id="columns">
		<!-- class card 부분이 게시글 하나 --> 
		<%
			for( int i=0; i<commentList.size(); i++)
			{
				String commentId = commentList.get(i).comment_id;
				String content = commentList.get(i).comment_content;
				content = content.replaceAll("\\r\\n|\\r|\\n","<br>"); // text에서 줄바꿈 문자 <br>로 변경
				
				String commentPhoto = commentList.get(i).comment_photo;

				String userPhoto = commentList.get(i).user_photo;
				int guestPhoto = commentList.get(i).guest_photo;
				if( guestPhoto != 0 ) // 게스트면
				{
					userPhoto = "icon/comment_" + guestPhoto + ".png";
				}
				String comment_nickname = commentList.get(i).comment_nickname;
				int commentNum = commentList.get(i).comment_num;
				String pw = commentList.get(i).comment_pw;
		%>
			<div class="card">
		<% 
				if( !commentPhoto.equals("No Photo"))
				{
		%>
				<img src="<%=commentPhoto%>" /> <!-- 대표 사진 -->
		<%
				}
		%>
				<p> <%=content%> </p> <!-- 내용 -->
				<div> 
					<!-- 사용자 프로필 사진 -->
					<div class="profile"> 
					<img src="<%=(userPhoto.equals("No Photo")) ? "icon/user.png" : userPhoto %>"> 
					</div> 
					<!-- 사용자 이름 -->
					<h5> <%=comment_nickname%> </h5> 
				</div>
			</div>
		<%
			}
		%>
		</div>
	</div>
	<footer>
		<img src="icon/up-arrow.png">
	</footer>
	
	<form action="storePage.bo" method="POST" accept-charset="utf-8">
			<input type="hidden" name="userId" value="<%=userId%>">
			<input id='backBt' type="submit" value="" style="display:none">
	</form>
</body>
</html>
<script>

	$(document).ready(function(){
		var isBackground = '<%= background %>';
		var h = $(document).height();
		if( isBackground != 'null')
			$("html").css("backgroundImage", "url(<%= background %>)");	
		
		$("#frontground").css("height", h); // 불투명한 배경 높이
		
		$("footer > img").click(function(){
			$( 'html, body' ).animate( { scrollTop : 0 }, 400 );
		})
	
	});

		  
	function clickBack(){
		$("#backBt").click();
	}
		  
</script>