<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title> SIGN IN FORM </title>
	<link rel="stylesheet" type="text/css" 
		href="SignIn/SignIn_CSS.css">
		
	<script src="http://code.jquery.com/jquery-latest.min.js"></script>
	<script>
		$(document).ready(function(){
			if( <%=request.getAttribute("join") != null%>)
			{
				alert('회원가입 성공')
			}
			$('#signIn').click(function(){
				$('form').attr("action", "LoginUserAction.me");
			});
			$('#signUp').click(function(){
				$('form').attr("action", "SignUp.me");
			});
		})
	</script>
</head>
<body>

<div class="box">
<!-- main 박스 -->

	<div id="left">
	<!-- 왼쪽  -->
		<span> S N S </span> <br>
		<span> Social Network Service <br>
		<span> Let's talk together :) </span> 
	</div>

	<form method = "post">
	<div class="loginForm">
	<!-- 오른쪽  -->
		<input type="text" class="text" placeholder="e-mail*" name="id"> </input>
		<input type="password" class="text" placeholder="password*"  name="pw"> </input>
		<input type="submit" value="SIGN UP" class="submit" id ="signUp"> </input>
		<input type="submit" value="SIGN IN" class="submit" id ="signIn"> </input>
	</div>
	</form>
</div>

</body>
</html>