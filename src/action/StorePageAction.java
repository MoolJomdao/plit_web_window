package action;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.StorePageDao;

public class StorePageAction implements Action{

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
	{
		try
		{
			ActionForward forward = new ActionForward();

			HttpSession session = request.getSession();
			String sessionId = (String)session.getAttribute("id"); //
			String clickId = request.getParameter("userId");
	   		
			StorePageDao dao = new StorePageDao();
			request.setAttribute( "mypage", dao.read_myPage(clickId) );
			
	   		forward.setRedirect(false);
	   		forward.setPath("html/storePage.jsp");
	   		
	   		return forward;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
}
