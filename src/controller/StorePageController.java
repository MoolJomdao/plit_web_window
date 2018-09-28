package controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import action.Action;
import action.ActionForward;
import action.StorePageAction;
import dao.Dao;
import dao.StorePageDao;

public class StorePageController extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {
	
	
		protected void doProcess(HttpServletRequest request, HttpServletResponse response) 
				throws ServletException, IOException {

			request.setCharacterEncoding("UTF-8"); 
			response.setContentType("text/html;charset=UTF-8"); 
			
			String RequestURI = request.getRequestURI();  
			String contextPath = request.getContextPath();
			String command = RequestURI.substring(contextPath.length()); 
			ActionForward forward = null;
			Action action = null;
			HttpSession session = request.getSession();
			String id = (String)session.getAttribute("id");
			
			if( command.equals("/storeNameChange.store") )
			{
				String storeName = (String)request.getParameter("storeName");
				StorePageDao dao = new StorePageDao();
				response.getWriter().println( dao.change_nickname( storeName, id) );
				
			}	
			else if( command.equals("/storeMessageChange.store") )
			{
				String message = (String)request.getParameter("message");
				StorePageDao dao = new StorePageDao();
				response.getWriter().println( dao.change_message( message, id) );
				
			}			
			else if( command.equals("/storeLocationChange.store") )
			{
				double lat = Double.parseDouble( request.getParameter("lat") );
				double lng = Double.parseDouble( request.getParameter("lng") );
				StorePageDao dao = new StorePageDao();
				response.getWriter().println( dao.change_myLocation( lat, lng, id) );
			}
			else if( command.equals("/storeBackgroundChange.store") )
			{
				int result = -1;
				try
				{	
					StorePageDao dao = null;
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
					String board_num = null;
					
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
							
							File oldFile = new File( savePath + "\\" + fileName );
							File newFile = new File( savePath + "\\" + newName );
							oldFile.renameTo( newFile );
							
							dao = new StorePageDao();
							response.getWriter().println( dao.change_background(id, newName) );
						}
					}
					
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
			
			/*********************************************************************/
			
			if(forward != null)
			{
				if(forward.isRedirect())
				{
					response.sendRedirect(forward.getPath());
				}
				else
				{
					RequestDispatcher dispatcher = 
							request.getRequestDispatcher(forward.getPath());
					dispatcher.forward(request, response);
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
