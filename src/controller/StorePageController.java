package controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import action.Action;
import action.ActionForward;
import action.StorePageAction;
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
			else if( command.equals("/storePage.store") )
			{
				action = new StorePageAction();
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
