package action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.Dao;
import dao.StorePageDao;

public class StoreBoardAction implements Action{

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
	{
		try
		{
			ActionForward forward = new ActionForward();

			HttpSession session = request.getSession();
			String sessionId = (String)session.getAttribute("id"); //
			String storeBoardId = request.getParameter("storeBoardId");
	   		
			StorePageDao dao = new StorePageDao();
			request.setAttribute( "storeBoards", dao.read_storeBoard_List(storeBoardId) );
			StorePageDao backDao = new StorePageDao();
			request.setAttribute( "background", backDao.get_background(storeBoardId) );
			Dao d = new Dao();
			request.setAttribute("storeName", d.get_nickname(storeBoardId));
			request.setAttribute("boardId", storeBoardId);
			
	   		forward.setRedirect(false);
	   		forward.setPath("html/storeBoard.jsp");
	   		
	   		return forward;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
}
