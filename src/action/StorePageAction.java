package action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.BoardDao;

public class StorePageAction implements Action{

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
	{
		try
		{
			ActionForward forward = new ActionForward();
			
			String userid = request.getParameter("userId"); // userId who write this board
	   		
			BoardDao dao = new BoardDao();
			
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
