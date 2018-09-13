package controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import dao.DataDao;

public class DataController extends javax.servlet.http.HttpServlet 
implements javax.servlet.Servlet {
	
	public static final String m_serverIP = "http://192.168.0.11:8080";
	
// doGet, doPost �몮�떎 �떎�뻾�빐�룄 doProcess �떎�뻾�맖
protected void doProcess( HttpServletRequest request, HttpServletResponse response ) 
		throws ServletException, IOException {
	
	request.setCharacterEncoding("UTF-8"); // 諛쏅뒗嫄� UTF-8 �씤肄붾뵫
	response.setCharacterEncoding("UTF-8");; // 蹂대궪 �븣�룄 UTF-8濡� �씤肄붾뵫
	
	String RequestURI = request.getRequestURI();  // /plit/.data
	String contextPath = request.getContextPath();// /plit ( �쁽�옱 �넱罹� �뤃�뜑 )
	
	String dataType = request.getParameter("type");
	DataDao dataDao = new DataDao(); // �뵒鍮꾩뿉 媛믪쓣 �꽔嫄곕굹 鍮쇨린�쐞�빐�꽌 Dao �깮�꽦
	
	if( dataType != null )
	{
		if( dataType.equals("getFriendList") ) // 移쒓뎄紐⑸줉 遺덈윭�삤湲�
		{
			String id = request.getParameter("id");
			JSONArray json = dataDao.getFriendList( id, 0, 10 );
			
			if( json != null )
				response.getWriter().println( json );
		}
		else if( dataType.equals("setStateMessage") ) // 移쒓뎄紐⑸줉 遺덈윭�삤湲�
		{
			String id = request.getParameter("id");
			String message = request.getParameter("message");
			int result = dataDao.setStateMessage( id, message ); // 0�씠硫� false, 1�씠硫� true
			
			response.getWriter().println( result ); // "true", "false" �몮以� �븯�굹瑜� 蹂대깂
		}
	}
	else // null �씠�씪�뒗嫄� MultiRequest ���엯�쑝濡� 蹂대깉�떎�뒗嫄곗엫 洹몃쭚�� 利됱뒯 image 愿��젴 泥섎━�떎.
	{
		
		ServletContext context = request.getServletContext(); // �쁽�옱 �꽌釉붾┸ 媛앹껜
		String savePath = context.getRealPath("PlitImage"); // �쁽�옱 �넱罹� �뤃�뜑�뿉 images 寃쎈줈瑜� 諛쏆븘�샂
		int size = 50 * 1024 * 1024; // �떎�슫 諛쏆쓣 理쒕� �슜�웾 50Mb 源뚯� 媛��뒫
		
		File saveDir = new File(savePath); // �뵒�젆�넗由ш� �뾾�쓣 寃쎌슦 �깮�꽦
		if(!saveDir.exists()) 
		{
			saveDir.mkdirs(); 
		}
		
	    // MultipartRequest(request, ���옣寃쎈줈[, 理쒕��뿀�슜�겕湲�, �씤肄붾뵫耳�由��꽣�뀑, �룞�씪�븳 �뙆�씪紐� 蹂댄샇 �뿬遺�])
		MultipartRequest multiRequest = new MultipartRequest( request, savePath, size, "utf-8", new DefaultFileRenamePolicy() );
		dataType = multiRequest.getParameter("type"); // multiRequest�븞�뿉 "type" 媛믪쓣 媛��졇�삩�떎.

		if( dataType.equals("setProfileImage") ) // �봽濡쒗븘�씠誘몄� 蹂�寃쏀븯湲�
		{
			String id = multiRequest.getParameter("id"); // "id" 媛� 媛��졇�삤湲�
			
			String fileName = multiRequest.getFile("profileImage").getName(); // �뼸�뼱�삩 �뙆�씪 紐�
			String extName = fileName.substring(fileName.length()-4, fileName.length()); // �솗�옣�옄紐�

			File isFile = new File( savePath + "\\" + id + "_profileImage" + extName ); // �씠誘� �궗�슜�옄�쓽 �씠誘몄�媛� 議댁옱�븳�떎硫� �궘�젣�빐�빞�븳�떎
			if( isFile.exists() )
			{
				isFile.delete();
			}
			
			// �궘�젣�븯嫄곕굹 �븞�븯嫄곕굹 留뚮뱾�뼱吏� �뙆�씪�쓣 �궗�슜�옄id_profileImage �씪�뒗 �씠由꾩쑝濡� 諛붽씔�떎.
			String srcFilePath = savePath + "\\" + fileName; 
			File srcFile = new File( srcFilePath );
			String renameFilePath = savePath + "\\" + id + "_profileImage" + extName;
			File renameFile = new File( renameFilePath );

			srcFile.renameTo( renameFile ); // �씠由� 諛붽씀湲�
			
			String profileImageName = id + "_profileImage" + extName;
			
			/** DB�뿉 �꽔湲� **/
			int result = dataDao.setProfileImage( profileImageName, id ); // 0�씠硫� false, 1�씠硫� true
			
			response.getWriter().println( result ); // "true", "false" �몮以� �븯�굹瑜� 蹂대깂
		}
		else if( dataType.equals("setBoard") ) // 寃뚯떆湲� �옉�꽦
		{
			String id = multiRequest.getParameter("id"); // "id" 媛� 媛��졇�삤湲�
			String locationText = multiRequest.getParameter("locationText"); // "id" 媛� 媛��졇�삤湲�
			String textArea = multiRequest.getParameter("textArea"); // "id" 媛� 媛��졇�삤湲�
			
			Enumeration Names = multiRequest.getFileNames(); // �뼸�뼱�삩 �뙆�씪 紐�
			ArrayList<String> fileNames = new ArrayList<>();
			if( Names != null )
			{
				while( Names.hasMoreElements() ) // �벑濡앺븳 �궗吏꾨쭔�겮 �씠由� 媛��졇�삤湲�
				{
					String strName = Names.nextElement().toString();
					String fileName = multiRequest.getFilesystemName(strName);
					fileNames.add( fileName ); // �뙆�씪紐� + �솗�옣�옄紐� 異붽�
				}
			}
			else
			{
				fileNames.add("");
			}
			/** DB�뿉 �꽔湲� **/
			int result = dataDao.setBoard( textArea, id, "", 0, 0, fileNames ); // 0�씠硫� false, 1�씠硫� true
			
			response.getWriter().println( result ); // "true", "false" �몮以� �븯�굹瑜� 蹂대깂
		}
	}
}

protected void doGet(HttpServletRequest request, HttpServletResponse response) 
	throws ServletException, IOException {
	doProcess(request,response);
}  	

protected void doPost(HttpServletRequest request, HttpServletResponse response) 
	throws ServletException, IOException {
	doProcess(request,response);
}	  	 
}
