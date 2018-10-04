package action;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Read_Board_List;
import dao.Dao;

// �Խñ� ��������
public class SearchListAction implements Action{

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
	{
		try
		{
			request.setCharacterEncoding("utf-8");
			ActionForward forward= new ActionForward();
			HttpSession session = request.getSession();
			
			// login���� ����� ���̵� �޾ƿ���
			String id = (String)session.getAttribute("id");
	   		if(id==null){
	   			// TODO �Ƶ��� �޾� ���� ������ ��� �α��� ȭ������ ����.
				forward.setRedirect(true);
				forward.setPath("./SignIn.me");
				return forward;
	   		}
	   		String searchStr = request.getParameter("searchStr");
	   		String radioValue = request.getParameter("radioValue"); // context, location
	   		
		  	int page = 1;
			int limit = 10; // �ѹ��� �ҷ��� �� ����
			
			Dao dao = new Dao();
			ArrayList<Read_Board_List> arr = new ArrayList<>();
		   		
			if( radioValue.equals("context") )
				arr = dao.search_board( searchStr );
			else if( radioValue.equals("location") )
				arr = dao.search_board_from_location( searchStr );

			Dao nicknameDao = new Dao();
			String nickname = nicknameDao.get_nickname( id );
			
			request.setAttribute("rbl", arr);
			request.setAttribute("nickname", nickname);
		   	forward.setRedirect(false);
	   		forward.setPath("/html/searchPage.jsp");
	   		
	   		return forward;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

}
