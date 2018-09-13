<%@page import="db.BoardBean"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" type="text/css" href="board/readBoard_CSS.css">
<script src="https://code.jquery.com/jquery-latest.min.js" charset='UTF-8'></script>
<script type="text/javascript" src="board/readBoard_JS.js" charset='UTF-8'></script>
<script type="text/javascript" src="mainPage/menu_JS.js" charset='UTF-8'></script>
</head>
<body>

	<%
		BoardBean board = (BoardBean) request.getAttribute("board");
		String time = board.getDateBoard().substring(0, 10);
		String id = (String)session.getAttribute("id");
	%>
	<div id="toolbar"> <input type="hidden" value="<%= id %> id="userid"> </div>

	<div id="box">
		<div id="right">
			<div id="profile">
			<img src="board/userIcon.jpg" id="profileImg">
			<div id="profileId"> <%= board.getId() %> </div>
			</div>
	
			<div class="sub"> 대구 북구 복현동 </div>
			<div class="sub"> <%= time %> </div>
			<div id="imgBox"> </div>
		</div>
	
		<div>
			<div id="boardBox">
				<div id="content"> <%= board.getBoardContent() %> </div>
				<img src="board/heart.png">
			</div>
		</div>
	
		<div id="reply">
		<table>
			<tr>
				<td width="70px"> <img src="board/userIcon.jpg" class="replyIcon"/> </td>
				<td colspan="2" class="replyContent"> 내용 </td>
			</tr>
			<tr>
				<td class="replyId"> user id </td>
				<td class="replyDate"> YYYY-MM-DD </td>
				<td class="replyHit"> <img src="board/heart.png" width="24px" height="24px"> </td>
			</tr>
		</table>
	</div>
	</div>
</body>
</html>