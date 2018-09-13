$(document).ready( function() {

	var h = screen.height;
	var w = screen.width;
	var width = window.innerWidth;
	$.menu();
	$.changeMenu(width);

	var box = $("#box");
	if( w > box.width() )
	{
		var boxW = ( w - box.width() ) / 2;
		box.css("marginLeft", boxW);
	}
	if( h - 200 > box.height() )
	{
		var boxH = ( h - box.height() - 200 ) / 2;
		box.css("marginTop", boxH);
	}
});

// 창크기 변화 감지 ( 창 사이즈가 변할때 마다 호출 )
$( window ).resize(function() {

	var width = window.innerWidth;
	$.changeMenu(width);
});
