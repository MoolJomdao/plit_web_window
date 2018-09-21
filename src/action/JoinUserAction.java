package action;

//import static db.DBConnection.getConnection;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.User_Info;
import dao.Dao;

/* ȸ������  */
public class JoinUserAction implements Action
{
	public ActionForward execute(HttpServletRequest request,HttpServletResponse response) 
	{
	 	
		ActionForward forward = new ActionForward();
		//Connection con = getConnection();
		int result = 0; // ȸ������ ���� ����
		
		//UserDao userDao = new UserDao(con);
		Dao dao = null;
		User_Info user = new User_Info();
		
		try
		{
			request.setCharacterEncoding("euc-kr");
			
			// SignUp.jsp�� form�±��� ������ ������ ����
			user.setId(request.getParameter("id"));
			user.setPassword(request.getParameter("pw"));
			user.setDateBirth(request.getParameter("birth"));
			user.setUserPhoto("No Photo");
			user.setMassage("No Massage");
			
			response.setCharacterEncoding("UTF-8"); 
			response.setContentType("text/html; charset=UTF-8");
			
			// ID 중복체크
			dao = new Dao();
			int userList = dao.check_Id(user.getId());
			
			dao = new Dao();
			// ��� ������ ���̵� INSERT
			result = dao.membership(user);
			   		  		
			if( result != 1)
			{
				System.out.println("회원가입 실패");
				// TODO ����� �α��� ���� ( ���â )
				PrintWriter out = response.getWriter();
				out.println("<script>");
				out.println("alert('회원가입 실패.');");
				out.println("history.back();");
				out.println("</script>");
				out.close();
				return null;
			}
			
			//ȸ������ ����.
			System.out.println("회원가입 성공");
			System.out.println("회원가입 성공 : " + user.getId());

			request.setAttribute("join", "success");
			forward.setRedirect(false);
			forward.setPath("SignIn/SignIn.jsp");
			
			return forward;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
}
