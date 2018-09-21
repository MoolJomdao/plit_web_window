<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<title></title>
	<script type="text/javascript" src="http://code.jquery.com/jquery-latest.min.js"></script>
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
		});

		// 브라우저 창 변화화
		$(window).resize(function(){
			var h = $(document).height();
			$("#frontground").css("height", h); // 불투명한 배경 높이

			/* 버튼높이 조정 ( 정사각형 )*/
			var btnH = $("button").width();
			$("button").css("height", btnH);	    
		});
	</script>

	<style type="text/css">

		#setting{
			float: right;
			width: 25px;
		}

		#setting_section{
			float: right;
		}

		#setting_menu{
			list-style: none;
			display: none;
		}

		#setting_menu > li{
			float: left;
			margin-top: 1px;
			margin-right: 15px;
			font-size: 1.15em;
			cursor: pointer;
		}
		@media (max-height: 600px) {
			#setting_section{
				float: none;
				width: 100%;
			}
		}
	</style>

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
				<td class="bigFont bigGap"> STORE NAME </td>
				<td class="middleFont bigGap rightText"> +999 <img src="icon/comment.png"></td>
			</tr>
			<tr>
				<td colspan="2">
					<div id="message">
						Lorem ipsum dolor sit amet, consectetuer adipiscing elit. 
 						Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque. 
 					</div>
				</td>
			</tr>
			<tr>
				<td colspan="2" id="location">
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