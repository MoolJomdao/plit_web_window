function initStateMessage()
{
	var id = $("#toolbar > input").attr("value");
    var inPencil = false; // 펜슬에 마우스가 드러가있나?
    var isClicked = false; // 상태메세지 수정아이콘을 클릭했나?
    var isHoverchecked = false; // 상태메세지 수정하고 체크버튼위에 올라와 있냐
    
    $("#user_state_message").hover(
        function() // mouseenter
        {            
            // 상태메세지 hover 시 수정 아이콘  등장
            $("#user_state_message").append("<div class='pencil'></div>");
            $(".pencil").css({
                position : "absolute",
                backgroundImage : "url('myPage/pencil.png')",
                backgroundSize : "100%",
                width : "26px",
                height : "26px",
                right : "2px",
                bottom : "2px",
                opacity : "0",
            });
            
            
            // 수정 아이콘 클릭 시
            $(".pencil").click( 
                function()
                {
                    if( !isClicked )
                    {
                        $("#user_userInfo_box").append("<div id='input_box'>" + 
                                                       "<input id='input_text' type='text' name='message'></input>" + 
                                                       "<div id='input_text_button'></div>" +
                                                       "<div id='exit'></div>" + 
                                                       "</div>");
                                                        
                        // 전체 감싸는 박스
                        $("#input_box").css({
                            position : "absolute",
                            width : "320px",
                            height : "30px",
                            padding : "5px 5px",
                            
                            right : "0",
                            marginTop : "5px",
                            backgroundColor : "white",
                            opacity : "0.8",
                        });
                        // 글쓰느곳
                        $("#input_text").css({
                            width : "245px",
                            height : "28px",
                            float : "left",
                            marginBottom : "5px",
                            paddingLeft : "5px",
                            
                            backgroundColor : "rgba(255, 255, 255, 0.1)",
                            borderTop : "0",
                            borderBottom : "0.2px solid gray",
                            borderLeft : "0",
                            borderRight : "0",
                        });
                        
                        // 완료 버튼
                        $("#input_text_button").css({
                            width : "30px",
                            height : "30px",
                            float : "left",
                            marginLeft : "5px",
                            
                            backgroundImage : "url('myPage/checked.png')",
                            backgroundSize : "contain",
                            backgroundRepeat : "no-repeat",
                            backgroundPosition : "center center",
                            opacity : "0.5",
                        });
                        // 취소버튼
                        $("#exit").css({
                            width : "30px",
                            height : "30px",
                            float : "left",
                            marginLeft : "5px",
                            
                            backgroundImage : "url('myPage/cancel.png')",
                            backgroundSize : "contain",
                            backgroundRepeat : "no-repeat",
                            backgroundPosition : "center center",
                            opacity : "0.5",
                        });
                        
                        $("#input_text").focus();
                        
                        // 글쓰는곳에서 포커스 없어지면
                        $("#input_text").blur(
                            function()   
                            {
                            	if( !isHoverchecked )
                            	{
	                                $("#input_box").remove();
	                                isClicked = false;
                            	}
                            }
                        );
                        
                        // 체크버튼 hover
                        $("#input_text_button").hover(
                            function() // enter
                            {
                                 $("#input_text_button").css({
                                     opacity : "1",
                                 });
                                 isHoverchecked = true;
                            },
                            
                            function() // leave
                            {
                                 $("#input_text_button").css({
                                     opacity : "0.5",
                                 });
                                 isHoverchecked = false;
                            }
                        );
                        // 체크버튼 클릭 시
                        $("#input_text_button").click(
                            function()   
                            {
                            	if( $("#input_text").val().length > 0 ) // val로 태그사이에 text 가져옴, 상태메세지에 아무것도 안적었으면 서버로 안보냄
                            	{
	                                $.ajax({
	                                    type : "POST",
	                                    url : ".data", // 서버에 .data로 서블릿을 찾아간다
	                                    // data : type 으로 어떤 메소드를 사용할건지 적어준다, 뒤에는 전달할 인자, 사용자가 쓴 text를 인자로 보내기
	                                    data : { "type" : "setStateMessage", "id" : id, "message" : $("#input_text").val() }, 
	                                    dataType : "text",
	
	                                    success : function( result ) // 상태메세지 변경을 성공햇을 시
	                                    {
	                                    	if( parseInt( result ) == 1 ) // 1이면 성공 0이면 실패
	                                    	{
	                                    		$("#user_state_message").text( $("#input_text").val() ); // 적었던걸로 바꿔주공
	                                    	}
	                                    	
	                                    	$("#input_box").remove(); // 변경하는창 없애기
	                                        isClicked = false;
	                                        
	                                    },
	
	                                    error : function()
	                                    {
	                                        alert(" 상태메세지 변경 실패 ");
	                                    }
	                                });
                            	}
                            	else
                            		alert("글을 입력해주세요.");
                            }
                        );
                        
                        // 취소버튼 hover
                        $("#exit").hover(
                            function() // enter
                            {
                                 $("#exit").css({
                                     opacity : "1",
                                 });
                            },
                            
                            function() // leave
                            {
                                 $("#exit").css({
                                     opacity : "0.5",
                                 });
                            }
                        );
                        // 취소버튼 클릭 시
                        $("#exit").click(
                            function()   
                            {
                                $("#input_box").remove();
                                isClicked = false;
                            }
                        );
                    }
                    else
                        isClicked = true;
                    
                }
            );
            // 수정 아이콘 hover, leave 시
            $(".pencil").hover(
                function() // enter
                {
                    inPencil = true;
                    $(this).stop(true);
                    $(this).css({
                        opacity : "1"
                    });
                },
                function() // leave
                {
                    inPencil = false;
                    $(this).css({
                        opacity : "0.5"
                    });
                }
            );
            
            if( !inPencil )
            {
                $(".pencil").animate({
                    opacity : "0.5"
                },100);
            }
        },
        function() // mouseleave
        {    
            if( !inPencil ) // 마우스가 펜슬에 없을때만 삭제해라
            {
                $(".pencil").animate(
                    {
                        opacity : "0.0"
                    },
                    100, 
                    function()
                    {
                        $(this).remove();
                    }
                );
            }
        }
        
    );
}