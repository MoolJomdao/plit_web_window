
window.onload = function()
{
    initProfileImage(); // 프로필사진 초기화
    initStateMessage(); // 상태메세지 초기화
    initFriendList();
    
	var id = $("#toolbar > input").attr("value");
		
	var height = screen.height - 250;
	var width = window.innerWidth;
	$.menu();
	$.changeMenu(width);
	$("#wrapper").css('height', height );

}

// 부모 height, 자식 height로 자식 vertical margin 조정
function resizeShowImage( parent, child )
{
    if( parent != undefined && child != undefined ) // 사진 확대 중이면 
    {
        /** margin-top, bottom 브라우저에 맞게 resize **/
        var valueOfMargin = ((parent.clientHeight - child.clientHeight)/2); // 부모 height, 자기자신 height 계산하여 margin 구하기
        child.style.margin = ( ( valueOfMargin > 0 ) ? valueOfMargin : valueOfMargin * -1 ) + "px auto"; // -가 나오지 않도록
    }
}

//창크기 변화 감지 ( 창 사이즈가 변할때 마다 호출 )
$( window ).resize(function() {

	var width = window.innerWidth;
	$.changeMenu(width);
	
});
