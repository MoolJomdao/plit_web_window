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
    
    $("#delete").click( function() 
    {
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
        var str = textArea.replace(/(\r\n|\r|\n)/g, '\\r\\n');
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