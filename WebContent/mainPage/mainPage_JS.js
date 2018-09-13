 var tbOffset = null;
 var profile = null;
 var $scrollBar = null;
$(document).ready(function() {
        
    tbOffset = $( '#bar' ).offset();
    profile = $("#profile");
    $scrollBar = $("#left");

    $.clickBoard();	   
    
	$("#write").click(function(){
		///TODO 글쓰기
    	location.href='writeBoard.bo?id=' + id;
    });
  
});

// 스크롤 감지
$(window).scroll(function() {
	if ( $( document ).scrollTop() - 100 > tbOffset.top ) 
	{
		profile.css("color", "#2d99ff");
		$('#bar').addClass('tbFixed');
		$('#left').addClass('wrapMargin');
    }
	else 
	{
		profile.css("color", "#FFFFFF");
		$('#bar').removeClass('tbFixed');
		$('#left').removeClass('wrapMargin');
	}
});

// 창 사이즈 변화 감지
$(window).resize(function(){

	if ( $(window).width() > "800" ) 
	{
		$scrollBar.css("height", $(window).height() - 42 );
	}
	
});

$.clickBoard = function(){
	$(".boardBox").click( function(){
		var index = $(this).index();
		var a = $(".num").eq(index).text();
		location.href='readBoard.bo?num='+ a;
	});
};
