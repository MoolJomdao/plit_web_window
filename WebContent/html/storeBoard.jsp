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
				String boardId =  arr.get(i).getUserId();
				
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
					<img src='<%=boardPhoto%>' onclick='imgClick(<%= arr.get(i).getBoardNum()%>)'> <!-- 대표 사진 -->
		<% 
				}
		%>
				<p> <%= content %> </p> <!-- 내용 -->
				<h6> <%= address %> </h6> <!-- 주소 -->
				<div class='bottom'>
					<form action="reviewPage.store" method="POST" accept-charset="utf-8">
						<input type="hidden" name="boardId" value="<%=boardId%>">
						<input type="hidden" name="boardNum" value="<%=boardNum%>">
							<input type="hidden" name="prevPage" value="storeBoard.store">
						<input type="submit" value="">
					</form>
				</div>
			</div>
		<%
			}
		%>
	</div>

	<footer>
		<img src="icon/up-arrow.png">
	</footer>
	
	<!-- 이미지 클릭 시 나타날 html -->
	<div id="img_section">
		<div id="album_section">   
			<div class="arrow"> <img src="icon/left-arrow1.png"> </div>      
			<ul>
			</ul>
			<div class="arrow"> <img src="icon/right-arrow1.png"></div>      
		</div>      
		<div id="currentImg"> <img id='cImg' src=""> <img src="icon/cancel2.png" id="back"> </div>
   	</div>
	
	<%
		if( !id.equals("Guest") )
		{
	%>
	<form action='storePage.bo' accept-charset='utf-8' method='POST'>
	<% 
		}
		else
		{
	%>
	<form action='mainPageAction.bo' accept-charset='utf-8' method='POST'>
	<%
		}
	%>
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

	/** 게시글 사진 클릭 시 확대해서  **/
	$(function(){
	   var onClick = false;
	   var $input = $(".navbar-right input");
	   $("#img_section").css("min-height", screen.height);
	
	   // 이미지 상세 보기에서 다시 돌아가기
	   $("#back").click(function(){
	      $("#img_section").css("display", "none");
	   });
	
	   // 앨범 영역 화살표 이벤트 
	   $(".arrow").eq(0).click(function(){
	      // 왼쪽
	      scrollAlbum(300, "left", 300);
	   });
	
	   $(".arrow").eq(1).click(function(){
	      // 오른쪽 
	      scrollAlbum(300, "right", 300);
	   });
	})

	function scrollAlbum(space, direction, duration){
	   /*
	    * space: 엘범 움직이는 거리 정도
	    * direction: 왼쪽 / 오른쪽 
	    * duration: 슬라이드 속도
	   */
	
	   if( direction == "left")
	   {
	      var a = $("#album_section > ul").scrollLeft() - space;
	   }
	   else
	   {
	      var a = $("#album_section > ul").scrollLeft() + space;
	   }
	
	   $("#album_section > ul").animate({
	       scrollLeft: a
	   }, duration);
	}

	// .card > img 클릭시 이미지 상세 보기
	function imgClick( boardNum ){
		   
		   $.ajax({
	            type : "POST", 
	            url : "getBoardPhotos.bo", // url을 서버로 보내주면 지정 서블릿이 실행
	            data : { "boardNum": boardNum }, // 서버에서 사용할 메소드를 type 에다가 넣어준다
	            dataType : "json",
	            
	            success : function( data )
	            {
	            	var arr = data;
	            	
	            	for( var i=0; i<arr.length; i++)
	            	{
	            		var str = "<li class='albumImg no-drag'> <img src='" + arr[i] + "'> </li>"
	  	      	      	// 이미지 불러와서 
	  	      	      	$("#album_section ul").append(str);
	            	}
            		$('#currentImg #cImg').attr('src', arr[0]);

            		// 작은 사진 클릭시 큰 화면으로 띄우기
            		$(".albumImg").click(function(){
            		   var index = $(this).index();
            		
            		   var imgsrc = $(".albumImg > img").eq(index).attr("src");
            		   $("#currentImg > img").eq(0).attr("src", imgsrc);
            		});	
	            },
	            
	            error : function()
	            {
	                alert("변경 실패");
	            }                
	        });
	      $("#img_section").css("display", "block");
	 }
</script>