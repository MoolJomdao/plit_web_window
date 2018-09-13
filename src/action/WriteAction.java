package action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.BoardDao;
import db.BoardBean;

public class WriteAction implements Action{

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
	{
		try
		{
			ActionForward forward = new ActionForward();
			HttpSession session = request.getSession();
			String id = (String)session.getAttribute("id");
			BoardDao dao = new BoardDao();
			BoardBean bean = new BoardBean();
			//String content, String tag, double latitude, double longitude, String id, int category_num
			//dao.boardInsert();
			
		   	forward.setRedirect(false);
	   		forward.setPath("/writeBoard/HTML/writeBoard.html");
	   		
	   		return forward;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
}
