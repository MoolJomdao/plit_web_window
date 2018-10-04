<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="bean.Comment" %>
<!DOCTYPE html>
<html>
<%
	String id = (String)session.getAttribute("id");
	
	String prevPage = null;
	prevPage = (String)request.getAttribute("prevPage");

	Comment comment = (Comment)request.getAttribute("comment");
	
	String content = comment.comment_content;
	content = content.replaceAll("\\r\\n|\\r|\\n","<br>"); // text에서 줄바꿈 문자 <br>로 변경
	
	String boardNickname = (String)request.getAttribute("boardNickname"); // 이 리뷰가 달리는 board의 사용자 닉네임
	int commentNum = (Integer)request.getAttribute("commentNum");
	String userNickname = (String)request.getAttribute("userNickname"); // 현재 사용자의 닉네임
	String boardId = (String)request.getAttribute("boardId"); // 리뷰가 달리는 boardId
	int boardNum = (Integer)request.getAttribute("boardNum");
%>
<head>
	<link rel="stylesheet" type="text/css" href="css/bootstrap.css">
	<link rel="stylesheet" type="text/css" href="css/writeReview.css">
	<script type="text/javascript" src="http://code.jquery.com/jquery-latest.min.js"></script>
	<title> 리뷰 작성 페이지 ( Host ) </title>
	<script type="text/javascript">
	
		var isdelPhoto = "false"; // 수정때 사진으 삭제했으면
		
		$(document).ready(function(){
			upload();
			setId();
			
			// 키자마자 수정될 글을 채워야한다.
		    var str = "<%=content%>"; // 글내용
		    $("#textarea").val(str);
		    $("#inp").val("<%=comment.comment_nickname%>");
		    var imgUrl = "<%=comment.comment_photo%>";
		    
		    if( imgUrl != "No Photo")
		    {
	            $("#img > button").css("visibility", "visible");
		    }
		    
		    /********************************/

		    $("#submit").click( function( event )
		    {
		        event.preventDefault(); // 기존 리다이렉트 되는 이벤트 제거
		        var boardNum = "<%=boardNum%>";
		        var commentNickname = "<%=userNickname%>";
		        if( id == undefined )
		        {
		        	alert("로그인이 되어있지 않습니다. 재로그인 요망");
		        	return;
		        }
		        var textArea = $("#textarea").val(); // 글내용
		        var str = textArea.replace(/(\r\n|\r|\n)/g, '\\r\\n');

		        // 이미지 데이터 가져오기 
		        var formData = new FormData( $("#upload")[0] );
		        
		        formData.append("img", $("#upload")[0].files[0] );
		        
		        formData.append("boardNum", boardNum);
		        formData.append("commentNum", "<%=commentNum%>");
		        formData.append("commentNickname", commentNickname);
		        formData.append("content", textArea);
		        formData.append("isdelPhoto", isdelPhoto);
		        // 이미지 formData 넣기, 이미지 더 보내고 싶으면 formData.append('img', $(this)[1] ); 첫번째면 0, 두번째 이미지면 1 세번째 이미지면 2 반드시 key 값을 'img'로
		        // 글도 같이 보낼 수 있다 formData.append('key', 'value' );

		        $.ajax({
		            url: 'modifyReview.store',
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
		            		$("#success").click();
		            	}
		            	else{
		            		alert("게시글 작성 실패");
		            	}
		            }
		          });

		        return false; // 기존 리다이렉트 되는 이벤트 제거
		    } );
		    

		    $("#del").click( function( event ) 
		    {
		    	isdelPhoto = "true";
		    	event.preventDefault();
		    	$("#img").css("backgroundImage", "url('')");
                $("#img > button").css("visibility", "hidden");
		        
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
		});

		function upload()
		{
			var $upload = $("#upload");

			$("#uploadbtn").click(function(){
				event.preventDefault();
				$upload.click();
			});

			/* 파일 업로드에 변화가 일어났을 때 */
			$upload.change(function(){
				var fileList = this.files; // input type='file' 에서 사용자가 선택한 이미지들 가져오기	

				fileLoad( fileList[0] );
				           
				function fileLoad( file )
	           {
	              var fileReader = new FileReader();
	              fileReader.readAsDataURL( file ); // 한개 선택할꺼니 0번째 url로 

	               // 사용자 선택한 이미지 로드 시
	               fileReader.onload = function ()
	               {
	                   $("#img").css("backgroundImage", "url(" + fileReader.result + ")");
	                   $("#img > button").css("visibility", "visible");
	               }
	           }
			});
		}

		function setId()
		{
			var $id = $("#inp");
			var userid = $("#id").text();
			$id.val(userid);
		}

	</script>
</head>
<body>
	<!-- 게스트 이름, 비번, 사진은 한장만 추가될 것, 위치는 클릭한 게시글에서 해당 위치를 가져올 것 -->

	<form>
		<header class="header scrollNav">
			<nav class="navbar navbar-inverse">
			  <div class="container-fluid">
			    <div class="navbar-header">
			    	<!-- 로고 부분 ( 브랜드 명 )-->
			     	<a class="navbar-brand" href="#"> PLIT </a>
			    </div>

			    <!-- 우측 nav -->
			  	<ul class="nav navbar-nav navbar-right">
	  			   <li> <input id="cancel" type="button" value="작성 취소" onclick="javascript:history.back(-1)"> </li>
	  			   <li> <input id="submit" type="button" value="작성 완료"> </li>      
			  	</ul>
			  </div>
			</nav>
		</header>

		<div id="wrap">
			<div id="box">
				<!---------------------------->
				<div id="a">
					<div id="title"> REVIEW	 </div>
					<div class="inpbox"> 
						<label for="inp" class="inp">
						  <span id="id"><!-- 사용자 아이디 적어 주세요. ( 띄어쓰기 하지말것 )--><%=userNickname%></span>
						  <input type="text" id="inp" placeholder="&nbsp;" name="id" disabled="disabled">
						  <span class="label"> NAME </span>
						  <span class="border"> </span>
						</label>
					</div>
				</div>
				<!---------------------------->
				<div id="b">				
					<textarea id='textarea' name="context"></textarea>
				</div>
				<!---------------------------->
				<div id="c">
					<div id="location"> <img src="icon/shop.png"> <%=boardNickname%> </div>
					<div id="img_section">
						<button id="uploadbtn"> <div> 사진 추가 </div> </button>
						<input id="upload" type="file" accept="image/*" name="boardPhoto[]" enctype="multipart/form-data">
						<!-- 이미지 뜨는 곳 -->
						<div id="img" style="background-image : url('<%=comment.comment_photo%>')">
							<button id="del" onclick="javascript:location.href='#'"> 삭제 </button>
						</div>
					</div>
				</div>
			</div>
		</div>
	</form>
	
	
					<form action="reviewPage.store" method="POST" accept-charset="utf-8">
						<input type="hidden" name="boardId" value="<%=boardId%>">
						<input type="hidden" name="boardNum" value="<%=boardNum%>">
						<input type="hidden" name="prevPage" value="<%=prevPage%>">
						<input id='success' type="submit" value="">
					</form>
</body>
</html>