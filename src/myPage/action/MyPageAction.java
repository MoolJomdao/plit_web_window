package myPage.action;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import action.Action;
import action.ActionForward;

/*
 *  留덉씠�럹�씠吏� 泥섏쓬 �쓣�슱�븣 �븘�슂�븳 �봽濡쒗븘�궗吏�, �긽�깭硫붿꽭吏� �벑�쓣 �젙�쟻 濡쒕뵫
 */

public class MyPageAction implements Action
{
	Connection con;
	PreparedStatement pstmt;
	ResultSet rs;
	DataSource ds; // Connection pool
	
	public MyPageAction()
	{
		Context init;
		try
		{
			init = new InitialContext();
			ds = (DataSource) init.lookup("java:comp/env/jdbc/MysqlDB");	
		} 
		catch (NamingException e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public ActionForward execute( HttpServletRequest request, HttpServletResponse response )
	{
		ActionForward forward = new ActionForward();
		
		forward.setRedirect( false );
		forward.setPath("/myPage/myPage.jsp");
		
		return forward;
	}
	
	public void getMyPage( HttpServletRequest request, String id )
	{

		try 
		{
			con = ds.getConnection();
			pstmt = con.prepareStatement("SELECT user_photo, massage FROM user_info WHERE id = ? ");
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			
			while( rs.next() )
			{
				request.setAttribute("user_photo", rs.getString("user_photo"));
				request.setAttribute("message", rs.getString("massage"));
			}
			
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
	}

}
