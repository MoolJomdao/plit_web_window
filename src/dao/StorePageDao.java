package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import bean.Read_Mypage;
import db.GpsToAddress;

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
	
	public String change_message(String message,String id){
    	String result = id;
    
    	try {                                                                                                                                                                                 
    		pstmt = con.prepareStatement("UPDATE user_info SET massage = ? WHERE id  = ?");
    		pstmt.setString(1, message);
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
    	
    	System.out.println("change_message 성공");
    	result = message;
    	return result;
	}

	public String change_nickname(String nickname,String id){
    	String result = id;
    
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
    	
    	System.out.println("change_nickname 성공");
    	result = nickname;
    	return result;
	}
	
	public String change_myLocation(double lat, double lng ,String id){
		System.out.println("DAO Insert Strting");
		String result = "위치를 등록해주세요.";

		try {                                                                                                                                                                                 
			pstmt = con.prepareStatement("UPDATE user_info SET latitude = ? , longitude = ? WHERE id = ?");
			pstmt.setDouble(1, lat);
			pstmt.setDouble(2, lng);
			pstmt.setString(3, id);
			
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
    	System.out.println("change_myLocation 성공");
		GpsToAddress GTA = new GpsToAddress( lat, lng );
		result = GTA.getAddress().replace("대한민국 ", "");
		return result;
	}
	public String change_background( String id, String imageName ){
		System.out.println("DAO Insert Strting");
		String result = "-1";

		try {                                                                                                                                                                                 
			pstmt = con.prepareStatement("UPDATE user_info SET user_photo = ? WHERE id = ?");
			pstmt.setString(1, imageName);
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
    	System.out.println("change_background 성공");
    	result = "1";
		return result;
	}

    public Read_Mypage read_myPage(String user_name )
    {
    	Read_Mypage mypage = new Read_Mypage();
		try {

			System.out.println(user_name);
			pstmt = con.prepareStatement("SELECT id, user_photo, massage, nickname, latitude, longitude FROM user_info WHERE id=?");
			// LIMIT ?,10
			pstmt.setString(1, user_name);
			rs = pstmt.executeQuery();

			rs.next();

			mypage.userId = rs.getString(1);
			mypage.message = rs.getString(3);
			mypage.storeName = rs.getString(4);
			double lat = rs.getDouble(5);
			double lng = rs.getDouble(6);
			mypage.lat = lat;
			mypage.lng = lng;
			
			if( lat != 0.0 && lng != 0.0 )
			{
				GpsToAddress GTA = new GpsToAddress( lat, lng );
				mypage.address = GTA.getAddress().replace("대한민국 ", "");
			}
			else
				mypage.address = "위치를 입력해주세요.";

			if (rs.getString(2).equals("No Photo")) {
				mypage.userPhoto = null;
			} else {
				mypage.userPhoto = path + "PlitImage/" + rs.getString(2);
			}
			
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
    	
		return mypage;
    }
}



