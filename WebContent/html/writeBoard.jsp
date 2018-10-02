<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="bean.Read_Mypage"%>
<%@page import="dao.Dao"%>
<%@page import="db.GpsToAddress"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<%
	String id = (String)session.getAttribute("id");

	Dao dao = new Dao();
	Read_Mypage myPage = dao.read_myPage(id);
	
	double lat = myPage.lat;
	double lng = myPage.lng;
	String address = "주소 없음";
	// 지오코더 이용하여 주소 값 가져오기
	if( lat != 0.0 && lng != 0.0 )
	{
		GpsToAddress GTA = new GpsToAddress( lat, lng );
		address = GTA.getAddress().replace("대한민국 ", "");
	}
%>
<head>
	<title></title>
	<script type="text/javascript" src="http://maps.googleapis.com/maps/api/js?sensor=false"></script>
	<script async defer src="https://maps.googleapis.com/maps/api/js?key=AIzaSyB1O3_xjyaGDGbrQ38g-i3kjUpCgjuWEWw&callback=initMap"
  	type="text/javascript"></script>
  
	<link rel="stylesheet" type="text/css" href="../css/bootstrap.css">
	<link rel="stylesheet" type="text/css" href="../css/writeBoard.css">
	<script type="text/javascript" src="http://code.jquery.com/jquery-latest.min.js"></script>
	<script type="text/javascript" src="../js/bootstrap.js"></script>
</head>
<body>
	<input id="id_value" type="hidden" value=<%= id %>>

	<form action="./writeBoard.bo" method="post">
		<!-- 해더 부분 -->
		<header class="header scrollNav">
			<nav class="navbar navbar-inverse">
			  <div class="container-fluid">
			    <div class="navbar-header">
			    	<!-- 로고 부분 ( 브랜드 명 )-->
			     	<a class="navbar-brand" href="mainPageAction.bo" id="sns"> SNS </a>
			    </div>

			    <!-- 우측 nav -->
			  	<ul class="nav navbar-nav navbar-right">
	  			   <li> <input id="cancel" type="button" value="작성 취소"></li>
	  			   <li> <input id="submit" type="button" value="작성 완료"> </li>      
			  	</ul>
			  </div>
			</nav>
		</header>

		<div class="wrap">
			<!-- 위치 부분 -->
			<div class="location_section">
				<div><%=address%></div>
				<button id="location_button" type="button" value="위치받아오기"> </button>
				<input id="address" type="hidden" value="<%=address%>">
				<input id="lat" type="hidden" value="<%=lat%>">
                <input id="lng" type="hidden" value="<%=lng%>">
			</div>

			<div class="content_section" id="text_box"> 
				<textarea id="text_area"></textarea>
			</div>

			<div class="media_section">
				<input id="upload" type="file" accept="image/*" name="boardPhoto[]" value="사진 불러오기" multiple enctype="multipart/form-data" >
				<button class="btn" id="upload_click"> 사진 추가 </button>
				<button class="btn"> 동영상 추가 </button>
				<button class="btn" id="delete"> 사진삭제 </button>
	            <div id="image_box"></div>
			</div>
			
		</div>
	</form>
	
	<input type="hidden" id="sendEvent" onclick="sendEvent()"> <!-- 지도에서 전송 누를시 발생하는 이벤트를 위한 button -->
	
	<form action='../mainPageAction.bo' accept-charset='utf-8' method='POST'>
		<input id="backStoreBoard" type='submit'>
	</form>
</body>
</html>

