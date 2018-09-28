package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.oreilly.servlet.MultipartRequest;

public class DataDao {
	
	Connection con;
	PreparedStatement pstmt; // 占싼뱄옙占쏙옙 占쏙옙占쏙옙占쏙옙占쏙옙 占싻쇽옙, 占쏙옙占쏙옙占쏙옙 占쏙옙 캐占시울옙 占쏙옙占� 占쏙옙占쏙옙
	ResultSet rs;  // select占쏙옙占쏙옙 占쏙옙占쏙옙 占쏙옙 executeQuery() 占쌨쇽옙占썲를 占쏙옙占쏙옙玖占�, 占쏙옙占쏙옙 占쏙옙占쏙옙占� java.sql.ResultSet占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙
	DataSource ds; // Connection pool 占쏙옙占쏙옙
	
	public DataDao() { // 
		try{
			Context init = new InitialContext();
			ds = (DataSource)init.lookup("java:/comp/env/jdbc/CUBRIDDS");  	
		}catch(Exception ex){
			System.out.println("DB 연결 실패(DataDao) : " + ex);
			return;
		}
	}
	
	// 移쒓뎄由ъ뒪�듃 遺덈윭�삤湲�
	public JSONArray getFriendList( String id, int index , int count )
	{
		ArrayList<String> friendNames = new ArrayList<String>();
		try
		{
			con = ds.getConnection();
			pstmt = con.prepareStatement("SELECT id_respondent FROM friends WHERE id_applicant= ? ");
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			
			while( rs.next() )
			{
				friendNames.add( rs.getString( "id_respondent" ) ) ; // id�뿉 ���븳 移쒓뎄�뱾 id瑜� �떎 �뼸�뼱�삩�떎.
			}
			
			if( friendNames.size() > 0 ) // 1紐낆씠�씪�룄 移쒓뎄媛� �엳�쑝硫�
			{
				// �옄�썝 諛섑솚�빐二쇨퀬
				pstmt.close();
				pstmt = null;
				rs.close();
				rs = null;
				
				String sql = "SELECT * FROM user_info WHERE id=" + friendNames.get(0); // 泥ル쾲吏멸볼 �꽔�뼱二쇨퀬
				for( int i=1; i<friendNames.size(); i++) // 移쒓뎄 媛��닔留뚰겮 �씤�뜳�뒪 1遺��꽣 �떆�옉, �쐞�뿉 0 �꽔�뼱以ъ쑝�땲源�
				{
					sql += " OR ";
					sql += "id=" + friendNames.get(i);
				}
				pstmt = con.prepareStatement( sql );
				rs = pstmt.executeQuery();
				
				JSONArray jsonArray = new JSONArray();
				while( rs.next() )
				{
					JSONObject object = new JSONObject();
					
					object.put( "user_photo", rs.getString("user_photo") );
					object.put( "id", rs.getString("id") );
					object.put( "message", rs.getString("massage") );
					
					jsonArray.add( object );
				}
	
				return jsonArray;
			}
		}
		catch(Exception ex)
		{
			System.out.println("getFriendList �떎�뙣: " + ex);
		}
		finally
		{
			try
			{
				if(rs!=null)
					rs.close();
				if(pstmt!=null) 
					pstmt.close();
				if(con!=null) 
					con.close();
			}
			catch(SQLException ex){}
		}

		return null;
	}

	// �긽�깭硫붿꽭吏� 蹂�寃쏀븯湲�
	public int setStateMessage( String id, String message ) 
	{
		int result = 0;
		try
		{
			con = ds.getConnection();
			pstmt = con.prepareStatement("UPDATE user_info SET massage= ? WHERE id= ?");
			pstmt.setString(1, message);
			pstmt.setString(2, id);
			
			int rs = pstmt.executeUpdate();
			
			if( rs != 0 ) // �뾽�뜲�씠�듃 �꽦怨� �떆
				result = 1;

		}
		catch(Exception ex)
		{
			System.out.println("setStateMessage �떎�뙣: " + ex);
		}
		finally
		{
			try
			{
				if(rs!=null)
					rs.close();
				if(pstmt!=null) 
					pstmt.close();
				if(con!=null) 
					con.close();
			}
			catch(SQLException ex){}
		}
		
		return result;
	}
	// �긽�깭硫붿꽭吏� 蹂�寃쏀븯湲�
	public int setProfileImage( String imagePath, String id ) 
	{
		int result = 0;
		try
		{			
			con = ds.getConnection();
			pstmt = con.prepareStatement("UPDATE user_info SET user_photo= ? WHERE id= ?");
			pstmt.setString(1, imagePath);
			pstmt.setString(2, id);
			
			int rs = pstmt.executeUpdate();
			
			if( rs != 0 ) // �뾽�뜲�씠�듃 �꽦怨� �떆
				result = 1;
		}
		catch(Exception ex)
		{
			System.out.println("setProfileImage �떎�뙣: " + ex);
		}
		finally
		{
			try
			{
				if(rs!=null)
					rs.close();
				if(pstmt!=null) 
					pstmt.close();
				if(con!=null) 
					con.close();
			}
			catch(SQLException ex){}
		}
		
		return result;

	}
	
