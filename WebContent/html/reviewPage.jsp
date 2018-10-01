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
	String boardId = (String)request.getAttribute("boardId");
	int boardNum = (Integer)request.getAttribute("boardNum");
	
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
		      <li><a href="#" id="makeReview"> 리뷰 작성 </a></li>
					<form action="writeReviewPage.store" method="POST" accept-charset="utf-8">
						<input type="hidden" name="boardId" value="<%=boardId%>">
						<input type="hidden" name="boardNum" value="<%=boardNum%>">
						<input type="submit" value="" style="display:none" id="makeButton">
					</form>
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
				if( id.equals(commentId) ) // 접속중인 사용자와 리뷰의 작성자가 같을경우
				{
		%>
				<img src="icon/cancel2.png" class="del" onclick="delComment('<%=commentNum%>', '<%=id%>', '<%=pw%>')">
				<form action="modifyReviewPage.store" class="modify" method='POST'>
					<input type='hidden' name='boardNickname' value='<%= boardNickname %>'>
					<input type='hidden' name='userNickname' value='<%= userNickname %>'>
					<input type='hidden' name='boardId' value='<%= boardId %>'>
					<input type='hidden' name='boardNum' value='<%= boardNum %>'>
					<input type='hidden' name='commentNum' value='<%= commentNum %>'>
					<input type="submit" value="" onclick="modifyReviewClick('<%=pw%>')"> 
					<input type="hidden" name="prevPage" value="<%=prevPage%>">	
					<input id="sendModify" type="submit" style="display:none"> 
				</form>
				
		<% 
				}
		
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
	
	<%
		if(prevPage.equals("mainPageAction.bo"))
		{
	%>
	<form action="mainPageAction.bo" method="POST" accept-charset="utf-8">
	<%
		}
		else
		{
	%>
	<form action="storeBoard.store" method="POST" accept-charset="utf-8">
			<input type="hidden" name="storeBoardId" value="<%=boardId%>">
	<% 
		}
	%>
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
		
		$("#makeReview").click(function(event){
			event.preventDefault();
			$("#makeButton").click();
		});
	});

	function delComment( commentNum, id, pw ){ // 만약 id 가 Guest면 비밀번호를 물어봐야한다.
	
		  if( id != "Guest" )
		  {
		      bootbox.confirm("해당 리뷰글을 삭제 하시겠습니까?", function ( result ){ 
			    	if( result )
			    	{
				         // 받아온 게시글 arraylist에서 index값으로 해당 게시글 번호 찾기
				         $.ajax({
				               type : "POST", 
				               url : "deleteReview.store", // url을 서버로 보내주면 지정 서블릿이 실행
				               data : { "commentNum": commentNum, "boardNum" : <%=boardNum%> }, // 서버에서 사용할 메소드를 type 에다가 넣어준다
				               
				               success : function( data )
				               {
				                  window.location.reload( true );
				               },
				               
				               error : function()
				               {
				                   alert("리뷰 삭제 실패");
				               }                
				           });
			    	}
			  });
		  }
		  else{
	          var result = bootbox.prompt("해당 리뷰글을 삭제 하시겠습니까? 비밀번호를 입력해주세요.", function(result){ console.log(result);
	          if( result == pw )
	          {
	        	// 입력 확인 후 이벤트 
	        	  $.ajax({
		               type : "POST", 
		               url : "deleteReview.store", // url을 서버로 보내주면 지정 서블릿이 실행
		               data : { "commentNum": commentNum, "boardNum" : <%=boardNum%> }, // 서버에서 사용할 메소드를 type 에다가 넣어준다
		               
		               success : function( data )
		               {
		                  window.location.reload( true );
		               },
		               
		               error : function()
		               {
		                   alert("리뷰 삭제 실패");
		               }                
		           });
	          }
	          else
	          {
	        	  alert("비밀번호가 틀렸습니다.");
	          }
		  });
		}
	}
	
	function modifyReviewClick( pw )
	{
  	  event.preventDefault();
		if( "<%=id%>" == "Guest" ) // 게스트가 수정을 눌렀으면
		{
			bootbox.prompt("해당 리뷰글을 수정 하시겠습니까? 비밀번호를 입력해주세요.", function(result){ 
			  console.log(result);
			  
	          if( result == null )
	          {
	        	  event.preventDefault();
	          }
	          else if( result != pw )
	          {
	        	  alert("비밀번호가 틀렸습니다.");
	          }
	          else
	          {
	        	  $("#sendModify").click();  
	          }
			});
		}
		else
		{
			$("#sendModify").click();  
		}
	}
		  
	function clickBack(){
		$("#backBt").click();
	}
		  
</script>