<script>
	var openWin;

	$(function(){
		// 구글 Map 열기
		$('.location_section button').click(function(){
			openMap();
		});
	});
	
	function openMap()
	{
	    // window.name = "부모창 이름"; 
	    window.name = "parentForm";
	    // window.open("open할 window", "자식창 이름", "팝업창 옵션");
	    openWin = window.open("writeMap.jsp",
	            "childForm", "width=" + (window.innerWidth/2) + "px, height=" + (window.innerHeight/2) + "px, resizable = yes, scrollbars = no");    
	}
	
	// Map에서 좌표 찍고, click줘서 발생시키는 이벤트
	function sendEvent(){
		var address = document.getElementById("address").value;
		
		document.querySelector(".location_section div").innerText = address; // 화면에 주소 보여주기
		//alert("address : " + address + "\n, lat : " + lat + "\n, lng : " + lng );
	}
	var countOfImage = 0;

	$(document).ready(function(){
		
	    $("#upload_click").click( function( event ) {
	        event.preventDefault(); // 기존 리다이렉트 되는 이벤트 제거
	        $("#upload").click();
	    });
		/************************************************
	    					이미지파일을 선택
		*************************************************/
		$("#upload").change( function()
		{
		    var fileList = this.files; // input type='file' 에서 사용자가 선택한 이미지들 가져오기
		    
	        for( var i=0; i<fileList.length; i++ )
	        {
	            fileLoad( fileList[i] );
	        }
	        
	        function fileLoad( file )
	        {
	           var fileReader = new FileReader();
	           fileReader.readAsDataURL( file ); // 한개 선택할꺼니 0번째 url로 

	            // 사용자 선택한 이미지 로드 시
	            fileReader.onload = function ()
	            {
	                $("#image_box").append("<div id='photo" + countOfImage + "'></div>");
	                $("#photo" + countOfImage).css({
	                    width : "200px",
	                    height : "100%",
	                    marginLeft : "10px",
	                    backgroundImage : "url(" + fileReader.result + ")",
	                    backgroundSize : "100%",
	                    backgroundRepeat : "no-repeat",
	                    backgroundPosition : "center center",
	                    display : "inline-block",
	                });
	                
	                countOfImage++;
	            }
	        }
	        
		} );

		$("#cancel").click(function(event){
	        event.preventDefault(); // 기존 리다이렉트 되는 이벤트 제거
			$('#backStoreBoard').click();
		});
		$("#sns").click(function(event){
	        event.preventDefault(); // 기존 리다이렉트 되는 이벤트 제거
			$('#backStoreBoard').click();
		});
		
	    $("#delete").click( function() 
	    {
	        event.preventDefault(); // 기존 리다이렉트 되는 이벤트 제거
	        $("#image_box").empty();
	        
	        var agent = navigator.userAgent.toLowerCase(); //userAgent 에서 현재 사용하는 브라우저를 알 수 있다.
	        if ( agent.indexOf("msie") != -1 )
	        {
	            // ie 일때 input[type=file] init. 
	            $("#upload").replaceWith( $("#upload").clone(true) ); 
	        } 
	        else 
	        { 
	            // other browser 일때 input[type=file] init. 
	            $("#upload").val(""); 
	        }
	    });
	    
	    $("#submit").click( function( event )
	    {
	        var id = $("#id_value").val();
	        if( id == undefined )
	        {
	        	alert("로그인이 되어있지 않습니다. 재로그인 요망");
	        	return;
	        }
	        var textArea = $("#text_area").val(); // 글내용
	        var str = textArea.replace('/(\r\n|\r|\n)/g', '\\r\\n');
	        var locationText = $("#location_text").val();
			var lat = document.getElementById("lat").value;
			var lng = document.getElementById("lng").value;
	        event.preventDefault(); // 기존 리다이렉트 되는 이벤트 제거

	        // 이미지 데이터 가져오기 
	        var formData = new FormData( $("#upload")[0] );
	        
	        for( var i=0; i<countOfImage; i++ )
	        {
	            formData.append("img"+i, $("#upload")[0].files[i] );
	        }
	        
	        formData.append("id", id);
	        formData.append("lat", lat);
	        formData.append("lng", lng);
	        formData.append("content", textArea);
	        // 이미지 formData 넣기, 이미지 더 보내고 싶으면 formData.append('img', $(this)[1] ); 첫번째면 0, 두번째 이미지면 1 세번째 이미지면 2 반드시 key 값을 'img'로
	        // 글도 같이 보낼 수 있다 formData.append('key', 'value' );

	        $.ajax({
	            url: '../writeBoard.bo',
	            type: 'POST',
	            enctype: "multipart/form-data",
	            data: formData,
	            async: false,
	            cache: false,
	            contentType: false,
	            processData: false,
	            success: function ( returndata ) {
	            	if( parseInt( returndata ) != -1 ){
	            		alert("게시글 작성 완료");
	            		location.href = '../mainPageAction.bo'
	            	}
	            	else{
	            		alert("게시글 작성 실패");
	            	}
	            }
	          });

	        return false; // 기존 리다이렉트 되는 이벤트 제거
	    } );
	    
	});
    

</script>