	 public int setBoard( String Content, String user_name, String tag, double latitude ,double longitude, ArrayList<String> photos )
	 {
	    	int result = 0; // 0 fail, 1 success

	    	try 
	    	{                    
	    		double board_latitude = latitude;
	    		double board_longitude = longitude;
	    		
	    		// 湲곗〈�뿉 50m�븞�뿉 �엳�뒗 肄붾찘�듃濡� �쐞�룄,寃쎈룄 �씪移섏떆耳쒖＜�뒗 �옉�뾽 
				con = ds.getConnection();
	    		pstmt = con.prepareStatement("SELECT board_latitude,board_longitude FROM board WHERE board_latitude >= ? - 0.0001 AND  board_latitude <= ? + 0.0001 AND board_longitude >= ? - 0.0001 AND board_longitude <= ? + 0.0001 LIMIT 1");
	    		pstmt.setDouble(1, latitude);
	    		pstmt.setDouble(2, latitude);
	    		pstmt.setDouble(3, longitude);
	    		pstmt.setDouble(4, longitude);
	    		
	    		rs = pstmt.executeQuery();
	    		
	    		if(rs.next())
	    		{
	    			board_latitude = rs.getDouble(1);
	    			board_longitude = rs.getDouble(2);
	    		}	
	    		 		
	    		if( rs != null )
	    			rs.close();
	    		if( pstmt != null )
	    			pstmt.close();
	    		rs = null;
	    		pstmt = null;
	    		
	    		pstmt = con.prepareStatement("INSERT INTO board( board_content, date_board, good,hits, board_tag, board_latitude, board_longitude, id, category_num, comment_cnt ) VALUES (?,SYSDATE,0,0,?,?,?,?,?,?)"); // mysql : sysdate(), cubrid : sysdatetime()
	    		pstmt.setString(1, Content);
	    		pstmt.setString(2, tag);
	    		pstmt.setDouble(3, board_latitude);
	    		pstmt.setDouble(4, board_longitude);
	    		pstmt.setString(5, user_name);
	    		pstmt.setInt(6, 0); // 바꿔야함
	    		pstmt.setInt(7, 0); // 바꿔야
	    		result = pstmt.executeUpdate();

	    		if( rs != null )
	    			rs.close();
	    		if( pstmt != null )
	    			pstmt.close();
	    		rs = null;
	    		pstmt = null;
	    		
	    		pstmt = con.prepareStatement("SELECT board_num FROM board WHERE id = ? ORDER BY board_num DESC LIMIT 1");
	    		pstmt.setString(1, user_name);
	    		rs = pstmt.executeQuery();
	    		
	    		int board_num = 0;
	    		
	    		if(rs.next())
	    		{
	    			board_num = rs.getInt("board_num");
	    		}
	    		else
	    		{
	    			result = 0; // �떎�뙣
	    			return result;
	    		}
	    		

	    		for( int i=0; i<photos.size(); i++)
	    		{
		    		pstmt = con.prepareStatement("INSERT INTO board_photo VALUES (?,?)");
		    		pstmt.setInt(1, board_num);
		    		pstmt.setString(2, photos.get(i));
		    		int success = pstmt.executeUpdate();
		    		
		    		if( pstmt != null )
		    			pstmt.close();
	    		}
	    		
	    		result = 1;
	    	}
	    	catch ( Exception e ) 
	    	{
	    		e.printStackTrace();
	    	} 
	    	finally 
	    	{
	    		try
				{
					if(rs!=null)
						rs.close();
					if(pstmt!=null) 
						pstmt.close();
					if(con!=null) 
						con.close();
				}
				catch(SQLException ex){}
	    	}
	    	
	    	return result;
	    }   
}
