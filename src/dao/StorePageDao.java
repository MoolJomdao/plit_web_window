package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import bean.Read_Board_List;
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

	public String get_background( String id ){
		String backgroundName = null;
		try {                                                                                                                                                                                 
			pstmt = con.prepareStatement("SELECT user_photo FROM user_info WHERE id = ?");
			pstmt.setString(1, id);
			
			rs = pstmt.executeQuery();
			
			if( rs.next() )
			{
				backgroundName = "PlitImage/" + rs.getString(1);
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
		return backgroundName;
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
				mypage.address = "No Location";

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
    public ArrayList<Read_Board_List> read_storeBoard_List( String id ) {
		ArrayList<Read_Board_List> arr = new ArrayList<>();
		Read_Board_List rbl = null;
		ResultSet photoResult = null;

		try {
			String str = "SELECT a.board_num, a.board_content, a.board_latitude,a.board_longitude,a.id,b.user_photo, b.nickname "
					+ "FROM board a, user_info b " 
					+ "WHERE a.id = b.id AND a.id=? " 
					+ "ORDER BY a.date_board DESC";
			pstmt = con.prepareStatement( str );
			pstmt.setString(1, id);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				rbl = new Read_Board_List();

				rbl.setBoardNum(rs.getInt(1));
				rbl.setContent(rs.getString(2));
				rbl.setBoardLatitude(rs.getDouble(3));
				rbl.setBoardLongitude(rs.getDouble(4));
				rbl.setUserId(rs.getString(5));
				rbl.storeName = rs.getString(7);

				if (rs.getString(6).equals("No Photo")) {
					rbl.setUserPhoto(rs.getString(6));
				} else {
					rbl.setUserPhoto(path + "PlitImage/" + rs.getString(6));
				}

				pstmt = con.prepareStatement("SELECT * FROM board_photo WHERE board_num = ?;");
				pstmt.setInt(1, rbl.getBoardNum());
				photoResult = pstmt.executeQuery();
				
				if( photoResult.next() )
					rbl.setBoardPhoto(path + "PlitImage/" + photoResult.getString(2));
				
				arr.add(rbl);
			}

			// =======================================================================
		} catch (SQLException e) {
			System.out.println("RBL SQL error");
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			System.out.println("RBL Exception");
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
				if (photoResult != null)
					photoResult.close();
			} 
			catch (Exception e) {
			}
		}
		return arr;
	}
}