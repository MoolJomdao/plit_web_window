function initFriendList()
{
	var id = $("#toolbar > input").attr("value");
	
    $.ajax({
        type : "POST", 
        url : ".data", // .data를 url로 서버로 보내주면 DataController서블릿이 실행
        data : { "type" : "getFriendList", "id" : id}, // 서버에서 사용할 메소드를 type 에다가 넣어준다
        dataType : "json",
        
        success : function( jsonArray )
        {
            for( key in jsonArray ) // foreach문 jsonArray 마지막 인덱스까지 key 값을 반환해준다.
            {
                var user_photo = jsonArray[ key ].user_photo;
                var friend_id = jsonArray[ key ].id;
                var message = jsonArray[ key ].message;
                
                $("#user_friend").append("<div class='friend_list'>" +
                                            "<div class='friend_left'>" +
                                                 "<div class='friend_profile_image" + key + "'></div>" +
                                                 "<div class='friend_id'>" + friend_id + "</div>" +
                                             "</div>" +
                                             "<div class='friend_right'>" +
                                                 "<div rowspan='2' class='friend_message'>" + message + "</div>" +
                                             "</div>" + 
                                         "</div>");
                
                if( user_photo != null ) // 친구 프로필사진이 있을 시
	                $(".friend_profile_image" + key ).css({
	                    backgroundImage : "url('" + user_photo + "')",
	                });
                else
                	$(".friend_profile_image" + key ).css({
	                    backgroundImage : "url('myPage/test.jpg')",
	                });
                
                // 친구 이미지는 각각 업로드 되야하기 때문에 foreach문 안에서 해준다
                $(".friend_profile_image" + key ).css({
                    width : "50%px",
                    height : "50px",
                    margin : "5px 25% 2px 25%",
                    lineHeight : "50px",
                    float : "none",
                    backgroundSize : "100%",
                    backgroundPosition : "center center",
                    backgroundRepeat : "no-repeat",
                });
            }
            
            $(".friend_list").css({
                width : "98%",
                height : "80px",
                margin : "4px 1%",
                textAlign : "center",
                borderRadius: "7px",

            });
            
            // 친구 목록 왼쪽 영역
            $(".friend_left").css({
                width : "30%",
                height : "80px",
                float : "left",
                borderRadius: "7px 0 0 7px",
                backgroundColor : "white",
            });
            
            
            $(".friend_id").css({
                width : "100%",
                height : "23px",
                lineHeight : "23px",
                overflow : "hidden",
                textOverflow: "ellipsis",
                whiteSpace : "nowrap",
            });
    
            // 친구 목록 오른쪽 영역
            $(".friend_right").css({
                width : "69%",
                height : "80px",
                backgroundColor : "white",
                float : "left",
                borderRadius: "0 7px 7px 0",
                marginLeft : "1%",
            });
            
            $(".friend_message").css({
                width : "80%",
                height : "80px",
                margin : "0 10%",
                lineHeight : "80px",
                overflow : "hidden",
                textOverflow: "ellipsis",
                whiteSpace : "nowrap",
            });
        },
        
        error : function()
        {
        	// 친구가 없다는거임 ... ㅠ
        }
        
    });
}