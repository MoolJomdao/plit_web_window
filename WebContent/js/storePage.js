$(document).ready(function(){

	$("html").css("backgroundImage", "url('../icon/store_background.png')"); // 
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
			updateSettingData( "storeNameChange.store", result, $settingItem.eq( index ) );	
		});
	});

	$setting_menu.eq(1).click(function(){
		// 메세지 변경 
		index = $(this).index();
		result = bootbox.prompt({
		    title: "변경할 메세지를 입력해주세요 X)",
		    inputType: 'textarea',
		    callback: function (result) {
		        // 입력 확인 후 이벤트 
		        updateSettingData( "storeMessageChange.store", result, $settingItem.eq( index ) );	
		    }
		});
	});

	$setting_menu.eq(2).click(function(){
		// 위치 변경 
		index = $(this).index();
		result = bootbox.prompt("위치를 입력해주세요 XD", function(result){ console.log(result);
			// 입력 확인 후 이벤트 
			updateSettingData( "storeLocationChange.store", result, $settingItem.eq( index ) );	
		});
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
		return true;
	}
	else
	{
		// 입력 사항 없음 
		return false;
	}
}

function updateSettingData( url, result, $item )
{
	if( checkEmpty(result) )
	{
		// 문자열이 있을 경우 서버로 전송
		 $.ajax({
	        type : "POST", 
	        url : url, // url을 서버로 보내주면 지정 서블릿이 실행
	        data : { "result": result }, // 서버에서 사용할 메소드를 type 에다가 넣어준다
	        
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

function buttonEvent()
{
	var $buttons = $(".button > button");

	$buttons.eq(0).click(function(){
		location.href = "./storeBoard.html";
	}); 

	$buttons.eq(1).click(function(){
		location.href = "./reviewPage.html";
	}); 

	$buttons.eq(2).click(function(){
		location.href = "./map.html";
	});   
}		