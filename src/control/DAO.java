package control;


import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DAO 
{
    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    
    String path = "http://104.198.86.112:8080/";
    String path2 = "/var/lib/tomcat7/webapps/ROOT";
    
    public DAO()  
    {
    	 try {
    		 System.out.println("connecting DB");
    	        Context initContext = new InitialContext();               
    	        DataSource ds = (DataSource) initContext.lookup("java:comp/env/jdbc/CUBRIDDS");     
    	        conn = ds.getConnection();                   
    	        System.out.println("connect complete");

    	      } catch ( SQLException e ) {
    	    	  System.out.println("DAO 1");
    	              e.printStackTrace();
    	      } catch ( Exception e ) {
    	    	  System.out.println("DAO 2");
    	              e.printStackTrace();
    	      } 
    	         
    }
    
    
    //留덉씠�럹�씠吏� read Write
    //湲� 紐⑸줉 read--�쑀���븘�씠�뵒濡� (my�럹�씠吏��뿉�꽌 �궗�슜)
    
    
    
    public String read_myPage_comment(String id)
    {
    	JSONObject result = new JSONObject();
    	int count = 0;
    	
    	
    	try {                                                                                                                                                                                 

    		pstmt = conn.prepareStatement("SELECT a.board_num, a.comment_num, a.comment_date, a.comment_content, a.comment_id, b.user_photo , a.comment_photo, a.comment_pw, a.comment_nickname, a.user_photo FROM comment a , user_info b WHERE a.comment_id = b.id AND a.board_num IN (SELECT board_num FROM board WHERE id = ?)");
    		pstmt.setString(1,id);
    		rs = pstmt.executeQuery();
    		
    		JSONArray jrr = new JSONArray();
    	
    		while(rs.next())
    		{
    			JSONObject jtmp = new JSONObject();
    			
    			jtmp.put("board_num",rs.getInt(1));
    			jtmp.put("comment_num",rs.getInt(2));
    			jtmp.put("comment_date",rs.getString(3));
    			jtmp.put("comment_content",rs.getString(4));
    			jtmp.put("comment_id",rs.getString(5));
    			if(rs.getString(6).equals("No Photo"))
    			{
    				jtmp.put("user_photo",rs.getString(6));
    			}
    			else
    			{
    				jtmp.put("user_photo",path+"PlitImage/"+rs.getString(6));
    			}
    			if(rs.getString(7).equals("No Photo"))
    			{
    				jtmp.put("comment_photo",rs.getString(7));
    			}
    			else
    			{
    				jtmp.put("comment_photo",path+"PlitImage/"+rs.getString(7));
    			}
    			jtmp.put("comment_pw",rs.getString(8));
    			jtmp.put("comment_nickname",rs.getString(9));
    			jtmp.put("guest_photo",rs.getInt(10));
    			jrr.put(jtmp);
    			count++;
    			
    		}
    		
    		result.put("read_comment_data", jrr);

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
    		}catch( Exception e ) {}	
    		try{
    			if ( pstmt != null ) pstmt.close();
    		}catch( Exception e ) {}
    		try{
    			if ( conn != null ) conn.close();
    		}catch( Exception e ) {}
    	}
    	
    	
    	try {
			result.put("com_count", count);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return result.toString();
    }

    
    
    public String write_comment_phto(String comment_num,String photo_name)
    {
    
    	System.out.println("DAO PHOTO Strting");
    	String result = "-5";
    
    	try {                                                                                                                                                                                 

    		pstmt = conn.prepareStatement("UPDATE comment SET comment_photo = ? WHERE comment_num = ?");
    		pstmt.setString(1, photo_name);
    		pstmt.setInt(2, Integer.parseInt(comment_num));
    		int i = pstmt.executeUpdate();
    		
    		result = i+"";
    				
    	

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
    		}catch( Exception e ) {}	
    		try{
    			if ( pstmt != null ) pstmt.close();
    		}catch( Exception e ) {}
    		try{
    			if ( conn != null ) conn.close();
    		}catch( Exception e ) {}
    	}
 //=========================================================================   	
    	
    	System.out.println("DAO PHOTO COMPLETE");
    	return result;
    }
    
    
 
    public String read_myLocation(String id)
    {
    	JSONObject result = new JSONObject();
    	
    	
    	try {                                                                                                                                                                                 


    		pstmt = conn.prepareStatement("SELECT latitude, longitude FROM user_info WHERE id = ?");
    		// LIMIT ?,10
    		pstmt.setString(1, id);
    		rs = pstmt.executeQuery();
    		
    		if(rs.next());
    		{
    	
    		result.put("latitude",rs.getDouble(1));
    		result.put("longitude",rs.getDouble(2));

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
    		}catch( Exception e ) {}	
    		try{
    			if ( pstmt != null ) pstmt.close();
    		}catch( Exception e ) {}
    		try{
    			if ( conn != null ) conn.close();
    		}catch( Exception e ) {}
    	}
    	
    	return result.toString();
    }

    
    public String change_myLocation(double latitude, double longitude ,String id){
    	System.out.println("DAO Insert Strting");
    	String result = "-5";
    
    	try {                                                                                                                                                                                 
    		pstmt = conn.prepareStatement("UPDATE user_info SET latitude = ? , longitude = ? WHERE id = ?");
    		pstmt.setDouble(1, latitude);
    		pstmt.setDouble(2, longitude);
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
    		}catch( Exception e ) {}	
    		try{
    			if ( pstmt != null ) pstmt.close();
    		}catch( Exception e ) {}
    		try{
    			if ( conn != null ) conn.close();
    		}catch( Exception e ) {}
    	}
    	return result;
}

    
    
    
    public String change_nickname(String nickname,String id){
    	    	System.out.println("DAO Insert Strting");
    	    	String result = "-5";
    	    
    	    	try {                                                                                                                                                                                 
    	    		pstmt = conn.prepareStatement("UPDATE user_info SET nickname = ? WHERE id  = ?");
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
    	    		}catch( Exception e ) {}	
    	    		try{
    	    			if ( pstmt != null ) pstmt.close();
    	    		}catch( Exception e ) {}
    	    		try{
    	    			if ( conn != null ) conn.close();
    	    		}catch( Exception e ) {}
    	    	}
    	return result;
    }
    
    
    
    public String delete_Comment(int board_num,int comment_num)
    {
    	    	System.out.println("DAO Insert Strting");
    	    	String result = "-5";
    	    
    	    	try {                                                                                                                                                                                 

    	    		pstmt = conn.prepareStatement("DELETE FROM comment WHERE comment_num = ?");
    	    		pstmt.setInt(1, comment_num);
    	    		
    	    		int i = pstmt.executeUpdate();
    	    		
    	    		
    	    		pstmt = conn.prepareStatement("UPDATE board SET comment_cnt = comment_cnt - 1 WHERE board_num = ?");
    	    		pstmt.setInt(1,board_num);
    	    		
    	    		i = pstmt.executeUpdate();
    	    		
    	    		result = i+"";
    	    				
    	    	

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
    	    		}catch( Exception e ) {}	
    	    		try{
    	    			if ( pstmt != null ) pstmt.close();
    	    		}catch( Exception e ) {}
    	    		try{
    	    			if ( conn != null ) conn.close();
    	    		}catch( Exception e ) {}
    	    	}
    	return result;
    }
    
    
    public String search_board(String tag)
    {
        JSONObject result = new JSONObject();
        int count = 0;


        try {

                String board_tag = "%"+tag+"%";

                pstmt = conn.prepareStatement("SELECT a.board_num, a.board_content, a.date_board, a.good,a.board_latitude,a.board_longitude,b.nickname, b.user_photo, a.category_num,a.comment_cnt FROM board a, user_info b WHERE a.id = b.id AND (a.board_tag LIKE ? OR a.board_content LIKE ?) ORDER BY a.date_board DESC");
                // LIMIT ?,10
                pstmt.setString(1, board_tag);
                pstmt.setString(2, board_tag);
                rs = pstmt.executeQuery();

                JSONArray jrr = new JSONArray();

                while(rs.next())
                {
                        JSONObject jtmp = new JSONObject();

                        jtmp.put("board_num",rs.getInt(1));
                        jtmp.put("content",rs.getString(2));
                        jtmp.put("date_board",rs.getString(3));
                        jtmp.put("good",rs.getInt(4));
                        jtmp.put("board_latitude",rs.getDouble(5));
                        jtmp.put("board_longitude",rs.getDouble(6));
                        jtmp.put("user_id",rs.getString(7));
                        if(rs.getString(8).equals("No Photo"))
                        {
                                jtmp.put("user_photo",rs.getString(8));
                        }
                        else
                        {
                                jtmp.put("user_photo",path+"PlitImage/"+rs.getString(8));
                        }
            			jtmp.put("category",rs.getInt(9));
            			jtmp.put("comment_cnt",rs.getInt(10));
                        jrr.put(jtmp);
                        count++;

                }

                result.put("read_board_list_data", jrr);

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
                }catch( Exception e ) {}
                try{
                        if ( pstmt != null ) pstmt.close();
                }catch( Exception e ) {}
                try{
                        if ( conn != null ) conn.close();
                }catch( Exception e ) {}
        }

        try {
                        result.put("read_board_list_count", count);
                } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                }

        return result.toString();
    }
                                                                                     
    
    public String delete_friend(String id_applicant,String id_respondent)
    {
        String s = "-1";


        try {

                pstmt = conn.prepareStatement("DELETE FROM friends WHERE id_applicant = ? AND id_respondent = ?");
                // LIMIT ?,10
                pstmt.setString(1, id_applicant);
                pstmt.setString(2, id_respondent);

                pstmt.executeUpdate();

                s="0";
   //=======================================================================
        } catch ( SQLException e ) {
                s = "0";
                System.out.println("Login 1");
                e.printStackTrace();
        } catch ( Exception e ) {
                s = "0";
                System.out.println("Login 2");
                e.printStackTrace();
        } finally {
                try{
                        if ( rs != null ) rs.close();
                }catch( Exception e ) {}
                try{
                        if ( pstmt != null ) pstmt.close();
                }catch( Exception e ) {}
                try{
                        if ( conn != null ) conn.close();
                }catch( Exception e ) {}
        }

        return s;
    }

    
    
    public String change_massage(String massage,String id)
    {
        String s = "-1";


        try {

                pstmt = conn.prepareStatement("UPDATE user_info SET massage = ? WHERE id = ?");
                // LIMIT ?,10
                pstmt.setString(1, massage);
                pstmt.setString(2, id);

                s = pstmt.executeUpdate()+"";




   //=======================================================================
        } catch ( SQLException e ) {
                s = "0";
                System.out.println("Login 1");
                e.printStackTrace();
        } catch ( Exception e ) {
                s = "0";
                System.out.println("Login 2");
                e.printStackTrace();
        } finally {
                try{
                        if ( rs != null ) rs.close();
                }catch( Exception e ) {}
                try{
                        if ( pstmt != null ) pstmt.close();
                }catch( Exception e ) {}
                try{
                        if ( conn != null ) conn.close();
                }catch( Exception e ) {}
        }

        return s;
    }

    
    public String del_user_phto(String user_name)
    {
    
    	System.out.println("DAO PHOTO Strting");
    	String result = "-5";
    
    	try {                                                                                                                                                                                 

    		String photo_name = "";
    		
    		pstmt = conn.prepareStatement("SELECT user_photo FROM user_info WHERE id = ?");
    		pstmt.setString(1, user_name);
    		rs = pstmt.executeQuery();
    		
    		rs.next();
    	
    		photo_name = rs.getString(1);
    		
    		if(!photo_name.equals("No Photo"))
    		{
    		
    			boolean isdel = false;
    		
    			File file = new File(path2+"/PlitImage/"+photo_name);
    		
    			if( file.exists() ){
    				if(file.delete()){
    					isdel = true;
    				}else{
    					isdel = false;
    				}
    			}else{
    				System.out.println("No File");
    			}             	
    		
    			if(isdel)
    			{
    				pstmt = conn.prepareStatement("UPDATE user_info SET user_photo = 'No Photo' WHERE id = ?");
    				pstmt.setString(1, user_name);
    				int i = pstmt.executeUpdate();
        		
    				result = i+"";    				
    			}
    			else
				{
					result = "-15";
				}
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
    		}catch( Exception e ) {}	
    		try{
    			if ( pstmt != null ) pstmt.close();
    		}catch( Exception e ) {}
    		try{
    			if ( conn != null ) conn.close();
    		}catch( Exception e ) {}
    	}
 //=========================================================================   	
    	
    	System.out.println("DAO PHOTO COMPLETE");
    	return result;
    }
    
    
    
    public String write_user_phto(String user_name,String photo_name)
    {
    
    	System.out.println("DAO PHOTO Strting");
    	String result = "-5";
    
    	try {                                                                                                                                                                                 

    		pstmt = conn.prepareStatement("UPDATE user_info SET user_photo = ? WHERE id = ?");
    		pstmt.setString(1, photo_name);
    		pstmt.setString(2, user_name);
    		int i = pstmt.executeUpdate();
    		
    		result = i+"";
    				
    	

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
    		}catch( Exception e ) {}	
    		try{
    			if ( pstmt != null ) pstmt.close();
    		}catch( Exception e ) {}
    		try{
    			if ( conn != null ) conn.close();
    		}catch( Exception e ) {}
    	}
 //=========================================================================   	
    	
    	System.out.println("DAO PHOTO COMPLETE");
    	return result;
    }
    
    
    public String delete_board(int board_num)
    {			
    	String r = "-3";
    	try 
    	{                    
    	 		
    		pstmt = conn.prepareStatement("SELECT * FROM board_photo WHERE board_num = ?");
    		pstmt.setInt(1, board_num);
    		rs = pstmt.executeQuery();
    		
    		while(rs.next())
    		{  			
    			delete_board_photo(rs.getString(2));
    		}
    		
    		pstmt = conn.prepareStatement("DELETE FROM board WHERE board_num = ?");
    		pstmt.setInt(1, board_num);
    		r = pstmt.executeUpdate() + "";
    		
    		
    		
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
    		}catch( Exception e ) {}	
    		try{
    			if ( pstmt != null ) pstmt.close();
    		}catch( Exception e ) {}
    		try{
    			if ( conn != null ) conn.close();
    		}catch( Exception e ) {}
    	}
 //=========================================================================
    	
    	
    	
    	
    	return r;
    }
    
    
    public String count_friends(String user_name)
    {
    	String s = "";
    	
    	
    	try {                                                                                                                                                                                 

    		System.out.println(user_name);
    		pstmt = conn.prepareStatement("SELECT COUNT(id_applicant) FROM friends WHERE id_applicant = ?");
    		// LIMIT ?,10
    		pstmt.setString(1, user_name);
    		rs = pstmt.executeQuery();
    		
    		rs.next();
    		
    		s = s + rs.getInt(1)+",";
    		
    		pstmt = conn.prepareStatement("SELECT COUNT(id_respondent) FROM friends WHERE id_respondent = ?");
    		// LIMIT ?,10
    		pstmt.setString(1, user_name);
    		rs = pstmt.executeQuery();
    		
    		rs.next();
    		
    		s = s + rs.getInt(1);
    		
    			
    		
    		

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
    		}catch( Exception e ) {}	
    		try{
    			if ( pstmt != null ) pstmt.close();
    		}catch( Exception e ) {}
    		try{
    			if ( conn != null ) conn.close();
    		}catch( Exception e ) {}
    	}
    	
    	return s;
    }
    
    public String delete_board_photo(String photo_name)
    {
    	String r = "-5";
    	try 
    	{                    
    		
    		boolean isdel = false;
    		
    		File file = new File(path2+"/PlitImage/"+photo_name);
    		
    		if( file.exists() ){
                if(file.delete()){
                    isdel = true;
                }else{
                	isdel = false;
                }
            }else{
                System.out.println("�뙆�씪�씠 議댁옱�븯吏� �븡�뒿�땲�떎.");
            }             	
    		
    		if(isdel)
    		{
    			pstmt = conn.prepareStatement("DELETE FROM board_photo WHERE photo = ?");
    			pstmt.setString(1, photo_name);
    			r = pstmt.executeUpdate() + "";
    		}
    		else
    		{
    			r = "-15";
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
    		}catch( Exception e ) {}	
    		try{
    			if ( pstmt != null ) pstmt.close();
    		}catch( Exception e ) {}
    		try{
    			if ( conn != null ) conn.close();
    		}catch( Exception e ) {}
    	}
    	
    	return r;
    }
    
    public String change_board(int board_num, String Content, String tag,double longitude, double latitude)
    {			
    	String r = "-3";
    	try 
    	{                    
    	 		
    		pstmt = conn.prepareStatement("UPDATE board SET board_content = ?, board_tag = ?, date_board = SYSDATETIME, board_longitude = ?, board_latitude = ? WHERE board_num = ?");
    		pstmt.setString(1, Content);
    		pstmt.setString(2, tag);
    		pstmt.setDouble(3, longitude);
    		pstmt.setDouble(4, latitude);
    		pstmt.setInt(5, board_num);
    		
    		r = pstmt.executeUpdate() + "";
    		
    		
    		
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
    		}catch( Exception e ) {}	
    		try{
    			if ( pstmt != null ) pstmt.close();
    		}catch( Exception e ) {}
    		try{
    			if ( conn != null ) conn.close();
    		}catch( Exception e ) {}
    	}
 //=========================================================================
    	
    	
    	
    	
    	return r;
    }
    
    public String load_locate(String id)
    {
    	JSONObject result = new JSONObject();
    	int count = 0;
    	
    	
    	try {                                                                                                                                                                                 

    		pstmt = conn.prepareStatement("SELECT save_number,latitude,longitude,massage FROM save_locate WHERE id = ?");
    		// LIMIT ?,10
    		pstmt.setString(1, id);
    		rs = pstmt.executeQuery();
    		
    		JSONArray jrr = new JSONArray();
    	
    		while(rs.next())
    		{
    			JSONObject jtmp = new JSONObject();
    			
    			jtmp.put("save_number",rs.getInt(1));
    			jtmp.put("latitude",rs.getDouble(2));
    			jtmp.put("longitude",rs.getDouble(3));
    			jtmp.put("massage",rs.getString(4));
    			
    			jrr.put(jtmp);
    			count++;
    			
    		}
    		
    		result.put("save_locate_list", jrr);

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
    		}catch( Exception e ) {}	
    		try{
    			if ( pstmt != null ) pstmt.close();
    		}catch( Exception e ) {}
    		try{
    			if ( conn != null ) conn.close();
    		}catch( Exception e ) {}
    	}
    	
    	try {
			result.put("save_locate_count", count);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return result.toString();
    }
    
    
    public String delete_locate(int save_num)
    {
    	System.out.println("DAO Insert Strting");
    	String result = "-5";
    
    	try {                                                                                                                                                                                 

    		pstmt = conn.prepareStatement("DELETE FROM save_locate WHERE save_number = ?");
    		pstmt.setInt(1,save_num);
    	
    		
    		int i = pstmt.executeUpdate();
    		
    		result = i+"";
    				
    	

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
    		}catch( Exception e ) {}	
    		try{
    			if ( pstmt != null ) pstmt.close();
    		}catch( Exception e ) {}
    		try{
    			if ( conn != null ) conn.close();
    		}catch( Exception e ) {}
    	}
    	return result;
    }
    
    public String save_locate(String id, double lat,double lon,String massage)
    {
    	System.out.println("DAO Insert Strting");
    	String result = "-5";
    
    	try {                                                                                                                                                                                 

    		pstmt = conn.prepareStatement("INSERT INTO save_locate(id,latitude,longitude,massage) VALUES (?,?,?,?)");
    		pstmt.setString(1,id);
    		pstmt.setDouble(2, lat);
    		pstmt.setDouble(3, lon);
    		pstmt.setString(4,massage);
    		
    		
    		int i = pstmt.executeUpdate();
    		
    		result = i+"";
    				
    	

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
    		}catch( Exception e ) {}	
    		try{
    			if ( pstmt != null ) pstmt.close();
    		}catch( Exception e ) {}
    		try{
    			if ( conn != null ) conn.close();
    		}catch( Exception e ) {}
    	}
	return result;
    }
    
    
    
    public String read_board_camera(double lat,double lon)
    {
    	JSONObject result = new JSONObject();
    	int count = 0;
    	
    	
    	try {                                                                                                                                                                                 

    		pstmt = conn.prepareStatement("SELECT a.board_num, a.board_content, a.date_board, a.good, a.board_latitude,a.board_longitude,a.id,b.user_photo,b.nickname FROM board a, user_info b WHERE a.id = b.id and a.board_latitude = ?  AND a.board_longitude = ? ORDER BY a.date_board DESC");
    		// LIMIT ?,10
    		pstmt.setDouble(1, lat);
    		pstmt.setDouble(2, lon);
    		rs = pstmt.executeQuery();
    		
    		JSONArray jrr = new JSONArray();
    	
    		while(rs.next())
    		{
    			JSONObject jtmp = new JSONObject();
    			
    			jtmp.put("board_num",rs.getInt(1));
    			jtmp.put("content",rs.getString(2));
    			jtmp.put("date_board",rs.getString(3));
    			jtmp.put("good",rs.getInt(4));
    			jtmp.put("board_latitude",rs.getDouble(5));
    			jtmp.put("board_longitude",rs.getDouble(6));
    			jtmp.put("user_id",rs.getString(7));
    			if(rs.getString(8).equals("No Photo"))
    			{
    				jtmp.put("user_photo",rs.getString(8));
    			}
    			else
    			{
    				jtmp.put("user_photo",path+"PlitImage/"+rs.getString(8));
    			}
    			jtmp.put("nickname",rs.getString(9));
    			jrr.put(jtmp);
    			count++;
    			
    		}
    		
    		result.put("read_board_list_camera", jrr);

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
    		}catch( Exception e ) {}	
    		try{
    			if ( pstmt != null ) pstmt.close();
    		}catch( Exception e ) {}
    		try{
    			if ( conn != null ) conn.close();
    		}catch( Exception e ) {}
    	}
    	
    	try {
			result.put("read_board_camera_count", count);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return result.toString();
    }
    
    public int check_good(String board_num, String user_name)
    {
    	System.out.println("DAO Insert Strting");
    	String result = "-5";
    	int i = 0;
        ResultSet cg_rs = null;
    	try {        
    		
    		pstmt = conn.prepareStatement("SELECT * FROM board_good WHERE board_num = ? AND id = ?");
    		pstmt.setInt(1, Integer.parseInt(board_num));
    		pstmt.setString(2, user_name);
    		
    		cg_rs = pstmt.executeQuery();
    		
    		if(cg_rs.next())
    		{
    			i = 1;
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
    			if ( cg_rs != null ) cg_rs.close();
    		}catch( Exception e ) {}	
    	}
    	return i;
    }
    
    
    public String plus_good(String board_num, String user_name)
    {
    	System.out.println("DAO Insert Strting");
    	String result = "-5";
    
    	try {
    		
    		if(check_good(board_num,user_name) == 0)
    		{
    		
    		pstmt = conn.prepareStatement("INSERT INTO board_good VALUES (?,?)");
    		pstmt.setInt(1, Integer.parseInt(board_num));
    		pstmt.setString(2, user_name);
    		
    		int i = pstmt.executeUpdate();
    		

    		pstmt = conn.prepareStatement("UPDATE board SET good = good+1 WHERE board_num = ?");
    		pstmt.setInt(1, Integer.parseInt(board_num));
    		
    		i = pstmt.executeUpdate();
    		
    		result = 1+"";
    		}
    		else
    		{
        		pstmt = conn.prepareStatement("DELETE FROM board_good WHERE board_num = ? AND id = ?");
        		pstmt.setInt(1, Integer.parseInt(board_num));
        		pstmt.setString(2, user_name);
        		
        		int i = pstmt.executeUpdate();
        		

        		pstmt = conn.prepareStatement("UPDATE board SET good = good-1 WHERE board_num = ?");
        		pstmt.setInt(1, Integer.parseInt(board_num));
        		
        		i = pstmt.executeUpdate();
        		
        		result = 0+"";
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
    		}catch( Exception e ) {}	
    		try{
    			if ( pstmt != null ) pstmt.close();
    		}catch( Exception e ) {}
    		try{
    			if ( conn != null ) conn.close();
    		}catch( Exception e ) {}
    	}
    	return result;
    }
    
    
    public String membership(String user_id,String passwd,String birth)
    {
    	    	System.out.println("DAO Insert Strting");
    	    	String result = "-5";
    	    
    	    	try {                                                                                                                                                                                 

    	    		pstmt = conn.prepareStatement("INSERT INTO user_info VALUES (?,?,?,SYSDATETIME,'No Photo','No Message',?,0,0)");
    	    		pstmt.setString(1, user_id);
    	    		pstmt.setString(2, passwd);
    	    		pstmt.setString(3, birth);
    	    		pstmt.setString(4, user_id);
    	    		
    	    		int i = pstmt.executeUpdate();
    	    		
    	    		result = i+"";
    	    				
    	    	

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
    	    		}catch( Exception e ) {}	
    	    		try{
    	    			if ( pstmt != null ) pstmt.close();
    	    		}catch( Exception e ) {}
    	    		try{
    	    			if ( conn != null ) conn.close();
    	    		}catch( Exception e ) {}
    	    	}
    	return result;
    }
    
    
    public String read_myPage(String user_name)
    {
    	JSONObject result = new JSONObject();
    	
    	
    	try {                                                                                                                                                                                 

    		System.out.println(user_name);
    		pstmt = conn.prepareStatement("SELECT * FROM user_info WHERE id=?");
    		// LIMIT ?,10
    		pstmt.setString(1, user_name);
    		rs = pstmt.executeQuery();
    		
    		rs.next();
    		
    		System.out.println("mypage json start");
    		result.put("user_id",rs.getString(1));
    		result.put("date_birth",rs.getString(3));
    		result.put("date_member",rs.getString(4));
    		if(rs.getString(5).equals("No Photo"))
    		{
    			result.put("user_photo",rs.getString(5));
    		}
    		else
			{
    			result.put("user_photo",path+"PlitImage/"+rs.getString(5));
			}
    		result.put("massage",rs.getString(6));
    		result.put("nickname",rs.getString(7));
    		result.put("latitude",rs.getDouble(8));
    		result.put("longitude",rs.getDouble(9));
    		System.out.println("mypage json end");
    			
    		
    		

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
    		}catch( Exception e ) {}	
    		try{
    			if ( pstmt != null ) pstmt.close();
    		}catch( Exception e ) {}
    		try{
    			if ( conn != null ) conn.close();
    		}catch( Exception e ) {}
    	}
    	
    	return result.toString();
    }
    
    public String read_myboard(String user_name,int limit)
    {
    	JSONObject result = new JSONObject();
    	int count = 0;
    	
    	
    	try {                                                                                                                                                                                 

    		pstmt = conn.prepareStatement("SELECT a.board_num, a.board_content, a.date_board, a.good, a.board_latitude,a.board_longitude,a.id,b.user_photo,a.category_num,a.comment_cnt,b.nickname FROM board a, user_info b WHERE a.id = b.id AND a.id = ? ORDER BY a.date_board DESC LIMIT ?,10");
    		// LIMIT ?,10
    		pstmt.setString(1, user_name);
    		pstmt.setInt(2, limit);
    		rs = pstmt.executeQuery();
    		
    		JSONArray jrr = new JSONArray();
    	
    		while(rs.next())
    		{
    			JSONObject jtmp = new JSONObject();
    			
    			jtmp.put("board_num",rs.getInt(1));
    			jtmp.put("content",rs.getString(2));
    			jtmp.put("date_board",rs.getString(3));
    			jtmp.put("good",rs.getInt(4));
    			jtmp.put("board_latitude",rs.getDouble(5));
    			jtmp.put("board_longitude",rs.getDouble(6));
    			jtmp.put("user_id",rs.getString(7));
    			if(rs.getString(8).equals("No Photo"))
    			{
    				jtmp.put("user_photo",rs.getString(8));
    			}
    			else
    			{
    				jtmp.put("user_photo",path+"PlitImage/"+rs.getString(8));
    			}
    			jtmp.put("category",rs.getInt(9));
    			jtmp.put("comment_cnt",rs.getInt(10));
    			jtmp.put("nickname",rs.getString(11));
    			jrr.put(jtmp);
    			count++;
    			
    		}
    		
    		result.put("read_board_list_data", jrr);

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
    		}catch( Exception e ) {}	
    		try{
    			if ( pstmt != null ) pstmt.close();
    		}catch( Exception e ) {}
    		try{
    			if ( conn != null ) conn.close();
    		}catch( Exception e ) {}
    	}
    	
    	try {
			result.put("read_board_list_count", count);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return result.toString();
    }
    
    public String read_board_List(double min_lat,double max_lat,double min_lon,double max_lon)
    {
    	JSONObject result = new JSONObject();
    	int count = 0;
    	
    	
    	try {                                                                                                                                                                                 

    		pstmt = conn.prepareStatement("SELECT a.board_num, a.board_content, a.date_board, a.good, a.board_latitude,a.board_longitude,a.id,b.user_photo,a.category_num,a.comment_cnt,b.nickname FROM board a, user_info b WHERE a.id = b.id and a.board_latitude >= ? AND a.board_latitude <= ? AND a.board_longitude >= ? AND a.board_longitude <= ? ORDER BY b.id DESC");
    		// LIMIT ?,10
    		pstmt.setDouble(1, min_lat);
    		pstmt.setDouble(2, max_lat);
    		pstmt.setDouble(3, min_lon);;
    		pstmt.setDouble(4, max_lon);
    		rs = pstmt.executeQuery();
    		
    		JSONArray jrr = new JSONArray();
    	
    		while(rs.next())
    		{
    			JSONObject jtmp = new JSONObject();
    			
    			jtmp.put("board_num",rs.getInt(1));
    			jtmp.put("content",rs.getString(2));
    			jtmp.put("date_board",rs.getString(3));
    			jtmp.put("good",rs.getInt(4));
    			jtmp.put("board_latitude",rs.getDouble(5));
    			jtmp.put("board_longitude",rs.getDouble(6));
    			jtmp.put("user_id",rs.getString(7));
    			if(rs.getString(8).equals("No Photo"))
    			{
    				jtmp.put("user_photo",rs.getString(8));
    			}
    			else
    			{
    				jtmp.put("user_photo",path+"PlitImage/"+rs.getString(8));
    			}  			
    			jtmp.put("category",rs.getInt(9));
    			jtmp.put("comment_cnt",rs.getInt(10));
    			jtmp.put("nickname",rs.getString(7));
    			jrr.put(jtmp);
    			count++;
    			
    		}
    		
    		result.put("read_board_list_data", jrr);

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
    		}catch( Exception e ) {}	
    		try{
    			if ( pstmt != null ) pstmt.close();
    		}catch( Exception e ) {}
    		try{
    			if ( conn != null ) conn.close();
    		}catch( Exception e ) {}
    	}
    	
    	try {
			result.put("read_board_list_count", count);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return result.toString();
    }
    
    
    
    public String read_board_List(double min_lat,double max_lat,double min_lon,double max_lon,int limit)
    {
    	JSONObject result = new JSONObject();
    	int count = 0;
    	
    	
    	try {                                                                                                                                                                                 

    		pstmt = conn.prepareStatement("SELECT a.board_num, a.board_content, a.date_board, a.good, a.board_latitude,a.board_longitude,a.id,b.user_photo,a.category_num,a.comment_cnt,b.nickname FROM board a, user_info b WHERE a.id = b.id and a.board_latitude >= ? AND a.board_latitude <= ? AND a.board_longitude >= ? AND a.board_longitude <= ? ORDER BY a.date_board DESC LIMIT ?,10");
    		// LIMIT ?,10
    		pstmt.setDouble(1, min_lat);
    		pstmt.setDouble(2, max_lat);
    		pstmt.setDouble(3, min_lon);;
    		pstmt.setDouble(4, max_lon);
    		pstmt.setInt(5, limit);
    		rs = pstmt.executeQuery();
    		
    		JSONArray jrr = new JSONArray();
    	
    		while(rs.next())
    		{
    			JSONObject jtmp = new JSONObject();
    			
    			jtmp.put("board_num",rs.getInt(1));
    			jtmp.put("content",rs.getString(2));
    			jtmp.put("date_board",rs.getString(3));
    			jtmp.put("good",rs.getInt(4));
    			jtmp.put("board_latitude",rs.getDouble(5));
    			jtmp.put("board_longitude",rs.getDouble(6));
    			jtmp.put("user_id",rs.getString(7));
    			if(rs.getString(8).equals("No Photo"))
    			{
    				jtmp.put("user_photo",rs.getString(8));
    			}
    			else
    			{
    				jtmp.put("user_photo",path+"PlitImage/"+rs.getString(8));
    			}
    			jtmp.put("category",rs.getInt(9));
    			jtmp.put("comment_cnt",rs.getInt(10));
    			jtmp.put("nickname",rs.getString(7));
    			jrr.put(jtmp);
    			count++;
    			
    		}
    		
    		result.put("read_board_list_data", jrr);

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
    		}catch( Exception e ) {}	
    		try{
    			if ( pstmt != null ) pstmt.close();
    		}catch( Exception e ) {}
    		try{
    			if ( conn != null ) conn.close();
    		}catch( Exception e ) {}
    	}
    	
    	try {
			result.put("read_board_list_count", count);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return result.toString();
    }
    
    
    public String read_board_location(double min_lat,double max_lat,double min_lon,double max_lon)
    {
    	JSONObject result = new JSONObject();
    	int count = 0;
    	
    	
    	try {                                                                                                                                                                                 

    		pstmt = conn.prepareStatement("SELECT board_latitude,board_longitude,COUNT(*) FROM board WHERE board_latitude >=? AND board_latitude <= ? AND board_longitude >= ? AND board_longitude <= ? GROUP BY board_latitude,board_longitude ORDER BY board_latitude,board_longitude");
    		pstmt.setDouble(1, min_lat);
    		pstmt.setDouble(2, max_lat);
    		pstmt.setDouble(3, min_lon);;
    		pstmt.setDouble(4, max_lon);
    		rs = pstmt.executeQuery();
    		
    		JSONArray jrr = new JSONArray();
    	
			ResultSet res = null;
			ResultSet tres = null;
    		
    		while(rs.next())
    		{
    			 			
    			JSONObject jtmp = new JSONObject();
    			JSONArray tjrr = new JSONArray();
    			
    			pstmt = conn.prepareStatement("SELECT a.board_num, a.board_content, a.id,b.user_photo,b.nickname FROM board a, user_info b WHERE a.id = b.id and a.board_latitude = ?  AND a.board_longitude = ? ORDER BY a.date_board DESC");
    			pstmt.setDouble(1, rs.getDouble(1));
    			pstmt.setDouble(2, rs.getDouble(2));
    			res = pstmt.executeQuery();
    			
    			while(res.next())
    			{
    				
        			JSONObject tjob = new JSONObject();
    				
    				tjob.put("board_num",res.getInt(1));
    				tjob.put("content",res.getString(2));
    				tjob.put("id",res.getString(3));
    				tjob.put("nickname",res.getString(5));
    				//tjob.put("user_photo",res.getString(4));
    				
        			if(res.getString(4).equals("No Photo"))
        			{
        				tjob.put("user_photo",res.getString(4));
        			}
        			else
        			{
        				tjob.put("user_photo",path+"PlitImage/"+res.getString(4));
        			}  	
    			
    				pstmt = conn.prepareStatement("SELECT COUNT(*),photo FROM board_photo WHERE board_num = ? GROUP BY board_num");
    				pstmt.setInt(1, res.getInt(1));
    				tres = pstmt.executeQuery();
    				
    				if(tres.next())
    				{
    					tjob.put("is_Photo",1);
    					tjob.put("photo_count",tres.getInt(1));
    					tjob.put("board_photo",path+"PlitImage/"+tres.getString(2));
    				}
    				else
    				{
    					tjob.put("is_Photo",0);
    				}
    				
    				tjrr.put(tjob);
    			}
    			jtmp.put("board_latitude",rs.getDouble(1));
    			jtmp.put("board_longitude",rs.getDouble(2));
    			jtmp.put("board_count",rs.getInt(3));
    			jtmp.put("board_data", tjrr);
    			
    			
    			jrr.put(jtmp);
    			count++;
    			
    		}
    		
    		result.put("read_board_location_data", jrr);

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
    		}catch( Exception e ) {}	
    		try{
    			if ( pstmt != null ) pstmt.close();
    		}catch( Exception e ) {}
    		try{
    			if ( conn != null ) conn.close();
    		}catch( Exception e ) {}
    	}
    	
    	try {
			result.put("read_board_location_count", count);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return result.toString();
    }
    
    
    public String add_frirends(String id_applicant, String id_respondent)
    {
    	System.out.println("DAO Insert Strting");
    	String result = "-5";
    	int i;
    
    	try {                                       
   		
    		
    		pstmt = conn.prepareStatement("SELECT * FROM friends WHERE id_applicant = ? AND id_respondent = ?");
    		pstmt.setString(1, id_applicant);
    		pstmt.setString(2, id_respondent);
    		rs = pstmt.executeQuery();
    		
    		
    		if(rs.next())
    		{
    			result = delete_friend(id_applicant,id_respondent);
    		}
    		else
    		{

    		pstmt = conn.prepareStatement("INSERT INTO friends VALUES (?,?)");
    		pstmt.setString(1, id_applicant);
    		pstmt.setString(2, id_respondent);
    		
    		i = pstmt.executeUpdate();
    		
    		result = i+"";
    		
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
    		}catch( Exception e ) {}	
    		try{
    			if ( pstmt != null ) pstmt.close();
    		}catch( Exception e ) {}
    		try{
    			if ( conn != null ) conn.close();
    		}catch( Exception e ) {}
    	}
    	return result;
    }
    
    
    public String read_friends(String user_name)
    {
    	JSONObject result = new JSONObject();
    	int count = 0;
    	
    	
    	try {                                                                                                                                                                                 

    		pstmt = conn.prepareStatement("SELECT a.id_respondent, b.user_photo, b.massage FROM friends a, user_info b WHERE a.id_respondent = b.id AND a.id_applicant = ?");
    		pstmt.setString(1, user_name);
    		rs = pstmt.executeQuery();
    		
    		JSONArray jrr = new JSONArray();
    	
    		while(rs.next())
    		{
    			JSONObject jtmp = new JSONObject();
    			
    			jtmp.put("user_id",rs.getString(1));
    			if(rs.getString(2).equals("No Photo"))
    			{
    				jtmp.put("user_photo",rs.getString(2));
    			}
    			else
    			{
    				jtmp.put("user_photo",path+"PlitImage/"+rs.getString(2));
    			}
    			jtmp.put("message",rs.getString(3));
    			
    			jrr.put(jtmp);
    			count++;
    			
    		}
    		
    		result.put("read_friends_data", jrr);

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
    		}catch( Exception e ) {}	
    		try{
    			if ( pstmt != null ) pstmt.close();
    		}catch( Exception e ) {}
    		try{
    			if ( conn != null ) conn.close();
    		}catch( Exception e ) {}
    	}
    	
    	try {
			result.put("friends_count", count);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return result.toString();
    }
    
    
    public String read_comment(String board_num)
    {
    	JSONObject result = new JSONObject();
    	int count = 0;
    	
    	
    	try {                                                                                                                                                                                 

    		pstmt = conn.prepareStatement("SELECT a.board_num, a.comment_num, a.comment_date, a.comment_content, a.comment_id, b.user_photo , a.comment_photo, a.comment_pw, a.comment_nickname, a.user_photo FROM comment a , user_info b WHERE a.comment_id = b.id AND a.board_num = ?");
    		pstmt.setInt(1, Integer.parseInt(board_num));
    		rs = pstmt.executeQuery();
    		
    		JSONArray jrr = new JSONArray();
    	
    		while(rs.next())
    		{
    			JSONObject jtmp = new JSONObject();
    			
    			jtmp.put("board_num",rs.getInt(1));
    			jtmp.put("comment_num",rs.getInt(2));
    			jtmp.put("comment_date",rs.getString(3));
    			jtmp.put("comment_content",rs.getString(4));
    			jtmp.put("comment_id",rs.getString(5));
    			if(rs.getString(6).equals("No Photo"))
    			{
    				jtmp.put("user_photo",rs.getString(6));
    			}
    			else
    			{
    				jtmp.put("user_photo",path+"PlitImage/"+rs.getString(6));
    			}
    			if(rs.getString(7).equals("No Photo"))
    			{
    				jtmp.put("comment_photo",rs.getString(7));
    			}
    			else
    			{
    				jtmp.put("comment_photo",path+"PlitImage/"+rs.getString(7));
    			}
    			jtmp.put("comment_pw",rs.getString(8));
    			jtmp.put("comment_nickname",rs.getString(9));
    			jtmp.put("guest_photo",rs.getInt(10));
    			jrr.put(jtmp);
    			count++;
    			
    		}
    		
    		result.put("read_comment_data", jrr);

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
    		}catch( Exception e ) {}	
    		try{
    			if ( pstmt != null ) pstmt.close();
    		}catch( Exception e ) {}
    		try{
    			if ( conn != null ) conn.close();
    		}catch( Exception e ) {}
    	}
    	
    	
    	try {
			result.put("com_count", count);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return result.toString();
    }
    
    
    public String write_Comment(String board_num,String user_name,String content,String pw,String nickname, int guestPhoto)
    {
    	    	System.out.println("DAO Insert Strting");
    	    	String result = "-5";
    	    	String user_nickname = nickname;
    	    
    	    	try { 
    	    		if(!user_name.equals("Guest"))
    	    		{
        	    		pstmt = conn.prepareStatement("SELECT nickname FROM user_info WHERE id = ?");
        	    		pstmt.setString(1,user_name);
        	    		
        	    		
        	    		rs = pstmt.executeQuery();
        	    		
        	    		if(rs.next())
        	    		{
        	    			user_nickname = rs.getString(1);
        	    		}
    	    		}

    	    		pstmt = conn.prepareStatement("INSERT INTO comment(board_num,comment_date,comment_content,comment_ID,comment_photo,comment_pw,comment_nickname,user_photo) VALUES (?,SYSDATETIME,?,?,'No Photo',?,?,?)");
    	    		pstmt.setInt(1, Integer.parseInt(board_num));
    	    		pstmt.setString(2, content);
    	    		pstmt.setString(3, user_name);
    	    		pstmt.setString(4, pw);
    	    		pstmt.setString(5, user_nickname);
    	    		pstmt.setInt(6, guestPhoto);
    	    		
    	    		int i = pstmt.executeUpdate();
    	    		
    	    		pstmt = conn.prepareStatement("SELECT comment_num FROM comment WHERE board_num = ? ORDER BY comment_num DESC LIMIT 1");
    	    		pstmt.setInt(1, Integer.parseInt(board_num));
    	    		
    	    		
    	    		rs = pstmt.executeQuery();
    	    		
    	    		if(rs.next())
    	    		{
    	    			result = rs.getInt(1)+"";
    	    		}   	    		
    	    	   	    		
    	        	    		
    	    		pstmt = conn.prepareStatement("UPDATE board SET comment_cnt = comment_cnt + 1 WHERE board_num = ?");
    	    		pstmt.setInt(1, Integer.parseInt(board_num));
    	    		
    	    		i = pstmt.executeUpdate();
    	    		
    	    	
    	    				
    	    	

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
    	    		}catch( Exception e ) {}	
    	    		try{
    	    			if ( pstmt != null ) pstmt.close();
    	    		}catch( Exception e ) {}
    	    		try{
    	    			if ( conn != null ) conn.close();
    	    		}catch( Exception e ) {}
    	    	}
    	return result;
    }
    
    public String read_board_info(String board_num,String user_name)
    {	
    	
    	String r = "-1";
    	
    	try {                                                                                                                                                                                 

    		pstmt = conn.prepareStatement("SELECT a.board_num, a.board_content, a.date_board, a.good, a.hits, a.board_latitude,a.board_longitude,a.id,b.user_photo FROM board a, user_info b WHERE a.id = b.id AND a.board_num = ?");
    		pstmt.setInt(1,Integer.parseInt(board_num));
    		rs = pstmt.executeQuery();
    		
    		rs.next();
    		
    		JSONObject j = new JSONObject();
    		j.put("board_num",rs.getInt(1));
    		j.put("content",rs.getString(2));
    		j.put("date",rs.getString(3));
    		j.put("good",rs.getInt(4));
    		j.put("hits",rs.getInt(5));
    		j.put("latitude",rs.getDouble(6));
    		j.put("longitude",rs.getDouble(7));
    		j.put("user_id",rs.getString(8));
    		//j.put("user_photo",rs.getString(9));
    		if(rs.getString(9).equals("No Photo"))
			{
    			j.put("user_photo",rs.getString(9));
			}
			else
			{
				j.put("user_photo",path+"PlitImage/"+rs.getString(9));
			}
    		if(check_good(board_num,user_name)==0)
    		{
    			j.put("is_good",1);
    		}
    		else
    		{
    			j.put("is_good",0);
    		}
    		
    		
    		pstmt = conn.prepareStatement("SELECT * FROM board_photo WHERE board_num = ?;");
    		pstmt.setInt(1,j.getInt("board_num"));
    		rs = pstmt.executeQuery();
    		
    		JSONArray jrr = new JSONArray();
    		int count = 0;
    		while(rs.next())
    		{
    			System.out.println("jrr start");
    			JSONObject tmp = new JSONObject();
    			tmp.put("board_photo",(path+"PlitImage/"+rs.getString(2)));
    			System.out.println(tmp);
    			jrr.put(tmp);
    			System.out.println("jrr end");
    			count++;
    		}
    		System.out.println("jrr puts");
    		j.put("b_photos", jrr);
    		j.put("img_count", count);
    		System.out.println("jrr pute");
    		
    		r = j.toString();
    		
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
    		}catch( Exception e ) {}	
    		try{
    			if ( pstmt != null ) pstmt.close();
    		}catch( Exception e ) {}
    		try{
    			if ( conn != null ) conn.close();
    		}catch( Exception e ) {}
    	}
    	
    //=======================================================================	
    	
    	
    	return r;
    }
    
    public String write_board_phto(String board_num,String photo_name)
    {
    
    	System.out.println("DAO PHOTO Strting");
    	String result = "-5";
    
    	try {                                                                                                                                                                                 

    		pstmt = conn.prepareStatement("INSERT INTO board_photo VALUES (?,?)");
    		pstmt.setInt(1, Integer.parseInt(board_num));
    		pstmt.setString(2, photo_name);
    		int i = pstmt.executeUpdate();
    		
    		result = i+"";
    				
    	

    		
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
    		}catch( Exception e ) {}	
    		try{
    			if ( pstmt != null ) pstmt.close();
    		}catch( Exception e ) {}
    		try{
    			if ( conn != null ) conn.close();
    		}catch( Exception e ) {}
    	}
 //=========================================================================   	
    	
    	System.out.println("DAO PHOTO COMPLETE");
    	return result;
    }
    
   public String write_board(String Content, String user_name, String tag, double latitude ,double longitude,int category)
    {
    	String board_num = "-1";

    	
    			
    	try 
    	{                    
    		double board_latitude = latitude;
    		double board_longitude = longitude;
    		
    		
    		pstmt = conn.prepareStatement("SELECT board_latitude,board_longitude FROM board WHERE board_latitude >= ? - 0.0001 AND  board_latitude <= ? + 0.0001 AND board_longitude >= ? - 0.0001 AND board_longitude <= ? + 0.0001 LIMIT 1");
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
    		 		
    		rs.close();
    		pstmt.close();
    		rs = null;
    		pstmt = null;
    		
    		
    		pstmt = conn.prepareStatement("INSERT INTO board(board_content,date_board,good,hits,board_tag,board_latitude,board_longitude,ID,category_num,comment_cnt) VALUES (?,SYSDATETIME,0,0,?,?,?,?,?,0)");
    		pstmt.setString(1, Content);
    		pstmt.setString(2, tag);
    		pstmt.setDouble(3, board_latitude);
    		pstmt.setDouble(4, board_longitude);
    		pstmt.setString(5, user_name);
    		pstmt.setInt(6, category);
    		int s = pstmt.executeUpdate();
    		
    		pstmt.close();
    		pstmt = null;
    		
    		pstmt = conn.prepareStatement("SELECT board_num FROM board WHERE ID = ? ORDER BY board_num DESC LIMIT 1");
    		pstmt.setString(1, user_name);
    		rs = pstmt.executeQuery();
    		if(rs.next())
    		{
    			board_num = rs.getString(1);
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
    		}catch( Exception e ) {}	
    		try{
    			if ( pstmt != null ) pstmt.close();
    		}catch( Exception e ) {}
    		try{
    			if ( conn != null ) conn.close();
    		}catch( Exception e ) {}
    	}
 //=========================================================================
    	
    	
    	
    	
    	return board_num;
    }
   public String check_Id(String ID)
   {
   
   	System.out.println("DAO Login Strting");
   	String result = "-5";
   
   	try {                                                                                                                                                                                 

   		pstmt = conn.prepareStatement("SELECT ID FROM user_info WHERE ID = ?");
   		pstmt.setString(1, ID);
   		rs = pstmt.executeQuery();
   		
   		if(rs.next())
   		{
   			result = "0";
   		}
   		else
   		{
   			result = "1";
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
   		}catch( Exception e ) {}	
   		try{
   			if ( pstmt != null ) pstmt.close();
   		}catch( Exception e ) {}
   		try{
   			if ( conn != null ) conn.close();
   		}catch( Exception e ) {}
   	}
//=========================================================================   	
   	
   	System.out.println("DAO LOGIN COMPLETE");
   	return result;
   
   }
	
    
    public String Login(String ID,String PW)
    {
    
    	System.out.println("DAO Login Strting");
    	String result = "-5";
    
    	try {                                                                                                                                                                                 

    		pstmt = conn.prepareStatement("select * from user_info where id = ? and passwd = ?");
    		pstmt.setString(1, ID);
    		pstmt.setString(2, PW);
    		rs = pstmt.executeQuery();
    		
    		if(rs.next())
    		{
    			result = "1";
    		}
    		else
    		{
    			result = "0";
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
    		}catch( Exception e ) {}	
    		try{
    			if ( pstmt != null ) pstmt.close();
    		}catch( Exception e ) {}
    		try{
    			if ( conn != null ) conn.close();
    		}catch( Exception e ) {}
    	}
 //=========================================================================   	
    	
    	System.out.println("DAO LOGIN COMPLETE");
    	return result;

    }
	
}
