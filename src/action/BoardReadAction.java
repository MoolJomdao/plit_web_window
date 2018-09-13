package action;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.BoardDao;
import db.BoardBean;

public class BoardReadAction implements Action{

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
	{
		try
		{
			ActionForward forward = new ActionForward();
			HttpSession session = request.getSession();
			String id = (String)session.getAttribute("id");
			
			int num = Integer.parseInt(request.getParameter("num").replaceAll(" ", "")); // 글번호 받아오기
			System.out.println("글번호 :" + num);
	   		
			BoardDao boardDao = new BoardDao();
			BoardBean board;
			
			board = boardDao.getBoardDetail(num);
	   				
	   		request.setAttribute("board", board);
	   		request.setAttribute("id", id);
		   	forward.setRedirect(false);
	   		forward.setPath("/board/readBoard.jsp");
	   		
	   		return forward;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
}
