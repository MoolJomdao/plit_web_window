package dao;

import static db.DBConnection.*;

import java.sql.*;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import db.UserBean;

public class UserDao 
{
	Connection con;
	PreparedStatement pstmt; // �ѹ��� �������� �м�, ������ �� ĳ�ÿ� ��� ����
	ResultSet rs; // select���� ���� �� executeQuery() �޼��带 ����ϸ�, ���� ����� java.sql.ResultSet������ ����
	DataSource ds;
	
	public UserDao() {
		try{
			Context init = new InitialContext();
			ds = (DataSource)init.lookup("java:/comp/env/jdbc/CUBRIDDS");  
			con = ds.getConnection();
		}catch(Exception ex){
			System.out.println("DB ���� ����(UserDao): " + ex);
			return;
		}
	}
	
	public UserDao(Connection con) 
	{
		super();
		pstmt = null; 
		rs = null;
		this.con = con;
	}
		
	/* ����� �α��� ���� Ȯ�� */
	public UserBean getUserLogin(String id, String pw) 
	{
		// TODO Auto-generated method stub	
		UserBean user = null;
			
		try{
			String sql="select * from user_info where id=? and passwd=?"; // ����� ���� ��ȸ
			pstmt = con.prepareStatement(sql); // ������ ����
			pstmt.setString(1, id); // ù��° ?�� �� ����
			pstmt.setString(2, pw); // �ι�° ?�� �� ����
			rs = pstmt.executeQuery();
			
			if(rs.next())
			{
				// TODO ���ǿ� ��ġ�ϴ� ����� ������ User ��ü�� ����
				user = new UserBean();
				user.setId(rs.getString("id"));
				user.setPw(rs.getString("passwd"));
				user.setBirth(rs.getString(2));
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			close(pstmt);
			close(rs);
		}	
		return user;
	}
	
	/* ����� ���� ���� : ȸ�� ���� ( INSERT ) */
	public boolean joinUser(UserBean user) throws SQLException
	{
		// ( ?, ?, ? )  id, passwd, date_birth
		String sql="INSERT INTO user_info(id, passwd, date_birth, date_member) VALUES (?,?,?,now() + 1)";
		int count = 0;
		try{
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, user.getId());
			pstmt.setString(2, user.getPw());
			pstmt.setString(3, user.getBirth());
			count = pstmt.executeUpdate();
			
			if( count != 0 )
			{
				return true;
			}
		}
		catch(Exception ex)
		{
			System.out.println("joinUser ����: " + ex);			
		}finally{
			close(pstmt);
			close(rs);
			close(con);
		}	
		return false;
	}
	
	/* ��ü ����� ���� ��ȸ */
	public ArrayList getUserList() throws SQLException
	{
		String sql = "SELECT * FROM user_info";
		ArrayList<UserBean> userlist = new ArrayList();
		
		try{
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while(rs.next())
			{
				UserBean user = new UserBean();
				user.setId(rs.getString("id"));
				user.setPw(rs.getString("passwd"));
				user.setBirth(rs.getString("date_birth"));		
				userlist.add(user);
			}
			
			return userlist;
		}
		catch(Exception ex)
		{
			System.out.println("getUserList ����: " + ex);			
		}
		finally{
			close(pstmt);
			close(rs);
			close(con);
		}
		return null;
	}
	
	/* ����� ���� ��ȸ */
	public UserBean getUserInfo( String id ) throws SQLException
	{
		// �Ѱܹ��� �ش� ���̵��� ����� ���� ��ȸ
		String sql = "SELECT * FROM user_info WHERE id = ?";
		
		try{
			con = ds.getConnection();
			pstmt=con.prepareStatement(sql);
			pstmt.setString(1, id);
			rs=pstmt.executeQuery();
			rs.next();
			
			UserBean user = new UserBean();
			user.setId(rs.getString("id"));
			user.setPw(rs.getString("passwd"));
			user.setBirth(rs.getString("date_birth"));	
			user.setuserPhoto(rs.getString("user_photo"));
			return user;
		}
		catch(Exception ex)
		{
			System.out.println("getUserInfo ����: " + ex);			
		}
		finally
		{
			close(pstmt);
			close(rs);
			close(con);
		}	
		return null;
	}
	
	/* ����� ���� ���� ( ȸ�� Ż�� ) */
	public boolean deleteUserInfo(String id) throws SQLException
	{
		String sql="DELETE FROM user_info WHERE id= ? ";
		boolean isSuccess = false;
		int result1=0;
		boolean result =false;
		System.out.println("deleteId = " + id);
		try{
			con=ds.getConnection();
			con.setAutoCommit(false);
			pstmt=con.prepareStatement(sql);
			pstmt.setString(1, id);
			result1=pstmt.executeUpdate();

			if(result1 > 0)
			{
				result = true;
			}
			isSuccess=true;
		}catch(Exception ex)
		{
			System.out.println("deleteMember ����: " + ex);	
		}
		finally
		{
			try
			{
				if(isSuccess){
					con.commit();
				}
				else{
					con.rollback();
				}
			}
			catch(Exception e){};
			close(pstmt);
			close(rs);
			close(con);
		}
		
		return result;
	}
	
	public int follow(String id){
		String sql = "SELECT count(id_applicant)  FROM friends WHERE id_applicant=?";
		
		try{
			con = ds.getConnection();
			pstmt=con.prepareStatement(sql);
			pstmt.setString(1, id);
			rs=pstmt.executeQuery();
			
			rs.next();

			return rs.getInt(1);
		}
		catch(Exception ex)
		{
			System.out.println("getUserInfo ����: " + ex);			
		}
		finally
		{
			close(pstmt);
			close(rs);
			close(con);
		}	
		return -1;
	}
	
	public int follower(String id){
		String sql = "SELECT count(id_respondent)  FROM friends WHERE id_respondent=?";
		
		try{
			con = ds.getConnection();
			pstmt=con.prepareStatement(sql);
			pstmt.setString(1, id);
			rs=pstmt.executeQuery();
			
			rs.next();

			return rs.getInt(1);
		}
		catch(Exception ex)
		{
			System.out.println("getUserInfo ����: " + ex);			
		}
		finally
		{
			close(pstmt);
			close(rs);
			close(con);
		}	
		return -1;
	}
}

