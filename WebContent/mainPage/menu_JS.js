// 상단 메뉴 관련 함수
$.menu = function(){

	var toolbar = $("#toolbar");
	var id = $("#userid").attr("value");
	var photo = $("#usericon").attr("value");
	// toolbar 태그 추가	
	toolbar.append("<img src='mainPage/write.png' class='menu' id='write'>");
	toolbar.append("<img src='mainPage/setting.png' class='menu'>");	
	toolbar.append("<img src='mainPage/home.png' class='menu' id='home'>");
	toolbar.append("<span id='userId'>" + id + "</span>");
	toolbar.append("<img src='mainPage/icon.jpg' id='userIcon'/>");
	toolbar.append("<div class='searchBox'> </div>");
	$("#toolbar > .searchBox").append("<input type='text' class='searchTerm' placeholder='Search...'/>");
	$("#toolbar > .searchBox").append("<button type='submit' class='searchButton'>" + 
		                              "<img src='mainPage/search.png' id ='searchImg'> </button>");
	toolbar.append("<img src='mainPage/logo.png' id='logo'>");

	// 아이콘 애니메이션
	// hover시 서서히 크기 증가
	$('.menu').hover( function() {
		$(this).stop().animate({
			width: 28,
			height: 28,
			paddingTop: 15
		}, 500);		
	}, function(){
		$(this).stop().animate({
			width: 24,
			height: 24,
			paddingTop: 17
		}, 500);
	});
	
	$("#userIcon").click(function(){
    	location.href='myPage.bo?id=' + id;
    });
	$("#write").click(function(){
		///TODO 글쓰기
    	location.href='writeBoard.bo?id=' + id;
    });
	$("#home").click(function(){
    	location.href='mainPageAction.bo';
    });

	// searchBox 애니메이션
    $(".searchTerm").click(function(){
    	//TODO 검색창 클릭 시 : x표시로 전환
    	$("#searchImg").slideToggle(200);
    	$("#searchImg").attr('src','mainPage/close.png');
    	$("#searchImg").slideToggle(200);
    });
};

/**********************************************************/
// 반응형 관련 스크립트 ( width : window width )
$.changeMenu = function(width){

	if( width <= 600 )
	{	
		//TODO window width is less than 600px
	  	$('#map').fadeOut(); // 맵 사라지기
	  	$('#logo').css('display','none');
	  	// 검색 입력창 사라지기
	  	$('.searchTerm').stop().animate({
	  		width: 0,
	  	},500);

	  	// 검색 버튼 클릭시
	  	$('.searchButton').click(function(){
	  		// 작은 메뉴 아이콘, 사용자 정보 사라지기
	  		$('.menu').css('display','none');
	  		$('#userId').css('display','none');
	  		$('#userIcon').css('display','none');
	  		
	  		// 검색 입력창 늘어나기
	  		$('.searchBox').css('width', '98%');
	  		$('.searchBox').css('margin-left', '1%');
			$('.searchBox').css('margin-right', '1%');
	  		$('.searchTerm').stop().animate({
	  			width: "90%"
	  		},500);
	  	});

	  	// 검색입력창 사라지기
	  	$('.searchTerm').focusout(function(){
	  		$('.menu').css('display','block');
	  		$('#userId').css('display','block');
	  		$('#userIcon').css('display','block');

			$('.searchBox').css('width', '15%');
			$('.searchBox').css('margin-right', '40px');
	  		$('.searchTerm').stop().animate({
	  		width: 0
	  	},500);
	  	});
	}
	else
	{
		$('#map').fadeIn();
		
		// 검색 입력창 나타내기
		$('.menu').css('display','block');
	  	$('#userId').css('display','block');
	  	$('#userIcon').css('display','block');
	  	$('#logo').css('display','block');
	  	
		$('.searchBox').css('width', '15%');
		$('.searchBox').css('margin-right', '40px');
	  	$('.searchTerm').stop().animate({
	  		width: "90%"
	  	},500);

	  	$('.searchButton').off('mouseover');
	  	$('.searchTerm').off('focusout');

	  	//TODO 검색창 클릭 해제 시
    	$(".searchTerm").focusout(function(){
	    	$("#searchImg").slideToggle(200);
	    	$("#searchImg").attr('src','mainPage/search.png');
	    	$("#searchImg").slideToggle(200);
	    });
	}
};