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
					<img src='<%=boardPhoto%>' onclick='imgClick(<%= arr.get(i).getBoardNum()%>)'> <!-- 대표 사진 -->
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
	
	<!-- 이미지 클릭 시 나타날 html -->
	<div id="img_section">
		<div id="album_section">   
			<div class="arrow"> <img src="icon/left-arrow1.png"> </div>      
			<ul>
			</ul>
			<div class="arrow"> <img src="icon/right-arrow1.png"></div>      
		</div>      
		<div id="currentImg"> <img id='cImg' src="icon/store_background.png"> <img src="icon/cancel2.png" id="back"> </div>
   	</div>
   	
	<form action='searchPage.bo' accept-charset='utf-8' method='POST'>
		<input id='searchStr' type='hidden' name='searchStr' value='1'>
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

	/** 게시글 사진 클릭 시 확대해서  **/
	$(function(){
	   var onClick = false;
	   var $input = $(".navbar-right input");
	   $("#img_section").css("min-height", screen.height);
	
	   // search 버튼 클릭시
	   $(".navbar-right").children().eq(1).click(function(){
	
	      if(onClick == false)
	      {
	         onClick = true;
	         $input.css({ width: "200px", paddingLeft: "10px" });
	      }
	      else
	      {
	         onClick = false;
	         $input.css({ width: "0px", paddingLeft: "0px" });
	         $input.val('');
	      }
	   })
	
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