<<<<<<< HEAD
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.ArrayList"%>
<%@page import="bean.Read_Board_List"%>
<%@page import="db.GpsToAddress"%>
<!DOCTYPE html>
<html>
<head>
	<title></title>
	<script type="text/javascript" src="http://code.jquery.com/jquery-latest.min.js"></script>
	<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/bootbox.js/4.4.0/bootbox.min.js"></script>
	<script type="text/javascript" src="js/bootstrap.min.js"></script>
	<link rel="stylesheet" type="text/css" href="css/bootstrap.css">
	<link rel="stylesheet" type="text/css" href="css/storeSubPage.css">
<%
	/*
	private int boardNum;
	private String content;
	private double boardLatitude;
	private double boardLongitude;
	private String userId;
	public String storeName;
	private String userPhoto;
	private String boardPhoto;
	*/
	String id = (String)session.getAttribute("id");

	ArrayList<Read_Board_List> arr = new ArrayList<Read_Board_List>();
	arr = ( ArrayList<Read_Board_List> )request.getAttribute("storeBoards");
	String background = (String)request.getAttribute("background");
	String storeName = (String)request.getAttribute("storeName");
	String pageId = (String)request.getAttribute("boardId");
	
%>
</head>
<body>
	<div id="frontground"></div>
	<header>
		<nav class="navbar navbar-inverse">
		  <div class="container-fluid">
		    <div class="navbar-header">
		    	<a class="navbar-brand" href="#" onclick="backButtonClick(<%= pageId%>)"> <img src="icon/back.png" width="18px" padding="15px" > </a> <!-- 뒤로 가기 -->
		      	<a class="navbar-brand" href="#"> store board </a>
		    </div>
		    <ul class="nav navbar-nav navbar-right">
		      <li><a href="#"> <%= storeName %> </a></li>
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
				if( id.equals(pageId) )
				{
		%>
				<img src="icon/cancel2.png" class="del" onclick='boardDel(<%= boardNum %>)'>
				<form action="modifyBoard.store" class="modify" method='POST'>
					<input type='hidden' name='boardNum' value='<%= boardNum %>'>
					<input type="submit" value=""> 
				</form>
		<% 
				}
		
				if( boardPhoto != null )
				{
		%>
					<img src='<%=boardPhoto%>'> <!-- 대표 사진 -->
		<% 
				}
		%>
				<p> <%= content %> </p> <!-- 내용 -->
				<h6> <%= address %> </h6> <!-- 주소 -->
				
			</div>
		<%
			}
		%>
	</div>

	<footer>
		<img src="icon/up-arrow.png">
	</footer>
	
	<form action='storePage.bo' accept-charset='utf-8' method='POST'>
		<input type='hidden' name='userId' value=<%= pageId %>>
		<input id="send" type='submit' style="display:none;">
	</form>
</body>
</html>
<script>
	$(document).ready(function(){
	
		var h = $(document).height();
		$("html").css("backgroundImage", "url(<%= background %>)");	
		$("#frontground").css("height", h); // 불투명한 배경 높이
		
		$("footer > img").click(function(){
			$( 'html, body' ).animate( { scrollTop : 0 }, 400 );
		})
	});
	
	function backButtonClick( id ){
		$("#send").click();
	}
	
	function boardDel( boardNum ){
	      index = $(this).index();

	      bootbox.confirm("해당 게시글을 삭제 하시겠습니까?", function( result ){ 
		    	if( result )
		    	{
			         // 받아온 게시글 arraylist에서 index값으로 해당 게시글 번호 찾기
			         $.ajax({
			               type : "POST", 
			               url : "deleteBoard.bo", // url을 서버로 보내주면 지정 서블릿이 실행
			               data : { "boardNum": boardNum }, // 서버에서 사용할 메소드를 type 에다가 넣어준다
			               dataType : "json",
			               
			               success : function( data )
			               {
			                  window.location.reload( true );
			               },
			               
			               error : function()
			               {
			                   alert("변경 실패");
			               }                
			           });
		    	}
	      });
	   }
=======
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.ArrayList"%>
<%@page import="bean.Read_Board_List"%>
<%@page import="db.GpsToAddress"%>
<!DOCTYPE html>
<html>
<head>
	<title></title>
	<script type="text/javascript" src="http://code.jquery.com/jquery-latest.min.js"></script>
	<script type="text/javascript" src="js/bootstrap.min.js"></script>
	<link rel="stylesheet" type="text/css" href="css/bootstrap.css">
	<link rel="stylesheet" type="text/css" href="css/storeSubPage.css">
<%
	/*
	private int boardNum;
	private String content;
	private double boardLatitude;
	private double boardLongitude;
	private String userId;
	public String storeName;
	private String userPhoto;
	private String boardPhoto;
	*/
	String id = (String)session.getAttribute("id");

	ArrayList<Read_Board_List> arr = new ArrayList<Read_Board_List>();
	arr = ( ArrayList<Read_Board_List> )request.getAttribute("storeBoards");
	String background = (String)request.getAttribute("background");
	String storeName = arr.get(0).storeName;
	String pageId = arr.get(0).getUserId();
	
%>
</head>
<body>
	<div id="frontground"></div>
	<header>
		<nav class="navbar navbar-inverse">
		  <div class="container-fluid">
		    <div class="navbar-header">
		    	<a class="navbar-brand" href="#" onclick="backButtonClick(<%= pageId%>)"> <img src="icon/back.png" width="18px" padding="15px" > </a> <!-- 뒤로 가기 -->
		      	<a class="navbar-brand" href="#"> store board </a>
		    </div>
		    <ul class="nav navbar-nav navbar-right">
		      <li><a href="#"> <%= arr.get(0).storeName %> </a></li>
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
				<p> <%= content %> </p> <!-- 내용 -->
				<h6> <%= address %> </h6> <!-- 주소 -->
				<!-- <div> -->
					<!-- 사용자 프로필 사진 -->
					<!-- <div class="profile"> -->
					<!-- <img src=<%= //userPhoto %>> -->
					<!-- </div> -->
					<!-- 사용자 이름 -->
					<!-- <h5> <%= //arr.get(i).storeName %> </h5> -->
				<!-- </div>-->
				
			</div>
		<%
			}
		%>
	</div>

	<footer>
		<img src="icon/up-arrow.png">
	</footer>
	
	<form action='storePage.bo' accept-charset='utf-8' method='POST'>
		<input type='hidden' name='userId' value=<%= pageId %>>
		<input id="send" type='submit' style="display:none;">
	</form>
</body>
</html>
<script>
	$(document).ready(function(){
	
		var h = $(document).height();
		$("html").css("backgroundImage", "url(<%= background %>)");	
		$("#frontground").css("height", h); // 불투명한 배경 높이
		
		$("footer > img").click(function(){
			$( 'html, body' ).animate( { scrollTop : 0 }, 400 );
		})
	});
	
	function backButtonClick( id ){
		$("#send").click();
	}
>>>>>>> refs/remotes/origin/master
</script>