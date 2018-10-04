package action;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import dao.Dao;

public class BoardModifyAction implements Action{

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
	{
		int result = -1;
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
			int maxSize = 1024*1024*50; 
			String encType = "UTF-8"; 
			String savefile = "img";
			
			File saveDir = new File(savePath); // �뵒�젆�넗由ш� �뾾�쓣 寃쎌슦 �깮�꽦
			if(!saveDir.exists()) 
			{
				saveDir.mkdirs(); 
			}
			
			try{ 
				multi=new MultipartRequest(request, savePath, maxSize, encType, new DefaultFileRenamePolicy()); 
			
			} catch(Exception e) { 
				e.printStackTrace(); 
			} 
			 
			//전달값 저장
			int board_num = Integer.parseInt( multi.getParameter("boardNum") );
			String id = (String)session.getAttribute("id");
			String content = (String) (multi.getParameter("content") == null ? null:multi.getParameter("content"));
			String tag = (String) (multi.getParameter("tag") == null ? null:multi.getParameter("tag"));
			double latitude = (double) Double.parseDouble( multi.getParameter("lat") );
			double longitude = (double) Double.parseDouble( multi.getParameter("lng") );
			int category = (int) (multi.getParameter("category") == null ? 0:multi.getParameter("category"));
			boolean delAll = multi.getParameter("delAll").equals("true")? true: false;
			
			Enumeration Names = multi.getFileNames(); 
			ArrayList<String> fileNames = new ArrayList<>();
			if( Names != null )
			{
				while( Names.hasMoreElements() ) 
				{
					String name, ext, newName="";
				    String now = new SimpleDateFormat("yyyyMMddHmsS").format(new Date());  //현재시간

					String strName = Names.nextElement().toString();
					String fileName = multi.getFilesystemName(strName);
					
			        int index = fileName.lastIndexOf("."); // 확장자 앞 . 위치
			        if( index != -1) {
			        	name = (Math.random() * 1000) + now;
			        	ext = fileName.substring(index);
			        	newName = name  + ext;
			        }
					
					File oldFile = new File( savePath + "/" + fileName );
					File newFile = new File( savePath + "/" + newName );
					oldFile.renameTo( newFile );
					
					fileNames.add( newName ); 
				}
			}
			else
			{
				fileNames.add("");
			}

			String dirPath = context.getRealPath("\\"); 
			dao = new Dao();
			
			result = dao.change_board(board_num, content, tag, longitude, latitude, category, dirPath, delAll);

			
			if(fileNames.size() != 0) {
				dao = new Dao();
				result = dao.write_board_phto(board_num+"", fileNames);
			}

			response.getWriter().println( result ); // 1 = true, -1 = false
			
	   		return null;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
}
