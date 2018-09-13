
function initProfileImage()
{
    var userImage = document.getElementById("user_image");
    var imageBg; // 검정 백그라운드
    var uploadIcon; // 검정 백그라운드 위에 사진로드 아이콘
    var fileLoad; // 실제 file 로 동작하는 input 태그
            
    var enter = false; // 마우스 in Image?
    var fadeoutId = 0; // 페이드아웃 진행중?
    var fadeinId = 0; // 페이드인 진행중?
    var alpha = 0.0; // 검정 백그라운드 알파 값
    const maxAlpha = 0.5;
    
    var enterLoad = false; // 마우스 in uploadIcon 
    var showingImage = false; // 프로필 사진 클릭 시 확대상태인가?
    var showingImageExit = false; // 확대사진 보고 x를 눌렀는가?
    
    // 임시 image url 변수
    var imageUrl = $("#image_url").val(); //"myPage/test.jpg";
    if( imageUrl.toUpperCase() != "NULL")
    	userImage.style.backgroundImage = "url('" + imageUrl + "')"; // 프로필사진 등
    else
    	userImage.style.backgroundImage = "url('myPage/test.jpg')"; // 프로필사진 등
            
    /************************************************
        사진위에 마우스 올리면
    *************************************************/
    userImage.addEventListener( "mouseenter", function()
    {        
        enter = true;
        showingImageExit = false;
        
        if( !userImage.firstChild ) // div없을때만
        {
            this.innerHTML = "<div id='image_background'>" + "</div>" 
	            		   + "<form id='image_form' type='hidden' enctype='multipart/form-data'>"
		                        + "<input type='file' name='profileImage' accept='image/*' style='display:none'>"  // 속성으로 multiple을 넣으면 여러개 가져올 수 있음.
		                        + "<input id='submit' type='submit' style='display:none'>"
	                       + "</form>"
                           + "<div id='up_icon'>" + "</div>"; // 사진 등록아이콘 추가
            imageBg = document.querySelector("#image_background");
            uploadIcon = document.querySelector("#up_icon");
            fileLoad = document.querySelector("input[type=file]"); // file 불러오는 input 태그
            
            imageForm = document.querySelector("#image_form");
            submit = document.querySelector("#submit");

            /** 검정배경 css 적용 **/
            imageBg.style.width = "100%";
            imageBg.style.height = this.clientHeight + "px";
            imageBg.style.backgroundColor = "rgba(0,0,0," + alpha + ")";
            imageBg.style.borderRadius = "7px";
            
            /** 사진업로드 아이콘 css 적용 **/
            uploadIcon.style.width = "30px";
            uploadIcon.style.height = "30px";
            uploadIcon.style.backgroundImage = "url('myPage/picture.png')";
            uploadIcon.style.backgroundRepeat = "no-repeat";
            uploadIcon.style.backgroundSize = "100%";
            uploadIcon.style.position = "absolute";
            uploadIcon.style.right = "5px";
            uploadIcon.style.bottom = "5px";
            uploadIcon.style.opacity = "0";
            
            // 사진업로드 아이콘 마우스 상효작용
            uploadIcon.onmouseover = function()
            {
                this.style.opacity = "1";
                enterLoad = true;
            }
            uploadIcon.onmouseout = function()
            {
                this.style.opacity = "" + maxAlpha;
                enterLoad = false;
            }
            
            // 이미지 클릭 시 input type="file" 태그가 클릭되도록 함
            uploadIcon.onclick = function()
            {
                fileLoad.click();
            }
            
            /************************************************
                사용자가 변경할 이미지파일을 선택 시 이벤트 호출
            *************************************************/
            fileLoad.onchange = function()
            {
                var fileList = this.files; // input type='file' 에서 사용자가 선택한 이미지들 가져오기
                
                var fileReader = new FileReader();
                if( !fileReader ) // 만약 file을 가져오지 못했다면
                {
                    return false;
                }
                fileReader.readAsDataURL( fileList[0] ); // 한개 선택할꺼니 0번째 url로 
                
                // 사용자 선택한 이미지 로드 시
                fileReader.onload = function ()
                {
                    userImage.style.backgroundImage = "url(" + fileReader.result + ")"; 
                    // 먼저 사용자화면에 등록, 이 파일은 form 태그로 인해 서버 서블릿을 통해 서버로 전송될 것이다.
                    imageUrl = fileReader.result;
                    
                    $("#submit").click(); // 서버로 이미지 보내기 위해 클릭이벤트 발생시키기
                }
            }

            /************************************************
            		이미지 서버로 전송하기
             *************************************************/
            $("form#image_form").submit(function(event)
            {

            	var id = $("#toolbar > input").attr("value");
            	event.preventDefault(); // 기존 리다이렉트 되는 이벤트 제거
            	 
            	// 이미지 데이터 가져오기 
            	var formData = new FormData( $(this)[0] ); 
            	formData.append("type","setProfileImage");
            	formData.append("id", id);
            	// 이미지 formData 넣기, 이미지 더 보내고 싶으면 formData.append('img', $(this)[1] ); 첫번째면 0, 두번째 이미지면 1 세번째 이미지면 2 반드시 key 값을 'img'로
            	// 글도 같이 보낼 수 있다 formData.append('key', 'value' );
            	  
            	$.ajax({
            	    url: '.data',
            	    type: 'POST',
            	    enctype: "multipart/form-data",
            	    data: formData,
            	    async: false,
            	    cache: false,
            	    contentType: false,
            	    processData: false,
            	    success: function (returndata) {
            	      alert("이미지 업로드 성공");
            	    }
            	  });
            	 
            	return false; // 기존 리다이렉트 되는 이벤트 제거
            });
        }
        /** css background-color 페이드 아웃 애니메이션 **/
        if( fadeoutId == 0 && !showingImage ) // 하나의 인터벌만 생성되도록
            fadeoutId = setInterval( fadeout, 5 );
        
        /*******************
            페이드 아웃
        ********************/
        function fadeout()
        {       
            if( !showingImageExit )
                {
            if( !enter )
            {
                clearInterval( fadeoutId );
                fadeoutId = 0;
                return;
            }
            
            if( fadeinId == 0)
            {

                if( alpha <= maxAlpha )
                {
                    imageBg.style.backgroundColor = "rgba(0, 0, 0, " + alpha + ")";
                    if( !enterLoad )
                        uploadIcon.style.opacity = "" + alpha;
                    alpha += 0.01;
                }
                else
                {
                    clearInterval( fadeoutId ); // alpha 0.7 까지 갔으면 clear
                    fadeoutId = 0;
                    return;
                }
            }
                }
        }

    } );
                
    /************************************************
        사진위에서 마우스가 나가면
    *************************************************/
    userImage.addEventListener( "mouseleave", function()
    {
        enter = false;
        
        imageBg = this.firstChild;
        /** css background-color 애니메이션 **/
        if( fadeinId == 0 && !showingImage )
            fadeinId = setInterval( fadein, 5 );
        
        /*******************
            페이드 인
        ********************/
        function fadein()
        {               
            if( !showingImageExit )
                { 
            if( !showingImage ) // 이미지 확대해서 보고 있으면 진행안함
            {
                if( enter )
                {
                    clearInterval( fadeinId );
                    fadeinId = 0;
                    return;
                }

                if( fadeoutId == 0 )
                {
                    if( alpha >= 0.0)
                    {
                        imageBg.style.backgroundColor = "rgba(0, 0, 0, " + alpha + ")";
                        uploadIcon.style.opacity = "" + alpha;
                        alpha -= 0.01;
                    }
                    else
                    {
                        // 자식노드 해제 ( imageBg, upload (input 태그), uploadIcon )
                    	userImage.innerHTML = "";

                        clearInterval( fadeinId );
                        fadeinId = 0;
                        return;
                    } 
                }
            }
            }
        }
    } );
    
    /************************************************
        사진 클릭 시 확대
    *************************************************/
    userImage.addEventListener( "click", function()
    {
        if( !enterLoad && imageUrl && !showingImageExit ) // 로드아이콘에서 누르면 동작 되지 않도록, x아이콘 눌렀으면 동작 안되도록
        {
            showingImage = true;
            
            /** 프로필 이미지 불러오면 **/
            var profileImage = new Image();
            profileImage.src = imageUrl;
            
            profileImage.onload = function()
            {
                userImage.innerHTML += "<div id='show_image_background'>" 
                                        + "<div id='show_image_box'>" 
                                            + "<div id='show_image'>" 
                                                + "<div id='show_image_xbutton'>" + "</div>" 
                                            + "</div>" 
                                        + "</div>"
                                    + "</div>" ;

                var showImageBg = document.getElementById("show_image_background");
                var showImageBox = document.getElementById("show_image_box");
                var showImage = document.getElementById("show_image");
                var showImageXButton = document.getElementById("show_image_xbutton");

                /** 검정배경 css 적용 **/
                showImageBg.style.position = "fixed";
                showImageBg.style.width = "100%";
                showImageBg.style.height = "100%";
                showImageBg.style.backgroundColor = "rgba(0,0,0," + (maxAlpha + 0.35) + ")";
                showImageBg.style.top = "0px";
                showImageBg.style.left = "0px";
                
                /** 검정배경 css 적용 **/
                showImageBox.style.position = "relative";
                showImageBox.style.width = "50%";
                showImageBox.style.height = "70%";
                var valueOfMargin = ((showImageBg.clientHeight - showImageBox.clientHeight)/2); // 부모 height, 자기자신 height 계산하여 margin 구하기
                showImageBox.style.margin = ( ( valueOfMargin > 0 ) ? valueOfMargin : valueOfMargin * -1 ) + "px auto"; // -가 나오지 않도록

                /** 확대해서 보여줄 이미지 **/
                showImage.style.position = "absolute";
                showImage.style.width = "100%"
                showImage.style.height = "100%";
                showImage.style.backgroundImage = "url(" + imageUrl + ")";
                showImage.style.backgroundSize = "contain"; // 가로를 기준으로 세로비율이 결정되도록
                showImage.style.backgroundRepeat = "no-repeat";
                showImage.style.backgroundPosition = "center center"; // 정중앙에 위치하도록
                showImage.style.margin = "0 auto";
                
                /** 종료할 x 버튼 **/
                showImageXButton.style.position = "absolute";
                showImageXButton.style.width = "25px";
                showImageXButton.style.height = "25px";
                showImageXButton.style.top = "0px";
                showImageXButton.style.right = "-30px";
                showImageXButton.style.backgroundImage = "url('myPage/xbotton.png')";
                showImageXButton.style.backgroundSize = "contain"; // 가로를 기준으로 세로비율이 결정되도록
                showImageXButton.style.backgroundRepeat = "no-repeat";
                showImageXButton.style.backgroundPosition = "center center"; // 정중앙에 위치하도록
                showImageXButton.style.opacity = "0.7";
                
                showImageXButton.addEventListener( "mouseover", function()
                {
                    this.style.opacity = "1";
                } );
                showImageXButton.addEventListener( "mouseleave", function()
                {
                    this.style.opacity = "0.7";
                } );
                showImageXButton.addEventListener( "click", function()
                {
                    // 자식노드 해제
                	userImage.innerHTML = "";
                    
                    /** 사진 확대보기 하면 fadeout, fadein interval이 동작안되기 때문에 삭제해주기 **/
                    clearInterval( fadeoutId );
                    fadeoutId = 0;
                    clearInterval( fadeinId );
                    fadeinId = 0;
                    
                    alpha = 0.0; // 알파값도 초기화
                    
                    showingImageExit = true;
                    showingImage = false;
                } );
            }
        }
    } );
    
    /************************************************
        브라우저 resize 시
    *************************************************/
    window.addEventListener( "resize", function()
    {
        resizeShowImage( document.getElementById("show_image_background"), document.getElementById("show_image_box") );
    } );
}