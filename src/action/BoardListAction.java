package action;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Read_Board_List;
import dao.Dao;

// �Խñ� ��������
public class BoardListAction implements Action{

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
	{
		try
		{
			ActionForward forward= new ActionForward();
			HttpSession session = request.getSession();
			
			// login���� ����� ���̵� �޾ƿ���
			String id = (String)session.getAttribute("id");
			if( id == null )
				session.setAttribute("id", "Guest");
			
	   		if(id==null){
	   			// TODO �Ƶ��� �޾� ���� ������ ��� �α��� ȭ������ ����.
				forward.setRedirect(true);
				forward.setPath("./SignIn.me");
				System.out.println("�α��� ����. - BoardListAction -");
				return forward;
	   		}
	   		
			Dao boardDao = new Dao();
			ArrayList<Read_Board_List> boardlist =new ArrayList();
			ArrayList<String> imglist = new ArrayList<>();
			
		  	int page = 1;
			int limit = 10; // �ѹ��� �ҷ��� �� ����
			
			boardlist = boardDao.read_board_List(); //����Ʈ�� �޾� ��
			
	   		request.setAttribute("page", page); // ���� ������
			request.setAttribute("boardlist", boardlist); // return ArrayList;
			request.setAttribute("imglist", imglist);
			
			Dao dao = new Dao();
			ArrayList<Read_Board_List> arr = new ArrayList<>();
		   		
			arr = dao.read_board_List();
			
			Dao nicknameDao = new Dao();
			String nickname = nicknameDao.get_nickname( id );
			
			request.setAttribute("rbl", arr);
			request.setAttribute("nickname", nickname);
		   	forward.setRedirect(false);
	   		forward.setPath("/html/mainPage.jsp");
	   		
	   		return forward;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

}
