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

public class ReviewModifyAction implements Action{

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
	{
		int result = -1;
		try
		{	
			boolean isImg = false; // 이 리뷰에 이미지가 있나?
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
				isImg = true;
			}
			else
			{
				fileNames.add("No Photo");
			}

			Dao userPhotoDao = new Dao();
			//전달값 저장
			String id = (String)session.getAttribute("id");
			int boardNum = Integer.parseInt( multi.getParameter("boardNum") );
			int commentNum = Integer.parseInt( multi.getParameter("commentNum") );
			String commentNickname = multi.getParameter("commentNickname");
			String content = multi.getParameter("content");
			boolean isdelPhoto = multi.getParameter("isdelPhoto").equals("true") ? true : false;

			dao = new Dao();
			String contextPath = context.getRealPath("/");
			
			if( isImg ) // 이미지가 추가되었으면
			{
				result = dao.modify_Comment(boardNum, commentNum, content, fileNames.get(0), true, contextPath ); // 무조건 이전 이미지 삭제 해야함
			}
			else // 이미지 추가는 안했다.
			{
				result = dao.modify_Comment(boardNum, commentNum, content, fileNames.get(0), isdelPhoto, contextPath ); // 삭제 눌렀나? 
			}
			
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
