<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="bean.Read_Mypage"%>
<!DOCTYPE html>
<html>
<head>
<%
	request.setCharacterEncoding("utf-8");
	String id = (String)session.getAttribute("id");
	Read_Mypage mypage = (Read_Mypage)request.getAttribute("mypage");
	String clickId = (String)request.getAttribute("userId");
	mypage.message = mypage.message.replaceAll("\\r\\n|\\r|\\n","<br>"); // text에서 줄바꿈 문자 <br>로 변경
%>
   <title></title>
   <script type="text/javascript" src="http://code.jquery.com/jquery-latest.min.js"></script>
   <script type="text/javascript" src="js/bootstrap.min.js"></script>
   <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/bootbox.js/4.4.0/bootbox.min.js"></script>
   <link rel="stylesheet" type="text/css" href="css/bootstrap.css">
   <link rel="stylesheet" type="text/css" href="css/storePage.css">
   <script type="text/javascript">

   $(document).ready(function(){

       $("html").css("backgroundImage", "url(<%= (mypage.userPhoto != null) ? mypage.userPhoto  : "icon/store_background.png"%>)"); // 
       var h = $(document).height();
       $("#frontground").css("height", h); // 불투명한 배경 높이

       /* 버튼높이 조정 ( 정사각형 )*/
       var btnH = $("button").width();
       $("button").css("height", btnH);

       /* 스토어 페이지 설정 메뉴 애니메이션 */
       $("#setting").click(function(){
          $("#setting_menu").toggle(1000);
       });  
       
       settingEvent();
       buttonEvent();

    });

    // 브라우저 창 변화화
    $(window).resize(function(){
       var h = $(document).height();
       $("#frontground").css("height", h); // 불투명한 배경 높이

       /* 버튼높이 조정 ( 정사각형 )*/
       var btnH = $("button").width();
       $("button").css("height", btnH);    
    });

    /********************************************************************/
    /************************설정창 클릭 이벤트***************************/
    /*******************************************************************/
    // prompt에 문자열이 있을 경우 문자열과 인덱스 값을 서버로 전송
    // 인덱스 값은 어떤 항목이 변경하는지 알려주는 역할
    // 전송된 데이터는 데이터 베이스에 저장.
    // 데이터 저장 후 웹 페이지로 따로 받는 데이터는 없음.
    // 단지 sucess된 경우 prompt에서 받은 값을 바로 적용하는 방식
       var index;
    function settingEvent()
    {
       var $setting_menu = $("#setting_menu > li");
       var $settingItem = $(".settingItem");
       var result;

       $setting_menu.eq(0).click(function(){
	          // 가게명 변경 
	          index = $(this).index();
	          result = bootbox.prompt("변경할 가게명을 입력해주세요 :)", function(result){ console.log(result);
	          if( !checkEmpty(result) )
	          {
	        	// 입력 확인 후 이벤트 
	              updateSettingData( "storeNameChange.store", {"storeName" : result}, $settingItem.eq( index ) ); 
	          }
          });
       });

       $setting_menu.eq(1).click(function(){
          // 메세지 변경 
          index = $(this).index();
          result = bootbox.prompt({
              title: "변경할 메세지를 입력해주세요 X)",
              inputType: 'textarea',
              callback: function (result) {
            	  if( !checkEmpty(result) )
    	          {
                  	// 입력 확인 후 이벤트
	        		result = result.replace('/(\r\n|\r|\n)/g', '\\r\\n');
                  	updateSettingData( "storeMessageChange.store", {"message" : result}, $settingItem.eq( index ) );
    	          }
              }
          });
       });

       $setting_menu.eq(2).click(function(){
		    // 위치 변경 
		    index = $(this).index();
			openMap( "html/writeMap.jsp" );
       });

       $setting_menu.eq(3).click(function(){
          // 배경 변경 
           event.preventDefault(); // 기존 리다이렉트 되는 이벤트 제거
            $("#upload").click();
       });      
       
       $("#upload").change( function()
       {
           var fileList = this.files; // input type='file' 에서 사용자가 선택한 이미지들 가져오기          
            fileLoad( fileList[0] );
            
            function fileLoad( file )
            {
               var fileReader = new FileReader();
               fileReader.readAsDataURL( file ); // 한개 선택할꺼니 0번째 url로 

                // 사용자 선택한 이미지 로드 시
                fileReader.onload = function ()
                {
                   $("html").css("backgroundImage", "url("+ fileReader.result + ")");
                }
				
                /* ajax로 파일데이터 보내기 */
                var formData = new FormData( $("#upload")[0] );
                
                formData.append("img", $("#upload")[0].files[0] );
                
                var id = "<%=id%>";
                if( id == undefined )
                {
                	alert("로그인이 되어있지 않습니다. 재로그인 요망");
                	return;
                }
                formData.append("id", id);
                $.ajax({
                    url: 'storeBackgroundChange.store',
                    type: 'POST',
                    enctype: "multipart/form-data",
                    data: formData,
                    async: false,
                    cache: false,
                    contentType: false,
                    processData: false,
                    success: function ( returndata ) {
                    	if( parseInt( returndata ) != -1 ){
                    		alert("배경사진 변경 완료");
                    	}
                    	else{
                    		alert("배경사진 변경 실패");
                    	}
                    }
                  });
            }
               
          } );   

    }

    // prompt창 데이터 여부 확인
    // 데이터 있을 시 서버에 전송 후 변환
    function checkEmpty(str)
    {
       if( str )
       {
          // 입력 사항 있음   
          return false;
       }
       else
       {
          // 입력 사항 없음 
          return true;
       }
    }

    function updateSettingData( url, result, $item )
    {
       if( !checkEmpty(result) )
       {
          // 문자열이 있을 경우 서버로 전송
           $.ajax({
               type : "POST", 
               url : url, // url을 서버로 보내주면 지정 서블릿이 실행
               data : result, // 서버에서 사용할 메소드를 type 에다가 넣어준다
               
               success : function( data )
               {
            	   data = data.replace(/(\r\n|\r|\n)/g, '<br>');
                  $item.html( data );
               },
               
               error : function()
               {
                  alert("변경 실패");
               }              
           });
       }
    }

    function buttonEvent()
	{
		var $buttons = $(".button > button");

		$buttons.eq(0).click(function(){
			$('#storeBoardButton').click();
		}); 

		$buttons.eq(1).click(function(){
			$('#reviewPageButton').click();
		}); 

		$buttons.eq(2).click(function(){
			openMap( "html/map.jsp" );
		});   
	}
    
   	/**************** 맵 띄우는 부분 ************************/
   	var openWin;

   	function openMap( openFile )
   	{
   	    // window.name = "부모창 이름"; 
   	    window.name = "parentForm";
   	    // window.open("open할 window", "자식창 이름", "팝업창 옵션");
   	    openWin = window.open(openFile,
   	            "childForm", "width=" + (window.innerWidth/2) + "px, height=" + (window.innerHeight/2) + "px, resizable = yes, scrollbars = no");    
   	}

   	// Map에서 좌표 찍고, click줘서 발생시키는 이벤트
   	function sendEvent(){
   		var address = document.getElementById("address").value;
   		var lat = document.getElementById("lat").value;
   		var lng = document.getElementById("lng").value;
        result = { "lat" : lat, "lng" : lng };
        // 입력 확인 후 이벤트 
        updateSettingData( "storeLocationChange.store", result, $(".settingItem").eq( index ) ); 
   		
   	}
   </script>
