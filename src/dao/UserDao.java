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
	PreparedStatement pstmt; // 한번의 쿼리문장 분석, 컴파일 후 캐시에 담아 재사용
	ResultSet rs; // select쿼리 실행 시 executeQuery() 메서드를 사용하며, 실행 결과로 java.sql.ResultSet형으로 리턴
	DataSource ds;
	
	public UserDao() {
		try{
			Context init = new InitialContext();
			ds = (DataSource)init.lookup("java:/comp/env/jdbc/CUBRIDDS");  
			con = ds.getConnection();
		}catch(Exception ex){
			System.out.println("DB 연결 실패(UserDao): " + ex);
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
		
	/* 사용자 로그인 정보 확인 */
	public UserBean getUserLogin(String id, String pw) 
	{
		// TODO Auto-generated method stub	
		UserBean user = null;
			
		try{
			String sql="select * from user_info where id=? and passwd=?"; // 사용자 정보 조회
			pstmt = con.prepareStatement(sql); // 쿼리문 저장
			pstmt.setString(1, id); // 첫번째 ?의 값 지정
			pstmt.setString(2, pw); // 두번째 ?의 값 지정
			rs = pstmt.executeQuery();
			
			if(rs.next())
			{
				// TODO 조건에 일치하는 사용자 정보를 User 객체에 저장
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
	
	/* 사용자 정보 저장 : 회원 가입 ( INSERT ) */
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
			System.out.println("joinUser 에러: " + ex);			
		}finally{
			close(pstmt);
			close(rs);
			close(con);
		}	
		return false;
	}
	
	/* 전체 사용자 정보 조회 */
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
			System.out.println("getUserList 에러: " + ex);			
		}
		finally{
			close(pstmt);
			close(rs);
			close(con);
		}
		return null;
	}
	
	/* 사용자 정보 조회 */
	public UserBean getUserInfo( String id ) throws SQLException
	{
		// 넘겨받은 해당 아이디의 사용자 정보 조회
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
			System.out.println("getUserInfo 에러: " + ex);			
		}
		finally
		{
			close(pstmt);
			close(rs);
			close(con);
		}	
		return null;
	}
	
	/* 사용자 정보 삭제 ( 회원 탈퇴 ) */
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
			System.out.println("deleteMember 에러: " + ex);	
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
			System.out.println("getUserInfo 에러: " + ex);			
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
			System.out.println("getUserInfo 에러: " + ex);			
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

