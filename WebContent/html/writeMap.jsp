<!DOCTYPE html>
<html>
<head>
	<title></title>
	<script async defer src="https://maps.googleapis.com/maps/api/js?key=AIzaSyB1O3_xjyaGDGbrQ38g-i3kjUpCgjuWEWw&callback=initMap"
  		type="text/javascript"></script>
	<script type="text/javascript" src="http://code.jquery.com/jquery-latest.min.js"></script>
	<!-- GoogoleMap Asynchronously Loading the API ********************************************* -->
	<style type="text/css">
		body{
			margin: 0px;
		}
	</style>  
	
  
</head>
<body>
	
	<input id="lat" type="hidden" value="1">
    <input id="lng" type="hidden" value="1">
	
	<div id="map-canvas" style="width: 100%; height: 400px"></div>

</body>
</html>

  	<script>
		
		$(document).ready(function(){
			$("#map-canvas").css("height", $(document).height());
			
		    google.maps.event.addDomListener(window, 'load', initialize);
		});
		
      function initialize() 
      {
    	  alert( document.getElementById("lat").value + ", " + document.getElementById("lon").value );
        var mapLocation = new google.maps.LatLng('36.322473', '127.412501'); // 지도에서 가운데로 위치할 위도와 경도
        var markLocation = new google.maps.LatLng('36.322473', '127.412501'); // 마커가 위치할 위도와 경도
         
        var mapOptions = {
          center: mapLocation, // 지도에서 가운데로 위치할 위도와 경도(변수)
          zoom: 18, // 지도 zoom단계
          mapTypeId: google.maps.MapTypeId.ROADMAP // 맵종류 ( ROADMAP = 2D )
        };

        var map = new google.maps.Map(document.getElementById("map-canvas"), // id: map-canvas, body에 있는 div태그의 id와 같아야 함
            mapOptions);
         
        var size_x = 60; // 마커로 사용할 이미지의 가로 크기
        var size_y = 60; // 마커로 사용할 이미지의 세로 크기
         
        // 마커로 사용할 이미지 주소
        var image = new google.maps.MarkerImage( 'http://www.larva.re.kr/home/img/boximage3.png',
                            new google.maps.Size(size_x, size_y),
                            '',
                            '',
                            new google.maps.Size(size_x, size_y));
         
        var marker;
        marker = new google.maps.Marker({
               position: markLocation, // 마커가 위치할 위도와 경도(변수)
               map: map,
               icon: image, // 마커로 사용할 이미지(변수)
//             info: '말풍선 안에 들어갈 내용',
//             title: ' 마커에 마우스 포인트를 갖다댔을 때 뜨는 타이틀' 
        });
         
        var content = "말풍선 안에 들어갈 내용";
         
        // 마커를 클릭했을 때의 이벤트. 말풍선
        var infowindow = new google.maps.InfoWindow({ content: content});
 
        google.maps.event.addListener(marker, "click", function() {
            infowindow.open(map,marker);
        });
         
        var geocoder = new google.maps.Geocoder();
        
        google.maps.event.addListener( map, 'click', function( event ){
        	var location = event.latLng;
        	geocoder.geocode( { 'latLng' : location },
        		function( results, status ){
        			if( status == google.maps.GeocoderStatus.OK ){
        				var address = results[0].formatted_address;
        				var lat = results[0].geometry.location.lat();
        				var lng = results[0].geometry.location.lng();
        				
        				alert("address : " + address + "\n, lat : " + lat + "\n, lng : " + lng );
        			}
        			else{
        				alert("Geocoder failed due to : " + status );
        			}
        	});
        	
        	if( !marker ){
        		marker = new google.maps.Marker({ position : location, map : map }); // 마커 없으면 생성
        	}
        	else{
        		marker.setMap(null); // 마커 없애주고
        		marker = new google.maps.Marker({ position : location, map : map }); // 마커 없으면 생성
        	}
        	
        	map.set( location );

        });
      }
      
      
	</script>
