// list의 item html 정의
$.boardItem = function(){
	var list = $("#list");

	/*
		class : item
		class : user
		class : .user > .member_icon
		class : .text > .ellipsis
	*/
	list.append("<div class ='item'><table> <tr> <td class='user'> <div>" + 					
				"<img src='icon.jpg' class='member_icon'>" +
				"<div> user id </div> </div> </td>" +
				// "<div>" + user id + "</div>""
				"<td colspan='2' class='text'>" +
				"<div class='ellipsis'>" +
				"내용" + "</div> </td>" +
				"</tr> " + 
				"<tr><td> yyyy-mm-dd </td>" + 
				"<td colspan ='2'>" +
				"<div>" +
					"<span class='bottom'> <img src='comment.png'> </span>" + 
					"<span class='bottom'> 댓글 </span> " +
					"<span class='bottom'> <img src='heart.png'> </span>" +
					"<span class='bottom'> 좋아요 </span>" +		
					"</div> </td> </tr>" +				
				"</table> </div>");
};