</head>
<body>   
   <div id="frontground"></div>

   <div class="wrap">
      <header>
         <a href="./mainPageAction.bo"> SNS </a>
         <%
         	if( id.equals( mypage.userId ))
         	{
         %>
         <a href="#"> <img src="icon/setting3.png" id="setting"></a>
         <div id="setting_section">
            <ul id="setting_menu">
               <li> 가게명 변경 &nbsp; </li>
               <li> 메세지 변경 &nbsp; </li>
               <li> 위치 변경 &nbsp; </li>
               <li> 배경 변경 </li>
               <input id="upload" type="file" accept="image/*" name="boardPhoto[]" value="사진 불러오기" enctype="multipart/form-data" >
            </ul>
         </div>
          <%
         	}
          %>
      </header>

      <!-- 가게 정보 ( 이름, 코멘트 수, 설명, 주소 )-->
      <table>
         <tr>
            <td class="bigFont bigGap settingItem"> <%=mypage.storeName%> </td>
         </tr>
         <tr>
            <td colspan="2">
               <div id="message" class="settingItem">
               <%=mypage.message%>
               </div>
            </td>
         </tr>
         <tr>
			<input id="address" type="hidden" value="">
			<input id="lat" type="hidden" value=<%= mypage.lat %>>
            <input id="lng" type="hidden" value=<%= mypage.lng %>>
            <td colspan="2" id="location" class="settingItem">
             	<%=mypage.address%>
            </td>
         </tr>
      </table>

      <!-- 버튼 ( 가게글, 리뷰, 지도 ) -->
      <div class="button">
         <button> <div> <a href="#"> STORE </a> </div> </button>
         <button> <div> <a href="#"> REVIEW </a> </div> </button>
         <button> <div> <a href="#"> LOCATION </a> </div> </button>
      </div>
   </div>
	<input type="hidden" id="sendEvent" onclick="sendEvent()"> <!-- 지도에서 전송 누를시 발생하는 이벤트를 위한 button -->
	
	<!-- storeBoard 누르면 id값 가지고 이동해야해서 form 태그 사용 -->
	<form action='storeBoard.store' method='POST' accept-charset='utf-8'>
		<input type='hidden' name='storeBoardId' value='<%= clickId %>'>
		<input id='storeBoardButton' type='submit' >
	</form>
	
	<!-- reviewPage 누르면 id값 가지고 이동해야해서 form 태그 사용 -->
	<form action='reviewPageFromStorePage.store' method='POST' accept-charset='utf-8'>
		<input type='hidden' name='userId' value='<%= clickId %>'>
		<input type="hidden" name="prevPage" value="storePage.bo">	
		<input id='reviewPageButton' type='submit' >
	</form>
</body>
</html>
