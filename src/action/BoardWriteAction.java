package action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import bean.Dao;
import dao.BoardDao;
import db.BoardBean;

public class BoardWriteAction implements Action{

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
	{
		try
		{	
			ActionForward forward = new ActionForward();
			HttpSession session = request.getSession();
			MultipartRequest multi = null;
			
			//이미지 처리
			request.setCharacterEncoding("UTF-8"); 
			String realFolder = ""; 
			String filename1 = ""; 
			int maxSize = 1024*1024*5; 
			String encType = "UTF-8"; 
			String savefile = "img";  
			
			try{ 
				multi=new MultipartRequest(request, realFolder, maxSize, encType, new DefaultFileRenamePolicy()); 
			
			} catch(Exception e) { 
				e.printStackTrace(); 
			} 
			 
			//전달값 저장
			String board_num = null;
			String id = (String)session.getAttribute("id");
			String content = (String) (request.getAttribute("content") == null ? null:request.getAttribute("content"));
			String tag = (String) (request.getAttribute("tag") == null ? null:request.getAttribute("tag"));
			double latitude = (double) (request.getAttribute("latitude") == null ? 0.0:request.getAttribute("latitude"));
			double longitude = (double) (request.getAttribute("longitude") == null ? 0.0:request.getAttribute("longitude"));
			int category = (int) (request.getAttribute("category") == null ? 0:request.getAttribute("category"));
			String photo = (String) (multi.getFilesystemName("photo") == null ? null:request.getAttribute("content"));
			String fullpath = realFolder + "\\" + filename1; 
			
			Dao dao = new Dao();
			
			try {
				board_num = dao.write_board(content, id, tag, latitude, longitude, category);
			}catch(Exception c){
				c.printStackTrace();
				return null;
			}
			
			if(photo != null) {
				try {
					dao.write_board_phto(board_num, photo);
					System.out.println(fullpath); //사진 전체경로 보기
				}catch(Exception c) {
					c.printStackTrace();
				}
			}
			
			
			
		   	forward.setRedirect(false);
	   		forward.setPath("mainPage/mainPage.jsp");
	   		
	   		return forward;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
}
