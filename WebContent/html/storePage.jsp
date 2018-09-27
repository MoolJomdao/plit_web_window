<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<title></title>
	<script type="text/javascript" src="http://code.jquery.com/jquery-latest.min.js"></script>
	<script type="text/javascript" src="js/bootstrap.min.js"></script>
	<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/bootbox.js/4.4.0/bootbox.min.js"></script>
	<link rel="stylesheet" type="text/css" href="css/bootstrap.css">
	<link rel="stylesheet" type="text/css" href="css/storePage.css">
	<script type="text/javascript">

		$(document).ready(function(){

			$("html").css("backgroundImage", "url('icon/store_background.png')"); // 
			var h = $(document).height();
			$("#frontground").css("height", h); // 불투명한 배경 높이

			/* 버튼높이 조정 ( 정사각형 )*/
			var btnH = $("button").width();
			$("button").css("height", btnH);

			/* 스토어 페이지 설정 메뉴 애니메이션 */
			$("#setting").click(function(){
				$("#setting_menu").toggle(1000);
			});

			$(".button > button").eq(2).click(function(){
				location.href = "html/map.html";
			});   
			
			settingEvent();
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
		function settingEvent()
		{
			var $setting_menu = $("#setting_menu > li");
			var $settingItem = $(".settingItem");
			var index;
			var result;

			$setting_menu.eq(0).click(function(){
				// 가게명 변경 
				index = $(this).index();
				result = bootbox.prompt("변경할 가게명을 입력해주세요 :)", function(result){ console.log(result);
					// 입력 확인 후 이벤트 
					updateSettingData( result, $settingItem.eq( index ) );	
				});
			});

			$setting_menu.eq(1).click(function(){
				// 메세지 변경 
				var result;

				result = bootbox.prompt({
				    title: "변경할 메세지를 입력해주세요 X)",
				    inputType: 'textarea',
				    callback: function (result) {
				        console.log(result);
				        // 입력 확인 후 이벤트 
				    }
				});
			});

			$setting_menu.eq(2).click(function(){
				// 위치 변경 
				var result;
				result = bootbox.prompt("위치를 입력해주세요 XD", function(result){ console.log(result);
					// 입력 확인 후 이벤트 
				});
			});

			$setting_menu.eq(3).click(function(){
				// 배경 변경 

			});			
		}

		// prompt창 데이터 여부 확인
		// 데이터 있을 시 서버에 전송 후 변환
		function checkEmpty(str)
		{
			if( str )
			{
				// 입력 사항 있음	
				return true;
			}
			else
			{
				// 입력 사항 없음 
				return false;
			}
		}

		function updateSettingData( result, $item )
		{
			if( checkEmpty(result) )
			{
				// 문자열이 있을 경우 서버로 전송
				 $.ajax({
			        type : "POST", 
			        url : "storeNameChange.store", // url을 서버로 보내주면 지정 서블릿이 실행
			        data : { "storeName": result }, // 서버에서 사용할 메소드를 type 에다가 넣어준다
			        
			        success : function( data )
			        {
			        	$item.html(result);
			        },
			        
			        error : function()
			        {
			        	alert("변경 실패");
			        }		        
			    });
			}			
		}
	</script>
</head>
<body>	
	<div id="frontground"></div>

	<div class="wrap">
		<header>
			<a href="#"> SNS </a>
			<a href="#"> <img src="icon/setting3.png" id="setting"></a>
			<div id="setting_section">
				<ul id="setting_menu">
					<li> 가게명 변경 &nbsp; </li>
					<li> 메세지 변경 &nbsp; </li>
					<li> 위치 변경 &nbsp; </li>
					<li> 배경 변경 </li>
				</ul>
			</div>
		</header>

		<!-- 가게 정보 ( 이름, 코멘트 수, 설명, 주소 )-->
		<table>
			<tr>
				<td class="bigFont bigGap settingItem"> STORE NAME </td>
				<td class="middleFont bigGap rightText"> +999 <img src="icon/comment.png"></td>
			</tr>
			<tr>
				<td colspan="2">
					<div id="message" class="settingItem">
						Lorem ipsum dolor sit amet, consectetuer adipiscing elit. 
 						Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque. 
 					</div>
				</td>
			</tr>
			<tr>
				<td colspan="2" id="location" class="settingItem">
					대구 북구 복현동 000 - 00
				</td>
			</tr>
		</table>

		<!-- 버튼 ( 가게글, 리뷰, 지도 ) -->
		<div class="button">
			<button> <div> <a href="#"> STORE </a> </div> </button>
			<button> <div> <a href="#"> REVIEW </a> </div> </button>
			<button> <div> <a href="html/map.html"> LOCATION </a> </div> </button>
		</div>
	</div>
</body>
</html>