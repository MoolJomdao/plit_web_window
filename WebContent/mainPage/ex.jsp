<%@page import="db.UserBean"%>
<%@page import="db.BoardBean"%>
<%@page import="java.util.ArrayList"%>
<%@page import="dao.UserDao" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%
	String imageServerPath = "http://127.0.0.1:8080/plit/PlitImage/";
	String id = null;
	UserBean user = (UserBean)session.getAttribute("user");
	if(session.getAttribute("id")!=null){
		// 현재 사용자 id 받아오기
		id = (String)session.getAttribute("id");
	}
	ArrayList boardList = (ArrayList) request.getAttribute("boardlist");
	ArrayList<String> imgs = (ArrayList) request.getAttribute("imglist");
%>

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link rel="stylesheet" type="text/css" href="mainPage/mainPage_CSS.css">
	<script src="https://code.jquery.com/jquery-latest.min.js"></script>
	<script type="text/javascript" src="mainPage/mainPage_JS.js" charset='UTF-8'></script>
	<script type="text/javascript" src="mainPage/board_JS.js" charset='UTF-8'></script>
	<title> Main Page </title>
	
	<%
		UserDao userdao = new UserDao();
		int follow = userdao.follow(id);
		int follower = userdao.follower(id);
	%>
</head>

<body>		

<div id="toolbar"> 
	<input type="hidden" value="<%= id %>"  id="userid"> 
	<input type="hidden" value="<%= imageServerPath + user.getuserPhoto() %>" id="usericon"> 
</div>

<div id="bar">
	<img id="logo" src="icons/chat.png">
	<span> S N S </span>

	<img id="search" src="icons/search.png">
	<input type="text" placeholder="search..."></input>
</div>
	
<div id="wrap">
	<div id="left">
		<!-- 좌측 상단 프로필 부분 -->
		<div id="profile">
		<img <%= imageServerPath + user.getuserPhoto() %>>
		<div id="id"> <%= id %> </div>
		<div> status message </div>
		<table>
			<tr>
				<td> following </td>
				<td> followers </td>
			</tr>
			<tr>
				<td> <%=follow%> </td>
				<td> <%=follower%> </td>
			</tr>
		</table>
		</div>

		<!-- 좌측 테이블 메뉴 -->
		<div id="menu">
		<table>
			<tr>
				<td id="write"> <img src="icons/write.png" id="writeImg"> </img> <div> write </div> </td>
				<td> </td>
			</tr>
			<tr>

				<td> <img src="icons/friends.png"> <div> friends </div> </td>

			<!-- 세팅 -->
			<td id="setting"> 
					<div>
        				<div id="setting1"> <img src="icons/setting1.png" id="settingImg"> </img> </div>
        			</div>

   					<div>
        				<div id="setting2"> <img src="icons/setting2.png"> </img ></div>
        			</div>

        			<div style="position: relative; top: 82px;"> setting </div>
			</td>

			</tr>			
		</table>
		</div>
	</div>
	
	<div id="right">
		<!-- 게시글 -->
		<div id="board">
	
		<%
			if( boardList != null)
			{
				for( int i = 0; i < boardList.size(); i++ )
				{
					BoardBean bl = (BoardBean)boardList.get(i);
					String time = bl.getDateBoard().substring(0, 10);
		%>
		<div class="num" style="display:none"> <%= bl.getBoardNum() %> </div>
		<div class="boardBox">
			<table>
				<tr>
					<td rowspan="3" class="imgBox"> <img src=<%= imageServerPath + imgs.get(i) %> class="userImg"> </td>
				</tr>
				<tr> <td> <%= time %> </td> </tr>
				<tr> <td class="userName"> <%= bl.getId() %> </td> </tr>
				<tr> <td colspan="2"> <%= bl.getBoardContent() %> </td> </tr>
			</table>
			<table>
				<tr>
					<td class="btn"> <a href="#"> REPLY </a> <img src="icons/speech-bubble.png"> </td>
					<td class="btn"> <a href="#"> LIKE </a> <img src="icons/like.png"> </td> 
				</tr>
			</table>
		</div>
		
		
		<% 	 	 }
			}
		%>
		</div>

		<div  id="ad">
			<div class="title"> <img src="icons/like.png"> 추천 페이지 </div>
		</div>
	</div>
</div>
</body>
</html>