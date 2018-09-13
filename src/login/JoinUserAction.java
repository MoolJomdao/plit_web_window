package login;

//import static db.DBConnection.getConnection;

import java.io.PrintWriter;
import java.sql.Connection;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import action.Action;
import action.ActionForward;
import db.UserBean;
import dao.UserDao;

/* 회원가입  */
public class JoinUserAction implements Action
{
	public ActionForward execute(HttpServletRequest request,HttpServletResponse response) 
	{
	 	
		ActionForward forward = new ActionForward();
		//Connection con = getConnection();
		boolean result = false; // 회원가입 성공 여부
		
		//UserDao userDao = new UserDao(con);
		UserDao userDao = new UserDao();
		UserBean user = new UserBean();
		
		try
		{
			request.setCharacterEncoding("euc-kr");
			
			// SignUp.jsp의 form태그의 데이터 가져와 저장
			user.setId(request.getParameter("id"));
			user.setPw(request.getParameter("pw"));
			user.setBirth(request.getParameter("birth"));   
			
			response.setCharacterEncoding("UTF-8"); 
			response.setContentType("text/html; charset=UTF-8");
			
			// 회원 정보 리스트
			ArrayList<UserBean> userList = userDao.getUserList();
			for( int i = 0; i < userList.size(); i++ )
			{
				// 아이디 중복 검사
				if( user.getId().equals( userList.get(i).getId() ) )
				{
					System.out.println("중복 아이디 발생");
					// TODO 사용자 로그인 실패 ( 경고창 )
					PrintWriter out = response.getWriter();
					out.println("<script>");
					out.println("alert('이미 존재하는 아이디입니다.');");
					out.println("history.back();");
					out.println("</script>");
					out.close();
					return null;
				}
			}
			
			/*con = getConnection();
			userDao = new UserDao(con);*/
			
			userDao = new UserDao();
			// 사용 가능한 아이디 INSERT
			result = userDao.joinUser(user);
			   		  		
			if( result == false)
			{
				System.out.println("회원가입 실패");
				// TODO 사용자 로그인 실패 ( 경고창 )
				PrintWriter out = response.getWriter();
				out.println("<script>");
				out.println("alert('회원가입 실패.');");
				out.println("history.back();");
				out.println("</script>");
				out.close();
				return null;
			}
			
			//회원가입 성공.
			System.out.println("회원가입 성공");
			System.out.println("회원가입한 아이디 : " + user.getId() );

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
