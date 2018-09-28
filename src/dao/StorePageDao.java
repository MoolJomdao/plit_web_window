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
	
	public String change_nickname(String nickname,String id){
    	String result = "-5";
    
    	try {                                                                                                                                                                                 
    		pstmt = con.prepareStatement("UPDATE user_info SET nickname = ? WHERE id  = ?");
    		pstmt.setString(1, nickname);
    		pstmt.setString(2, id);
    		
    		int i = pstmt.executeUpdate();
   //=======================================================================
    	} catch ( SQLException e ) {
    		System.out.println("Login 1");
    		e.printStackTrace();
    	} catch ( Exception e ) {
    		System.out.println("Login 2");
    		e.printStackTrace();
    	} finally {
    		try{	  
    			if ( rs != null ) rs.close();
    			if ( pstmt != null ) pstmt.close();
    			if ( con != null ) con.close();
    		}catch( Exception e ) {}
    	}
    	
    	System.out.println("StoreNameChange 성공");
    	return result;
	}
}

