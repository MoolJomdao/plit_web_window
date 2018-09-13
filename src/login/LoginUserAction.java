package login;

//import static db.DBConnection.getConnection;

import java.io.PrintWriter;
import java.sql.Connection;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import action.Action;
import action.ActionForward;
import bean.Dao;
import bean.Read_Board_List;
import db.UserBean;
import dao.UserDao;


public class LoginUserAction implements Action
{
	public ActionForward execute(HttpServletRequest request,HttpServletResponse response) 
	{

		try
		{
			request.setCharacterEncoding("UTF-8");
			HttpSession session = request.getSession();
			ActionForward forward = new ActionForward();
			
			UserDao userDao = new UserDao();
			UserBean user = new UserBean();
			
			user.setId(request.getParameter("id"));
			user.setPw(request.getParameter("pw"));
	 
			user = userDao.getUserLogin(user.getId(), user.getPw());
			
		   		
			if( user == null )
			{
				// TODO 아이디 없을시
				response.setContentType("text/html;charset=UTF-8");
				PrintWriter out=response.getWriter();
				out.println("<script>");
				out.println("alert('아이디가 없습니다, 회원가입을 해주세요!');");
				out.println("location.href='./SignIn.me';");
				out.println("</script>");
				out.close();

				return null;
			}
			
			Dao dao = new Dao();
			ArrayList<Read_Board_List> arr = new ArrayList<>();
		   		
			//로그인 성공
			session.setAttribute("id", user.getId());
			session.setAttribute("user", user);
			forward.setPath("/mainPageAction.bo");
			
			arr = dao.read_board_List();
			
			request.setAttribute("rbl", arr);
			
			return forward;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
}