package action;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Dao;
import bean.Read_Board_List;
import dao.BoardDao;
import db.BoardBean;

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
	   		if(id==null){
	   			// TODO �Ƶ��� �޾� ���� ������ ��� �α��� ȭ������ ����.
				forward.setRedirect(true);
				forward.setPath("./SignIn.me");
				System.out.println("�α��� ����. - BoardListAction -");
				return forward;
	   		}
	   		
			BoardDao boardDao = new BoardDao();
			ArrayList<BoardBean> boardlist  =new ArrayList();
			ArrayList<String> imglist = new ArrayList<>();
			
		  	int page = 1;
			int limit = 10; // �ѹ��� �ҷ��� �� ����
			
			boardlist = boardDao.getBoardList(page, limit); //����Ʈ�� �޾� ��
	   				
			for( int i = 0; i < boardlist.size(); i++ )
			{
				String writer = boardlist.get(i).getId();
				if( writer == null )
				{
					imglist.add("icon.jpg");
				}
				else
				{
					imglist.add(boardDao.getWriterImg(writer));
				}
				
			}
			
	   		request.setAttribute("page", page); // ���� ������
			request.setAttribute("boardlist", boardlist); // return ArrayList;
			request.setAttribute("imglist", imglist);
			
			Dao dao = new Dao();
			ArrayList<Read_Board_List> arr = new ArrayList<>();
		   		
			arr = dao.read_board_List();
			
			request.setAttribute("rbl", arr);
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
