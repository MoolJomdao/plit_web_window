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
import action.ReviewModifyAction;
import action.ReviewWriteAction;
import action.StoreBoardAction;
import bean.Comment;
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
			
			if( command.equals("/storeBoard.store") )
			{
				action = new StoreBoardAction();
				forward = action.execute(request, response);
			}
			else if( command.equals("/storeNameChange.store") )
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
			else if( command.equals("/modifyBoard.store") ) // 수정페이지 접속
			{
				int boardNum = Integer.parseInt( request.getParameter("boardNum") );
				Dao dao = new Dao();
				request.setAttribute("board", dao.read_board(boardNum) );
				forward = new ActionForward();
				forward.setRedirect(false);
				forward.setPath("html/modifyBoard.jsp");
			}else if( command.equals("/modifyReviewPage.store") ) // 리뷰 수정창 띄우기
			{
				int boardNum = Integer.parseInt( request.getParameter("boardNum") );
				int commentNum = Integer.parseInt( request.getParameter("commentNum") );
				String boardNickname = request.getParameter("boardNickname");
				String userNickname =  request.getParameter("userNickname");
				String boardId = request.getParameter("boardId");
				String prevPage = request.getParameter("prevPage");
				request.setAttribute("prevPage", prevPage );
				
				Dao dao = new Dao();
				request.setAttribute("comment", dao.read_comment_once(boardNum,commentNum) );

				request.setAttribute("boardNum", boardNum );
				request.setAttribute("commentNum", commentNum );
				request.setAttribute("boardNickname", boardNickname );
				request.setAttribute("userNickname", userNickname );
				request.setAttribute("boardId", boardId );
				forward = new ActionForward();
				forward.setRedirect(false);
				forward.setPath("html/modifyReview.jsp");
			}
			else if( command.equals("/modifyReview.store") ) // 리뷰 수정한거 DB로 Action
			{
				action = new ReviewModifyAction();
				forward = action.execute(request, response);
			}
			else if( command.equals("/reviewPageFromStorePage.store") ) // 리뷰페이지로 이동
			{
				String userId = request.getParameter("userId");
				String prevPage = null;
				
				Dao dao = new Dao();
				String nickname = dao.get_nickname( userId );
				
				Dao da = new Dao();
				String userNickname = da.get_nickname( id );
				
				Dao d = new Dao();
				ArrayList<Comment> commentList = d.read_comment_from_one_user(userId); // userId와 관련된 모든 리뷰 다 가져온다

				request.setAttribute("userId", userId );
				request.setAttribute("boardNickname", nickname );
				request.setAttribute("userNickname", userNickname );
				request.setAttribute("commentList", commentList);
				
				forward = new ActionForward();
				forward.setRedirect(false);
				forward.setPath("html/reviewPageFromStorePage.jsp");
			}	
			else if( command.equals("/reviewPage.store") ) // 리뷰페이지로 이동
			{
				int boardNum = Integer.parseInt( request.getParameter("boardNum") );
				String boardId = request.getParameter("boardId");
				String prevPage = null;
				prevPage = request.getParameter("prevPage");
				if( prevPage != null)
					request.setAttribute("prevPage", prevPage );
				
				Dao dao = new Dao();
				String nickname = dao.get_nickname( boardId );
				
				Dao da = new Dao();
				String userNickname = da.get_nickname( id );
				
				Dao d = new Dao();
				ArrayList<Comment> commentList = d.read_comment(boardNum);
				
				request.setAttribute("boardNickname", nickname );
				request.setAttribute("userNickname", userNickname );
				request.setAttribute("boardNum", boardNum );
				request.setAttribute("boardId", boardId );
				request.setAttribute("commentList", commentList);
				
				forward = new ActionForward();
				forward.setRedirect(false);
				forward.setPath("html/reviewPage.jsp");
			}		
			else if( command.equals("/writeReview.store") ) // 리뷰페이지 작성 엑션 실행
			{
				action = new ReviewWriteAction();
				forward = action.execute(request, response);
			}		
			else if( command.equals("/writeReviewPage.store") ) // 리뷰페이지 작성페이지 띄워주기
			{
				int boardNum = Integer.parseInt( request.getParameter("boardNum") );
				String boardId = request.getParameter("boardId");
				
				Dao dao = new Dao();
				String nickname = dao.get_nickname( boardId );
				
				Dao da = new Dao();
				String userNickname = da.get_nickname( id );

				String prevPage = request.getParameter("prevPage");
				request.setAttribute("prevPage", prevPage );
				request.setAttribute("boardNickname", nickname );
				request.setAttribute("userNickname", userNickname );
				request.setAttribute("boardId", boardId );
				request.setAttribute("boardNum", boardNum );
				
				forward = new ActionForward();
				forward.setRedirect(false);
				if( id.equals("Guest") )
					forward.setPath("html/writeReview(Guest).jsp");
				else
					forward.setPath("html/writeReview.jsp");
			}
			else if( command.equals("/deleteReview.store") ) // 리뷰 삭제
			{
				int commentNum = Integer.parseInt( request.getParameter("commentNum") );
				int boardNum = Integer.parseInt( request.getParameter("boardNum") );
				
				Dao dao = new Dao();

				response.getWriter().println( dao.delete_comment( boardNum, commentNum ) );
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
