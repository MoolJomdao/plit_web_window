package action;

import java.util.ArrayList;
import java.util.Enumeration;

import javax.servlet.ServletContext;
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
			Dao dao = null;
			ActionForward forward = new ActionForward();
			HttpSession session = request.getSession();
			MultipartRequest multi = null;
			
			//이미지 처리
			request.setCharacterEncoding("UTF-8"); 
			ServletContext context = request.getServletContext(); // �쁽�옱 �꽌釉붾┸ 媛앹껜
			String savePath = context.getRealPath("PlitImage"); 
			int maxSize = 1024*1024*5; 
			String encType = "UTF-8"; 
			String savefile = "img";  
			
			try{ 
				multi=new MultipartRequest(request, savePath, maxSize, encType, new DefaultFileRenamePolicy()); 
			
			} catch(Exception e) { 
				e.printStackTrace(); 
			} 
			 
			//전달값 저장
			String board_num = null;
			String id = (String)session.getAttribute("id");
			String content = (String) (multi.getParameter("content") == null ? null:multi.getParameter("content"));
			String tag = (String) (multi.getParameter("tag") == null ? null:multi.getParameter("tag"));
			double latitude = (double) (multi.getParameter("latitude") == null ? 0.0:multi.getParameter("latitude"));
			double longitude = (double) (multi.getParameter("longitude") == null ? 0.0:multi.getParameter("longitude"));
			int category = (int) (multi.getParameter("category") == null ? 0:multi.getParameter("category"));
			
			Enumeration Names = multi.getFileNames(); 
			ArrayList<String> fileNames = new ArrayList<>();
			if( Names != null )
			{
				while( Names.hasMoreElements() ) 
				{
					String strName = Names.nextElement().toString();
					String fileName = multi.getFilesystemName(strName);
					fileNames.add( fileName ); 
				}
			}
			else
			{
				fileNames.add("");
			}
			
			dao = new Dao();
			
			try {
				board_num = dao.write_board(content, id, tag, latitude, longitude, category);
			}catch(Exception c){
				c.printStackTrace();
				return null;
			}
			
			
			
			if(fileNames.size() != 0) {
				dao = new Dao();
				int result = dao.write_board_phto(board_num, fileNames);
				System.out.println(result);
				
			}
			
			
			
		   	forward.setRedirect(false);
	   		forward.setPath("/mainPageAction.bo");
	   		
	   		return forward;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
}
