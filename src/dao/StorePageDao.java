package dao;

import java.sql.SQLException;

import bean.Read_Mypage;

import static db.DBConnection.close;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class StorePageDao {

	Connection con;
	PreparedStatement pstmt;
	ResultSet rs;  
	DataSource ds; // Connection pool 

	//String path = "http://39.127.8.20:8080/";
	//String path2 = "/var/lib/tomcat7/webapps/ROOT";

	String path = "";
	String path2 = "./";
	
	public StorePageDao() {
		try{
			Context init = new InitialContext();
			ds = (DataSource)init.lookup("java:/comp/env/jdbc/CUBRIDDS");  	
			con = ds.getConnection();
		}catch(Exception ex){
			System.out.println("DB Init(StorePageDao) : " + ex);
			return;
		}
	}

	public Read_Mypage read_myPage(String user_name) {
	
		Read_Mypage read_board = new Read_Mypage();
	
		try {
	
			System.out.println(user_name);
			pstmt = con.prepareStatement("SELECT * FROM user_info WHERE id=?");
			// LIMIT ?,10
			pstmt.setString(1, user_name);
			rs = pstmt.executeQuery();
	
			rs.next();
	
			read_board.setUserId(rs.getString(1));
			read_board.setDateBirth(rs.getString(3));
			read_board.setDateMember(rs.getString(4));
	
			if (rs.getString(5).equals("No Photo")) {
				read_board.setUserPhoto(rs.getString(5));
			} else {
				read_board.setUserPhoto(path + "PlitImage/" + rs.getString(5));
			}
			read_board.setMassage(rs.getString(6));
	
			// =======================================================================
		} catch (SQLException e) {
			System.out.println("Read_Mypage SQL error");
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			System.out.println("Read_Mypage Exception");
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (Exception e) {
			}
		}
	
		return read_board;
	}
}

