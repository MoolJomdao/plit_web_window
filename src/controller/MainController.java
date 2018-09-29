package controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import action.Action;
import action.ActionForward;
import action.BoardListAction;
import action.BoardWriteAction;
import action.JoinUserAction;
import action.LoginUserAction;
import action.SearchListAction;
import action.StorePageAction;

public class MainController extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {
	
	
		protected void doProcess(HttpServletRequest request, HttpServletResponse response) 
				throws ServletException, IOException {

			request.setCharacterEncoding("UTF-8"); 
			response.setContentType("text/html;charset=UTF-8"); 
			
			String RequestURI = request.getRequestURI();  
			String contextPath = request.getContextPath();
			String command = RequestURI.substring(contextPath.length()); 
			ActionForward forward = null;
			Action action = null;
			
			if( command.equals("/mainPageAction.bo") )
			{
				action = new BoardListAction();
				forward = action.execute(request, response);
			}			
			else if( command.equals("/searchPage.bo") )
			{
				action = new SearchListAction();
				forward = action.execute(request, response);
			}	
			else if( command.equals("/storePage.bo") )
			{
				action = new StorePageAction();
				forward = action.execute(request, response);
			}
			else if( command.equals("/writeBoard.bo") )
			{
				action = new BoardWriteAction();
				forward = action.execute(request, response);
			}
			else if( command.equals("/SignIn.me") )
			{
				forward = new ActionForward();
				forward.setRedirect(false);
				forward.setPath("/SignIn/SignIn.jsp");
			}
			else if( command.equals("/LoginUserAction.me") )
			{
				action = new LoginUserAction();
				forward = action.execute(request, response);
			}
			else if( command.equals("/SignUp.me") )
			{
				forward = new ActionForward();
				forward.setRedirect(false);
				forward.setPath("/SignUp/SignUp.jsp");
			}
			else if( command.equals("/JoinUserAction.me") )
			{
				action = new JoinUserAction();
				forward = action.execute(request, response);
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
					// RequestDispatcher
					// 占쏙옙占쏙옙  request占쏙옙 占쏙옙占� 占쌍댐옙 占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙占싹곤옙 占쌍다곤옙 占쏙옙 占쏙옙占쏙옙占쏙옙占쏙옙占쏙옙, 占쏙옙占쏙옙占쏙옙占쏙옙占쏙옙占쏙옙占쏙옙占쏙옙
					// 占쌔댐옙 占쏙옙占쏙옙占쏙옙 占쏙옙 占쏙옙 占쌍곤옙 占쏙옙占쏙옙占싹댐옙 占쏙옙占�
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
