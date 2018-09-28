$(function(){
	var onClick = false;
	var $input = $(".navbar-right input");
	// search 버튼 클릭시
	$(".navbar-right").children().eq(1).click(function(){

		if(onClick == false)
		{
			onClick = true;
			$input.css({ width: "200px", paddingLeft: "10px" });
		}
		else
		{
			onClick = false;
			$input.css({ width: "0px", paddingLeft: "0px" });
			$input.val('');
		}
		
	})
})
