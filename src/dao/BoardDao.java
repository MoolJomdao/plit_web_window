package dao;

import static db.DBConnection.close;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class BoardDao {
	
	Connection con;
	PreparedStatement pstmt; // �ѹ��� �������� �м�, ������ �� ĳ�ÿ� ��� ����
	ResultSet rs;  // select���� ���� �� executeQuery() �޼��带 ����ϸ�, ���� ����� java.sql.ResultSet������ ����
	DataSource ds; // Connection pool ����
	
	public BoardDao() {
		try{
			Context init = new InitialContext();
			ds = (DataSource)init.lookup("java:/comp/env/jdbc/CUBRIDDS");  	
		}catch(Exception ex){
			System.out.println("DB ���� ����(BoardDao) : " + ex);
			return;
		}
	}
	
	public String getWriterImg( String id ) throws SQLException
	{
		con = ds.getConnection();
		String board_list_sql = "select user_photo from user_info where id = ?";		
		String img = null;
				
		try{
			con = ds.getConnection();
			pstmt = con.prepareStatement(board_list_sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			if(rs.next()){
				if( rs.getString("user_photo") == null)
				{
					img = "icon.jpg";
				}
				else
				{
					img = rs.getString("user_photo");
				}
			}
			
			return img;
		}catch(Exception ex){
			System.out.println("getBoardList ���� : " + ex);
		}finally{
			close(pstmt);
			close(rs);
			close(con);
		}
		return null;
	}	
}
