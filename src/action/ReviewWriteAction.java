package action;

import java.io.File;
import java.io.IOException;
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

public class ReviewWriteAction implements Action{

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
			 
			Enumeration Names = multi.getFileNames(); 
			ArrayList<String> fileNames = new ArrayList<>();
			if( Names.hasMoreElements() )
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
				
				File oldFile = new File( savePath + "\\" + fileName );
				File newFile = new File( savePath + "\\" + newName );
				oldFile.renameTo( newFile );
				
				fileNames.add( newName ); 
			}
			else
			{
				fileNames.add("No Photo");
			}

			Dao userPhotoDao = new Dao();
			//전달값 저장
			String id = (String)session.getAttribute("id");
			String pw = null;
			pw = (String)multi.getParameter("pw");
			int boardNum = Integer.parseInt( multi.getParameter("boardNum") );
			String commentNickname = multi.getParameter("commentNickname");
			String content = multi.getParameter("content");
			String userPhoto = userPhotoDao.get_user_photo(id);
			
			dao = new Dao();
			
			// photo가 0이면 userphoto, 1~5면 Guest 포토
			if( id.equals("Guest") ) // 게스트일 경우
				result = dao.write_Comment(boardNum, id, content, pw, commentNickname, (int)(Math.random()*5)+1, fileNames.get(0));
			else
				result = dao.write_Comment(boardNum, id, content, "0", commentNickname, 0, fileNames.get(0));
			
			response.getWriter().println( result ); // 1 = true, -1 = false
			// BoardWriteAction은 Ajax로 통신했기 때문에 forward가 필요 없다.
			
	   		return null;
		}
		catch(Exception e)
		{
			try {
				response.getWriter().println( -1 );
			} catch (IOException e1) {
				e1.printStackTrace();
			} // 1 = true, -1 = false
			e.printStackTrace();
			return null;
		}
	}
}
