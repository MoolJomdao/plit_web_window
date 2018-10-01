package dao;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import bean.Comment;
import bean.Load_Locate;
import bean.Read_Board;
import bean.Read_Board_Info;
import bean.Read_Board_List;
import bean.Read_Board_Location;
import bean.Read_Friends;
import bean.Read_Myboard;
import bean.Read_Mypage;
import bean.User_Info;
import db.GpsToAddress;

public class Dao {

	Connection conn = null;

	PreparedStatement pstmt = null;

	ResultSet rs = null;

	//String path = "http://39.127.8.20:8080/";
	//String path2 = "/var/lib/tomcat7/webapps/ROOT";

	String path = "";
	String path2 = "";

	public Dao()

	{
		try {

			System.out.println("connecting DB");

			Context initContext = new InitialContext();

			DataSource ds = (DataSource) initContext.lookup("java:comp/env/jdbc/CUBRIDDS");

			conn = ds.getConnection();

			System.out.println("connect complete");

		} catch (SQLException e) {
			System.out.println("DAO 1");

			e.printStackTrace();

		} catch (Exception e) {
			System.out.println("DAO 2");

			e.printStackTrace();

		}

	}

	public int delete_comment(int board_num, int comment_num) {
		System.out.println("DAO Insert Strting");
		int result = -1;

		try {

			pstmt = conn.prepareStatement("DELETE FROM comment WHERE comment_num = ?");
			pstmt.setInt(1, comment_num);

			int i = pstmt.executeUpdate();

			pstmt = conn.prepareStatement("UPDATE board SET comment_cnt = comment_cnt - 1 WHERE board_num = ?");
			pstmt.setInt(1, board_num);

			i = pstmt.executeUpdate();

			result = i;

			// =======================================================================
		} catch (SQLException e) {
			System.out.println("Login 1");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("Login 2");
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (Exception e) {
			}
			try {
				if (conn != null)
					conn.close();
			} catch (Exception e) {
			}
		}
		return result;
	}

	public ArrayList<Read_Board_List> search_board(String tag) // stop
	{
		ArrayList<Read_Board_List> arr = new ArrayList<>();
		Read_Board_List sb = null;
		ResultSet photoResult = null;

		try {
			String board_tag = "%" + tag + "%";

			pstmt = conn.prepareStatement("SELECT "
					+ "a.board_num, " 		// 1
					+ "a.board_content, "	// 2
					+ "a.date_board, "		// 3
					+ "a.good,"				// 4
					+ "a.board_latitude,"	// 5
					+ "a.board_longitude,"	// 6
					+ "a.id,"				// 7
					+ "b.user_photo, "		// 8
					+ "a.category_num,"		// 9
					+ "a.comment_cnt, "		// 10
					+ "b.nickname "			// 11
					+ "FROM board a, user_info b "
					+ "WHERE a.id = b.id AND (a.board_tag LIKE ? OR a.board_content LIKE ?) "
					+ "ORDER BY a.date_board DESC");
            // LIMIT ?,10
            pstmt.setString(1, board_tag);
            pstmt.setString(2, board_tag);
            rs = pstmt.executeQuery();

			while (rs.next()) {
				sb = new Read_Board_List();
				sb.setBoardNum(rs.getInt(1));
				sb.setContent(rs.getString(2));
				sb.setBoardLatitude(rs.getDouble(5));
				sb.setBoardLongitude(rs.getDouble(6));
				sb.setUserId(rs.getString(7));
				sb.storeName = rs.getString(11);

				if (rs.getString(8).equals("No Photo")) {
					sb.setUserPhoto(rs.getString(8));
				} else {
					sb.setUserPhoto(path + "PlitImage/" + rs.getString(8));
				}

				pstmt = conn.prepareStatement("SELECT * FROM board_photo WHERE board_num = ?;");
				pstmt.setInt(1, sb.getBoardNum());
				photoResult = pstmt.executeQuery();
				
				if( photoResult.next() )
					sb.setBoardPhoto(path + "PlitImage/" + photoResult.getString(2));
				
				arr.add(sb);
			}

			// =======================================================================
		} catch (SQLException e) {
			System.out.println("Search_Board SQL error");
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			System.out.println("Search_Board Exception");
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (Exception e) {
			}
		}

		return arr;
	}

	public ArrayList<Read_Board_List> search_board_from_location(String tag) // stop
	{
		ArrayList<Read_Board_List> arr = new ArrayList<>();
		Read_Board_List sb = null;
		ResultSet photoResult = null;
		int boardNum;
		double lat, lng;
		boolean isAddress;
		ArrayList<Integer> boardNums = new ArrayList<Integer>();
		try {
				
			pstmt = conn.prepareStatement("SELECT board_num, board_latitude, board_longitude FROM board");
            rs = pstmt.executeQuery();
            
            while( rs.next() )
            {
    			boardNum = rs.getInt(1);
    			lat = rs.getDouble(2);
    			lng = rs.getDouble(3);

    			if( lat != 0.0 && lng != 0.0 )
    			{
    				GpsToAddress GTA = new GpsToAddress( lat, lng );
    				String address = GTA.getAddress();
    				isAddress = address.contains(tag);
    				
    				if( isAddress ) // 찾고자하는 주소가 포함되어 있다면
    				{
    					boardNums.add( boardNum );
    				}
    			}
            }
			
            for( int i=0; i<boardNums.size(); i++)
            {
				pstmt = conn.prepareStatement("SELECT "
						+ "a.board_num, " 		// 1
						+ "a.board_content, "	// 2
						+ "a.date_board, "		// 3
						+ "a.good,"				// 4
						+ "a.board_latitude,"	// 5
						+ "a.board_longitude,"	// 6
						+ "a.id,"				// 7
						+ "b.user_photo, "		// 8
						+ "a.category_num,"		// 9
						+ "a.comment_cnt, "		// 10
						+ "b.nickname "			// 11
						+ "FROM board a, user_info b "
						+ "WHERE a.id = b.id AND a.board_num = ? "
						+ "ORDER BY a.date_board DESC");
	            // LIMIT ?,10
	            pstmt.setInt(1, boardNums.get(i));
	            rs = pstmt.executeQuery();
	
				if (rs.next()) {
					sb = new Read_Board_List();
					sb.setBoardNum(rs.getInt(1));
					sb.setContent(rs.getString(2));
					sb.setBoardLatitude(rs.getDouble(5));
					sb.setBoardLongitude(rs.getDouble(6));
					sb.setUserId(rs.getString(7));
					sb.storeName = rs.getString(11);
	
					if (rs.getString(8).equals("No Photo")) {
						sb.setUserPhoto(rs.getString(8));
					} else {
						sb.setUserPhoto(path + "PlitImage/" + rs.getString(8));
					}
	
					pstmt = conn.prepareStatement("SELECT * FROM board_photo WHERE board_num = ?;");
					pstmt.setInt(1, sb.getBoardNum());
					photoResult = pstmt.executeQuery();
					
					if( photoResult.next() )
						sb.setBoardPhoto(path + "PlitImage/" + photoResult.getString(2));
					
					arr.add(sb);
				}
            }

			// =======================================================================
		} catch (SQLException e) {
			System.out.println("Search_Board SQL error");
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			System.out.println("Search_Board Exception");
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (Exception e) {
			}
		}

		return arr;
	}

	public String delete_friend(String id_applicant, String id_respondent) {
		String s = "-1";

		try {

			pstmt = conn.prepareStatement("DELETE FROM friends WHERE id_applicant = ? AND id_respondent = ?");
			// LIMIT ?,10
			pstmt.setString(1, id_applicant);
			pstmt.setString(2, id_respondent);

			pstmt.executeUpdate();

			s = "0";
			// =======================================================================
		} catch (SQLException e) {
			s = "0";
			System.out.println("Login 1");
			e.printStackTrace();
		} catch (Exception e) {
			s = "0";
			System.out.println("Login 2");
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (Exception e) {
			}
			try {
				if (conn != null)
					conn.close();
			} catch (Exception e) {
			}
		}

		return s;
	}

	public String change_massage(String massage, String id) {
		String s = "-1";

		try {

			pstmt = conn.prepareStatement("UPDATE user_info SET massage = ? WHERE id = ?");
			// LIMIT ?,10
			pstmt.setString(1, massage);
			pstmt.setString(2, id);

			s = pstmt.executeUpdate() + "";

			// =======================================================================
		} catch (SQLException e) {
			s = "0";
			System.out.println("Login 1");
			e.printStackTrace();
		} catch (Exception e) {
			s = "0";
			System.out.println("Login 2");
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (Exception e) {
			}
			try {
				if (conn != null)
					conn.close();
			} catch (Exception e) {
			}
		}

		return s;
	}

	public String del_user_phto(String user_name) {

		System.out.println("DAO PHOTO Strting");
		String result = "-5";

		try {

			String photo_name = "";

			pstmt = conn.prepareStatement("SELECT user_photo FROM user_info WHERE id = ?");
			pstmt.setString(1, user_name);
			rs = pstmt.executeQuery();

			rs.next();

			photo_name = rs.getString(1);

			if (!photo_name.equals("No Photo")) {

				boolean isdel = false;

				File file = new File(path2 + "/PlitImage/" + photo_name);

				if (file.exists()) {
					if (file.delete()) {
						isdel = true;
					} else {
						isdel = false;
					}
				} else {
					System.out.println("No File");
				}

				if (isdel) {
					pstmt = conn.prepareStatement("UPDATE user_info SET user_photo = 'No Photo' WHERE id = ?");
					pstmt.setString(1, user_name);
					int i = pstmt.executeUpdate();

					result = i + "";
				} else {
					result = "-15";
				}
			}
			// =======================================================================
		} catch (SQLException e) {
			System.out.println("Login 1");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("Login 2");
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (Exception e) {
			}
			try {
				if (conn != null)
					conn.close();
			} catch (Exception e) {
			}
		}
		// =========================================================================

		System.out.println("DAO PHOTO COMPLETE");
		return result;
	}

	public String write_user_phto(String user_name, String photo_name) {

		System.out.println("DAO PHOTO Strting");
		String result = "-5";

		try {

			pstmt = conn.prepareStatement("UPDATE user_info SET user_photo = ? WHERE id = ?");
			pstmt.setString(1, photo_name);
			pstmt.setString(2, user_name);
			int i = pstmt.executeUpdate();

			result = i + "";

			// =======================================================================
		} catch (SQLException e) {
			System.out.println("Login 1");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("Login 2");
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (Exception e) {
			}
			try {
				if (conn != null)
					conn.close();
			} catch (Exception e) {
			}
		}
		// =========================================================================

		System.out.println("DAO PHOTO COMPLETE");
		return result;
	}

	public String delete_board(int board_num, String contextPath) {
		String r = "-1";
		try {

			pstmt = conn.prepareStatement("SELECT * FROM board_photo WHERE board_num = ?");
			pstmt.setInt(1, board_num);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				
				File file = new File(contextPath + "PlitImage/" + rs.getString(2));

				if (file.exists()) 
				{
					file.delete();
				} 
				else 
				{
					System.out.println( contextPath + "PlitImage/" + rs.getString(2) + "파일이 존재하지 않습니다.");
				}

			}
			// board_num 과 관련된 모든 사진 삭제
			pstmt = conn.prepareStatement("DELETE FROM board_photo WHERE board_num = ?");
			pstmt.setInt(1, board_num);
			r = pstmt.executeUpdate() + "";
			
			// board_num에 맞는 board 삭제
			pstmt = conn.prepareStatement("DELETE FROM board WHERE board_num = ?");
			pstmt.setInt(1, board_num);
			r = pstmt.executeUpdate() + "";

			// =======================================================================
		} catch (SQLException e) {
			System.out.println("delete_board failed");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("delete_board failed");
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (Exception e) {
			}
		}
		// =========================================================================

		return r;
	}

	public String count_friends(String user_name) {
		String s = "";

		try {

			System.out.println(user_name);
			pstmt = conn.prepareStatement("SELECT COUNT(id_applicant) FROM friends WHERE id_applicant = ?");
			// LIMIT ?,10
			pstmt.setString(1, user_name);
			rs = pstmt.executeQuery();

			rs.next();

			s = s + rs.getInt(1) + ",";

			pstmt = conn.prepareStatement("SELECT COUNT(id_respondent) FROM friends WHERE id_respondent = ?");
			// LIMIT ?,10
			pstmt.setString(1, user_name);
			rs = pstmt.executeQuery();

			rs.next();

			s = s + rs.getInt(1);

			// =======================================================================
		} catch (SQLException e) {
			System.out.println("Login 1");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("Login 2");
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (Exception e) {
			}
			try {
				if (conn != null)
					conn.close();
			} catch (Exception e) {
			}
		}

		return s;
	}

	public String delete_board_photo(String photo_name, String contextPath) {
		String r = "-5";
		try {

			boolean isdel = false;
			File file = new File(contextPath + "PlitImage/" + photo_name);

			if (file.exists()) {
				if (file.delete()) {
					isdel = true;
				} else {
					isdel = false;
				}
			} else {
				System.out.println( contextPath + "PlitImage/" + photo_name + "파일이 존재하지 않습니다.");
			}

			if (isdel) {
				pstmt = conn.prepareStatement("DELETE FROM board_photo WHERE photo = ?");
				pstmt.setString(1, photo_name);
				r = pstmt.executeUpdate() + "";
			} else {
				r = "-15";
			}

			// =======================================================================
		} catch (SQLException e) {
			System.out.println("Login 1");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("Login 2");
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (Exception e) {
			}
		}

		return r;
	}


	public int change_board(int board_num, String Content, String tag, double longitude, double latitude, int category, String contextPath, boolean delAll) {
		int r = -1;
		try {

			pstmt = conn.prepareStatement(
					"UPDATE board SET board_content = ?, board_tag = ?, date_board = SYSDATETIME, board_longitude = ?, board_latitude = ?, category_num = ? WHERE board_num = ?");
			pstmt.setString(1, Content);
			pstmt.setString(2, tag);
			pstmt.setDouble(3, longitude);
			pstmt.setDouble(4, latitude);
			pstmt.setInt(5, category);
			pstmt.setInt(6, board_num);

			r = pstmt.executeUpdate();
			
			if( delAll ) // 만약 사용자가 사진을 다 삭제하고 새로 업로드했다면
			{
				pstmt = conn.prepareStatement("SELECT * FROM board_photo WHERE board_num = ?");
				pstmt.setInt(1, board_num);
				rs = pstmt.executeQuery();
				
				while (rs.next()) {
					File file = new File(contextPath + "PlitImage/" + rs.getString(2));

					if (file.exists()) 
					{
						file.delete();
					} 
					else 
					{
						System.out.println( contextPath + "PlitImage/" + rs.getString(2) + "파일이 존재하지 않습니다.");
					}
				}
				
				// board_num 과 관련된 모든 사진 삭제
				pstmt = conn.prepareStatement("DELETE FROM board_photo WHERE board_num = ?");
				pstmt.setInt(1, board_num);
				r = pstmt.executeUpdate();
			}

			
			// =======================================================================
		} catch (SQLException e) {
			System.out.println("Login 1");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("Login 2");
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (Exception e) {
			}
			try {
				if (conn != null)
					conn.close();
			} catch (Exception e) {
			}
		}
		// =========================================================================
		
		r = 1;
		return r;
	}
	public ArrayList<Load_Locate> load_locate(String id) {
		
		ArrayList<Load_Locate> arr = new ArrayList<Load_Locate>();
		Load_Locate ll = null;

		try {

			pstmt = conn
					.prepareStatement("SELECT save_number,latitude,longitude,massage FROM save_locate WHERE id = ?");
			// LIMIT ?,10
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				ll = new Load_Locate();
				
				ll.setSaveNumber(rs.getInt(1));
				ll.setLatitude(rs.getDouble(2));
				ll.setLongitude(rs.getDouble(3));
				ll.setMassage(rs.getString(4));
				
				arr.add(ll);
			}

			// =======================================================================
		} catch (SQLException e) {
			System.out.println("Load_Locate SQL error");
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			System.out.println("Load_Locate Exception");
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (Exception e) {
			}
			try {
				if (conn != null)
					conn.close();
			} catch (Exception e) {
			}
		}

		return arr;
	}

	public String delete_locate(int save_num) {
		System.out.println("DAO Insert Strting");
		String result = "-5";

		try {

			pstmt = conn.prepareStatement("DELETE FROM save_locate WHERE save_number = ?");
			pstmt.setInt(1, save_num);

			int i = pstmt.executeUpdate();

			result = i + "";

			// =======================================================================
		} catch (SQLException e) {
			System.out.println("Login 1");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("Login 2");
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (Exception e) {
			}
			try {
				if (conn != null)
					conn.close();
			} catch (Exception e) {
			}
		}
		return result;
	}

	public String save_locate(String id, double lat, double lon, String massage) {
		System.out.println("DAO Insert Strting");
		String result = "-5";

		try {

			pstmt = conn.prepareStatement("INSERT INTO save_locate(id,latitude,longitude,massage) VALUES (?,?,?,?)");
			pstmt.setString(1, id);
			pstmt.setDouble(2, lat);
			pstmt.setDouble(3, lon);
			pstmt.setString(4, massage);

			int i = pstmt.executeUpdate();

			result = i + "";

			// =======================================================================
		} catch (SQLException e) {
			System.out.println("Login 1");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("Login 2");
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (Exception e) {
			}
			try {
				if (conn != null)
					conn.close();
			} catch (Exception e) {
			}
		}
		return result;
	}

	public String read_board_camera(double lat, double lon) {
		JSONObject result = new JSONObject();
		int count = 0;

		try {

			pstmt = conn.prepareStatement(
					"SELECT a.board_num, a.board_content, a.date_board, a.good, a.board_latitude,a.board_longitude,a.id,b.user_photo FROM board a, user_info b WHERE a.id = b.id and a.board_latitude = ?  AND a.board_longitude = ? ORDER BY a.date_board DESC");
			// LIMIT ?,10
			pstmt.setDouble(1, lat);
			pstmt.setDouble(2, lon);
			rs = pstmt.executeQuery();

			JSONArray jrr = new JSONArray();

			while (rs.next()) {
				JSONObject jtmp = new JSONObject();

				jtmp.put("board_num", rs.getInt(1));
				jtmp.put("content", rs.getString(2));
				jtmp.put("date_board", rs.getString(3));
				jtmp.put("good", rs.getInt(4));
				jtmp.put("board_latitude", rs.getDouble(5));
				jtmp.put("board_longitude", rs.getDouble(6));
				jtmp.put("user_id", rs.getString(7));
				if (rs.getString(8).equals("No Photo")) {
					jtmp.put("user_photo", rs.getString(8));
				} else {
					jtmp.put("user_photo", path + "PlitImage/" + rs.getString(8));
				}

				jrr.put(jtmp);
				count++;

			}

			result.put("read_board_list_camera", jrr);

			// =======================================================================
		} catch (SQLException e) {
			System.out.println("Login 1");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("Login 2");
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (Exception e) {
			}
			try {
				if (conn != null)
					conn.close();
			} catch (Exception e) {
			}
		}

		try {
			result.put("read_board_camera_count", count);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result.toString();
	}

	public int check_good(int board_num, String user_name) {
		System.out.println("DAO Insert Strting");
		String result = "-5";
		int i = 0;
		ResultSet cg_rs = null;
		try {

			pstmt = conn.prepareStatement("SELECT * FROM board_good WHERE board_num = ? AND id = ?");
			pstmt.setInt(1, board_num);
			pstmt.setString(2, user_name);

			cg_rs = pstmt.executeQuery();

			if (cg_rs.next()) {
				i = 1;
			}

			// =======================================================================
		} catch (SQLException e) {
			System.out.println("Login 1");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("Login 2");
			e.printStackTrace();
		} finally {
			try {
				if (cg_rs != null)
					cg_rs.close();
			} catch (Exception e) {
			}
		}
		return i;
	}

	public String plus_good(int board_num, String user_name) {
		System.out.println("DAO Insert Strting");
		String result = "-5";

		try {

			if (check_good(board_num, user_name) == 0) {

				pstmt = conn.prepareStatement("INSERT INTO board_good VALUES (?,?)");
				pstmt.setInt(1, board_num);
				pstmt.setString(2, user_name);

				int i = pstmt.executeUpdate();

				pstmt = conn.prepareStatement("UPDATE board SET good = good+1 WHERE board_num = ?");
				pstmt.setInt(1, board_num);

				i = pstmt.executeUpdate();

				result = 1 + "";
			} else {
				pstmt = conn.prepareStatement("DELETE FROM board_good WHERE board_num = ? AND id = ?");
				pstmt.setInt(1, board_num);
				pstmt.setString(2, user_name);

				int i = pstmt.executeUpdate();

				pstmt = conn.prepareStatement("UPDATE board SET good = good-1 WHERE board_num = ?");
				pstmt.setInt(1, board_num);

				i = pstmt.executeUpdate();

				result = 0 + "";
			}

			// =======================================================================
		} catch (SQLException e) {
			System.out.println("Login 1");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("Login 2");
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (Exception e) {
			}
			try {
				if (conn != null)
					conn.close();
			} catch (Exception e) {
			}
		}
		return result;
	}

	public int membership(User_Info user) {
		System.out.println("DAO Insert Strting");
		int result = -5;

		try {

			pstmt = conn.prepareStatement("INSERT INTO user_info VALUES (?,?,?,SYSDATETIME,'No Photo','No Message',?,0,0)");
			pstmt.setString(1, user.getId());
			pstmt.setString(2, user.getPassword());
			pstmt.setString(3, "0");
			pstmt.setString(4, user.getId());

			int i = pstmt.executeUpdate();

			result = i;

			// =======================================================================
		} catch (SQLException e) {
			System.out.println("Join SQL error");
			e.printStackTrace();
			return result;
		} catch (Exception e) {
			System.out.println("Join Exception");
			e.printStackTrace();
			return result;
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (Exception e) {
			}
			try {
				if (conn != null)
					conn.close();
			} catch (Exception e) {
			}
		}
		return result;
	}

	public Read_Mypage read_myPage(String user_name) {

		Read_Mypage read_board = new Read_Mypage();

		try {

			pstmt = conn.prepareStatement("SELECT * FROM user_info WHERE id=?");
			// LIMIT ?,10
			pstmt.setString(1, user_name);
			rs = pstmt.executeQuery();

			rs.next();

			read_board.setUserId(rs.getString(1));

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
			} catch (Exception e) {
			}
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (Exception e) {
			}
			try {
				if (conn != null)
					conn.close();
			} catch (Exception e) {
			}
		}

		return read_board;
	}

	public ArrayList<Read_Myboard> read_myboard(String user_name, int limit) {
		
		ArrayList<Read_Myboard> arr = new ArrayList<Read_Myboard>();
		Read_Myboard read_myboard = null;

		try {

			pstmt = conn.prepareStatement(
					"SELECT a.board_num, a.board_content, a.date_board, a.good, a.board_latitude,a.board_longitude,a.id,b.user_photo,a.category_num,a.comment_cnt FROM board a, user_info b WHERE a.id = b.id AND a.id = ? ORDER BY a.date_board DESC LIMIT ?,10");
			// LIMIT ?,10
			pstmt.setString(1, user_name);
			pstmt.setInt(2, limit);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				read_myboard = new Read_Myboard();
				
				read_myboard.setBoardNum(rs.getInt(1));
				read_myboard.setContent(rs.getString(2));
				read_myboard.setDateBoard(rs.getString(3));
				read_myboard.setGood(rs.getInt(4));
				read_myboard.setLatitude(rs.getDouble(5));
				read_myboard.setLongitude(rs.getDouble(6));
				read_myboard.setUserId(rs.getString(7));

				if (rs.getString(8).equals("No Photo")) {
					read_myboard.setUserPhoto(rs.getString(8));
				} else {
					read_myboard.setUserPhoto(path + "PlitImage/" + rs.getString(8));
				}
				read_myboard.setCategory(rs.getInt(9));
				read_myboard.setCommentCnt(rs.getInt(10));
				
				arr.add(read_myboard);
			}
			// =======================================================================
		} catch (SQLException e) {
			System.out.println("Read_Myboard SQL error");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("Read_Myboard Exception");
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (Exception e) {
			}
			try {
				if (conn != null)
					conn.close();
			} catch (Exception e) {
			}
		}

		return arr;
	}

	public ArrayList<Read_Board_List> read_board_List() {
		ArrayList<Read_Board_List> arr = new ArrayList<>();
		Read_Board_List rbl = null;
		ResultSet photoResult = null;

		try {
			String str = "SELECT a.board_num, a.board_content, a.board_latitude,a.board_longitude,a.id,b.user_photo,b.nickname "
					+ "FROM board a, user_info b " 
					+ "WHERE a.id = b.id " 
					+ "ORDER BY a.date_board DESC";
			pstmt = conn.prepareStatement( str );

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

				pstmt = conn.prepareStatement("SELECT * FROM board_photo WHERE board_num = ?;");
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
				if (conn != null)
					conn.close();
				if (photoResult != null)
					photoResult.close();
			} 
			catch (Exception e) {
			}
		}

		return arr;
	}
	public Read_Board read_board( int boardNum ) {
		Read_Board board = new Read_Board();
		ResultSet photoResult = null;

		try {
			String str = "SELECT a.board_num, a.board_content, a.board_latitude,a.board_longitude,a.id,b.user_photo,b.nickname "
					+ "FROM board a, user_info b " 
					+ "WHERE a.id = b.id AND a.board_num=?";
			pstmt = conn.prepareStatement( str );
			pstmt.setInt(1, boardNum);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				board.setBoardNum(rs.getInt(1));
				board.setContent(rs.getString(2));
				board.setBoardLatitude(rs.getDouble(3));
				board.setBoardLongitude(rs.getDouble(4));
				board.setUserId(rs.getString(5));
				board.storeName = rs.getString(7);

				if (rs.getString(6).equals("No Photo")) {
					board.setUserPhoto(rs.getString(6));
				} else {
					board.setUserPhoto(path + "PlitImage/" + rs.getString(6));
				}

				pstmt = conn.prepareStatement("SELECT * FROM board_photo WHERE board_num = ?;");
				pstmt.setInt(1, board.getBoardNum());
				photoResult = pstmt.executeQuery();
				
				while( photoResult.next() )
				{
					board.photos.add( path + "PlitImage/" + photoResult.getString(2) );
				}
			}

			// =======================================================================
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
				if (photoResult != null)
					photoResult.close();
			} 
			catch (Exception e) {
			}
		}

		return board;
	}

	public JSONArray get_board_photos( int boardNum ) {
		JSONArray jrr = new JSONArray();

		try {
			pstmt = conn.prepareStatement("SELECT photo FROM board_photo WHERE board_num = ?;");
			pstmt.setInt(1, boardNum);
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				
				jrr.put("PlitImage/" + rs.getString(1));
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
				if (conn != null)
					conn.close();
			} 
			catch (Exception e) {
			}
		}

		return jrr;
		
	}
	/*public Read_Board_List read_board_List(double min_lat, double max_lat, double min_lon, double max_lon) {
		Read_Board_List rbl = new Read_Board_List();

		try {

			pstmt = conn.prepareStatement(
					"SELECT a.board_num, a.board_content, a.date_board, a.good, a.board_latitude,a.board_longitude,a.id,b.user_photo,a.category_num,a.comment_cnt FROM board a, user_info b WHERE a.id = b.id and a.board_latitude >= ? AND a.board_latitude <= ? AND a.board_longitude >= ? AND a.board_longitude <= ? ORDER BY a.date_board DESC");
			// LIMIT ?,10
			pstmt.setDouble(1, min_lat);
			pstmt.setDouble(2, max_lat);
			pstmt.setDouble(3, min_lon);
			;
			pstmt.setDouble(4, max_lon);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				rbl.setBoardNum(rs.getInt(1));
				rbl.setContent(rs.getString(2));
				rbl.setDateBoard(rs.getString(3));
				rbl.setGood(rs.getInt(4));
				rbl.setBoardLatitude(rs.getDouble(5));
				rbl.setBoardLongitude(rs.getDouble(6));
				rbl.setUserId(rs.getString(7));

				if (rs.getString(8).equals("No Photo")) {
					rbl.setUserPhoto(rs.getString(8));
				} else {
					rbl.setUserPhoto(path + "PlitImage/" + rs.getString(8));
				}
				rbl.setCategory(rs.getInt(9));
				rbl.setCommentCnt(rs.getInt(10));

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
			} catch (Exception e) {
			}
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (Exception e) {
			}
			try {
				if (conn != null)
					conn.close();
			} catch (Exception e) {
			}
		}

		return rbl;
	}

	public Read_Board_List read_board_List(double min_lat, double max_lat, double min_lon, double max_lon, int limit) {
		Read_Board_List rbl = new Read_Board_List();

		try {

			pstmt = conn.prepareStatement(
					"SELECT a.board_num, a.board_content, a.date_board, a.good, a.board_latitude,a.board_longitude,a.id,b.user_photo,a.category_num,a.comment_cnt FROM board a, user_info b WHERE a.id = b.id and a.board_latitude >= ? AND a.board_latitude <= ? AND a.board_longitude >= ? AND a.board_longitude <= ? ORDER BY a.date_board DESC LIMIT ?,10");
			// LIMIT ?,10
			pstmt.setDouble(1, min_lat);
			pstmt.setDouble(2, max_lat);
			pstmt.setDouble(3, min_lon);
			;
			pstmt.setDouble(4, max_lon);
			pstmt.setInt(5, limit);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				rbl.setBoardNum(rs.getInt(1));
				rbl.setContent(rs.getString(2));
				rbl.setDateBoard(rs.getString(3));
				rbl.setGood(rs.getInt(4));
				rbl.setBoardLatitude(rs.getDouble(5));
				rbl.setBoardLongitude(rs.getDouble(6));
				rbl.setUserId(rs.getString(7));

				if (rs.getString(8).equals("No Photo")) {
					rbl.setUserPhoto(rs.getString(8));
				} else {
					rbl.setUserPhoto(path + "PlitImage/" + rs.getString(8));
				}
				rbl.setCategory(rs.getInt(9));
				rbl.setCommentCnt(rs.getInt(10));
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
			} catch (Exception e) {
			}
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (Exception e) {
			}
			try {
				if (conn != null)
					conn.close();
			} catch (Exception e) {
			}
		}

		return rbl;
	}*/

	public Read_Board_Location read_board_location(double min_lat, double max_lat, double min_lon, double max_lon) {
		Read_Board_Location rb_location = new Read_Board_Location();

		try {

			pstmt = conn.prepareStatement(
					"SELECT board_latitude,board_longitude,COUNT(*) FROM board WHERE board_latitude >=? AND board_latitude <= ? AND board_longitude >= ? AND board_longitude <= ? GROUP BY board_latitude,board_longitude ORDER BY board_latitude,board_longitude");
			pstmt.setDouble(1, min_lat);
			pstmt.setDouble(2, max_lat);
			pstmt.setDouble(3, min_lon);
			;
			pstmt.setDouble(4, max_lon);
			rs = pstmt.executeQuery();

			ResultSet res = null;
			ResultSet tres = null;

			while (rs.next()) {
				pstmt = conn.prepareStatement(
						"SELECT a.board_num, a.board_content, a.id,b.user_photo FROM board a, user_info b WHERE a.id = b.id and a.board_latitude = ?  AND a.board_longitude = ? ORDER BY a.date_board DESC");
				pstmt.setDouble(1, rs.getDouble(1));
				pstmt.setDouble(2, rs.getDouble(2));
				res = pstmt.executeQuery();

				while (res.next()) {
					rb_location.setBoardNum(res.getInt(1));
					rb_location.setContent(res.getString(2));
					rb_location.setId(res.getString(3));

					if (res.getString(4).equals("No Photo")) {
						rb_location.setUserPhoto(res.getString(4));
					} else {
						rb_location.setUserPhoto(path + "PlitImage/" + res.getString(4));
					}

					pstmt = conn.prepareStatement(
							"SELECT COUNT(*),photo FROM board_photo WHERE board_num = ? GROUP BY board_num");
					pstmt.setInt(1, res.getInt(1));
					tres = pstmt.executeQuery();

					if (tres.next()) {

						rb_location.setPhotoCount(tres.getInt(1));
						rb_location.setBoardPhoto(path + "PlitImage/" + tres.getString(2));
					} else {
						rb_location.setIsPhoto(0);
					}

				}
				rb_location.setBoardLatitude(rs.getDouble(1));
				rb_location.setBoardLongitude(rs.getDouble(2));
				rb_location.setBoardCount(rs.getInt(3));

			}

			// =======================================================================
		} catch (SQLException e) {
			System.out.println("Read_Board_Location SQL error");
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			System.out.println("Read_Board_Location Exception");
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (Exception e) {
			}
			try {
				if (conn != null)
					conn.close();
			} catch (Exception e) {
			}
		}

		return rb_location;
	}

	public String add_frirends(String id_applicant, String id_respondent) {
		System.out.println("DAO Insert Strting");
		String result = "-5";
		int i;

		try {

			pstmt = conn.prepareStatement("SELECT * FROM friends WHERE id_applicant = ? AND id_respondent = ?");
			pstmt.setString(1, id_applicant);
			pstmt.setString(2, id_respondent);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				result = delete_friend(id_applicant, id_respondent);
			} else {

				pstmt = conn.prepareStatement("INSERT INTO friends VALUES (?,?)");
				pstmt.setString(1, id_applicant);
				pstmt.setString(2, id_respondent);

				i = pstmt.executeUpdate();

				result = i + "";

			}

			// =======================================================================
		} catch (SQLException e) {
			System.out.println("Login 1");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("Login 2");
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (Exception e) {
			}
			try {
				if (conn != null)
					conn.close();
			} catch (Exception e) {
			}
		}
		return result;
	}

	public ArrayList<Read_Friends> read_friends(String user_name) {
		
		ArrayList<Read_Friends> arr = new ArrayList<Read_Friends>();
		Read_Friends rf = null;

		try {

			pstmt = conn.prepareStatement(
					"SELECT a.id_respondent, b.user_photo, b.massage FROM friends a, user_info b WHERE a.id_respondent = b.id AND a.id_applicant = ?");
			pstmt.setString(1, user_name);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				
				rf = new Read_Friends();
				
				rf.setUserId(rs.getString(1));

				if (rs.getString(2).equals("No Photo")) {
					rf.setUserPhoto(rs.getString(2));
				} else {
					rf.setUserPhoto(path + "PlitImage/" + rs.getString(2));
				}

				rf.setMessage(rs.getString(3));
				
				arr.add(rf);
			}

			// =======================================================================
		} catch (SQLException e) {
			System.out.println("Read_Friends SQL error");
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			System.out.println("Read_Friends Exception");
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (Exception e) {
			}
			try {
				if (conn != null)
					conn.close();
			} catch (Exception e) {
			}
		}

		return arr;
	}

	public Read_Board_Info read_board_info(int board_num, String user_name) {
			
		Read_Board_Info rbi = null;

		String r = "-1";

		try {

			pstmt = conn.prepareStatement(
					"SELECT a.board_num, a.board_content, a.date_board, a.good, a.hits, a.board_latitude,a.board_longitude,a.id,b.user_photo "
					+ "FROM board a, user_info b "
					+ "WHERE a.id = b.id AND a.board_num = ?");
			pstmt.setInt(1, board_num);
			rs = pstmt.executeQuery();

			rs.next();

			rbi.setBoardNum(rs.getInt(1));
			rbi.setContent(rs.getString(2));
			rbi.setDate(rs.getString(3));
			rbi.setGood(rs.getInt(4));
			rbi.setHits(rs.getInt(5));
			rbi.setLatitude(rs.getDouble(6));
			rbi.setLongitude(rs.getDouble(7));
			rbi.setUserId(rs.getString(8));

			if (rs.getString(9).equals("No Photo")) {
				rbi.setUserPhoto(rs.getString(9));
			} else {
				rbi.setUserPhoto(path + "PlitImage/" + rs.getString(9));
			}
			if (check_good(board_num, user_name) == 0) {
				rbi.setIs_Good(1);
			} else {
				rbi.setIs_Good(0);
			}

			pstmt = conn.prepareStatement("SELECT * FROM board_photo WHERE board_num = ?;");
			pstmt.setInt(1, rbi.getBoardNum());
			rs = pstmt.executeQuery();

			while (rs.next()) {
				rbi.setBoardPhoto(path + "PlitImage/" + rs.getString(2));
			}

			// =======================================================================
		} catch (SQLException e) {
			System.out.println("RBI SQL error");
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			System.out.println("RBI Exception");
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (Exception e) {
			}
			try {
				if (conn != null)
					conn.close();
			} catch (Exception e) {
			}
		}

		// =======================================================================

		return rbi;
	}

	public int write_board_phto(String board_num, ArrayList<String> photo_names) {

		System.out.println("DAO PHOTO Strting");
		int result = -1;
		int j;
		
		try {
			
			for(int i=0;i<photo_names.size();i++) {
				pstmt = conn.prepareStatement("INSERT INTO board_photo VALUES (?,?)");
				pstmt.setInt(1, Integer.parseInt(board_num));
				pstmt.setString(2, photo_names.get(i));
				j = pstmt.executeUpdate(); //사진 전체경로 보기
				
				if(j<=0) {
					return result;
				}
			}
			
			result = 1;
			// =======================================================================
		} catch (SQLException e) {
			System.out.println("write_board_phto SQL error");
			e.printStackTrace();
			return result;
		} catch (Exception e) {
			System.out.println("write_board_phto Exception");
			e.printStackTrace();
			return result;
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (Exception e) {
			}
			try {
				if (conn != null)
					conn.close();
			} catch (Exception e) {
			}
		}
		// =========================================================================

		System.out.println("DAO PHOTO COMPLETE");
		return result;
	}

	public String write_board(String Content, String user_name, String tag, double latitude, double longitude,
			int category) {
		String board_num = "-1";

		try {
			double board_latitude = latitude;
			double board_longitude = longitude;

			pstmt = conn.prepareStatement(
					"SELECT board_latitude,board_longitude FROM board WHERE board_latitude >= ? - 0.0001 AND  board_latitude <= ? + 0.0001 AND board_longitude >= ? - 0.0001 AND board_longitude <= ? + 0.0001 LIMIT 1");
			pstmt.setDouble(1, latitude);
			pstmt.setDouble(2, latitude);
			pstmt.setDouble(3, longitude);
			pstmt.setDouble(4, longitude);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				board_latitude = rs.getDouble(1);
				board_longitude = rs.getDouble(2);
			}

			rs.close();
			pstmt.close();
			rs = null;
			pstmt = null;

			pstmt = conn.prepareStatement(
					"INSERT INTO board(board_content,date_board,good,hits,board_tag,board_latitude,board_longitude,ID,category_num,comment_cnt) VALUES (?,SYSDATETIME,0,0,?,?,?,?,?,0)");
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
			if (rs.next()) {
				board_num = rs.getString(1);
			}

			// =======================================================================
		} catch (SQLException e) {
			System.out.println("Login 1");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("Login 2");
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (Exception e) {
			}
			try {
				if (conn != null)
					conn.close();
			} catch (Exception e) {
			}
		}
		// =========================================================================

		return board_num;
	}

	public int check_Id(String ID) {

		System.out.println("DAO Login Strting");
		int result = -5;

		try {

			pstmt = conn.prepareStatement("SELECT ID FROM user_info WHERE ID = ?");
			pstmt.setString(1, ID);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				result = 0;
			} else {
				result = 1;
			}

			// =======================================================================
		} catch (SQLException e) {
			System.out.println("Check Id SQL error");
			e.printStackTrace();
			return result;
		} catch (Exception e) {
			System.out.println("Check Id Exception");
			e.printStackTrace();
			return result;
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (Exception e) {
			}
			try {
				if (conn != null)
					conn.close();
			} catch (Exception e) {
			}
		}
//=========================================================================   	

		System.out.println("DAO LOGIN COMPLETE");
		return result;

	}

	public String Login(String ID, String PW) {

		System.out.println("DAO Login Strting");
		String result = "-5";

		try {

			pstmt = conn.prepareStatement("select * from user_info where id = ? and passwd = ?");
			pstmt.setString(1, ID);
			pstmt.setString(2, PW);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				result = "1";
			} else {
				result = "0";
			}

			// =======================================================================
		} catch (SQLException e) {
			System.out.println("Login 1");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("Login 2");
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (Exception e) {
			}
			try {
				if (conn != null)
					conn.close();
			} catch (Exception e) {
			}
		}
		// =========================================================================

		System.out.println("DAO LOGIN COMPLETE");
		return result;

	}

	public String get_nickname(String id) {

		System.out.println("DAO Login Strting");
		String result = id;

		try {

			pstmt = conn.prepareStatement("select nickname from user_info where id = ?");
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				result = rs.getString(1);
			} else {
				result = id;
			}

			// =======================================================================
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (Exception e) {
			}
		}
		return result;

	}

    public int write_Comment(int board_num, String userId, String content, String pw, String nickname, int guestPhoto, String contentPhoto)
    {
    	    	System.out.println("DAO Insert Strting");
    	    	int result = -1;
    	    	String user_nickname = nickname;
    	    
    	    	try { 
    	    		pstmt = conn.prepareStatement("INSERT INTO comment("
    	    				+ "board_num,"
    	    				+ "comment_date,"
    	    				+ "comment_content,"
    	    				+ "comment_ID,"
    	    				+ "comment_photo,"
    	    				+ "comment_pw,"
    	    				+ "comment_nickname,"
    	    				+ "user_photo) VALUES(?,SYSDATETIME,?,?,?,?,?,?)");
    	    		pstmt.setInt(1, board_num);
    	    		pstmt.setString(2, content);
    	    		pstmt.setString(3, userId);
    	    		pstmt.setString(4, contentPhoto);
    	    		pstmt.setString(5, pw);
    	    		pstmt.setString(6, user_nickname);
    	    		pstmt.setInt(7, guestPhoto);
    	    		
    	    		int i = pstmt.executeUpdate();
    	    		
    	    		pstmt = conn.prepareStatement("SELECT comment_num FROM comment WHERE board_num = ? ORDER BY comment_num DESC LIMIT 1");
    	    		pstmt.setInt(1, board_num);
    	    		
    	    		
    	    		rs = pstmt.executeQuery();
    	    		
    	    		if(rs.next())
    	    		{
    	    			result = rs.getInt(1); // 코멘트 num 반환
    	    		}   	    		
    	    	   	    		
    	        	    		
    	    		pstmt = conn.prepareStatement("UPDATE board SET comment_cnt = comment_cnt + 1 WHERE board_num = ?");
    	    		pstmt.setInt(1, board_num);
    	    		
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
    public int modify_Comment(int board_num, int commentNum, String content, String comment_photo, boolean isDel, String contextPath )
    {
    	    	int result = -1;
    	    
    	    	try { 

    	    		if( isDel )
    	    		{
    					pstmt = conn.prepareStatement("SELECT comment_photo FROM comment WHERE board_num = ? And comment_num = ?");
        	    		pstmt.setInt(1, board_num);
        	    		pstmt.setInt(2, commentNum);
    					rs = pstmt.executeQuery();
    					
    					if(rs.next()) {
    						String dir = contextPath + "PlitImage/" + rs.getString(1);
    						File file = new File(contextPath + "PlitImage/" + rs.getString(1));

    						if (file.exists()) 
    						{
    							file.delete();
    						} 
    						else 
    						{
    							System.out.println( dir + "파일이 존재하지 않습니다.");
    						}
    					}
    					result = 1;
    	    		}
    	    		
    	    		if( isDel ) // 이전 이미지 삭제버튼을 눌렀냐
    	    		{
    	    			pstmt = conn.prepareStatement("UPDATE comment SET comment_date = SYSDATETIME, comment_content = ?, comment_photo = ? WHERE board_num = ? AND comment_num = ?");
    	    			
        	    		pstmt.setString(1, content);
        	    		pstmt.setString(2, comment_photo);
        	    		pstmt.setInt(3, board_num);
        	    		pstmt.setInt(4, commentNum);
        	    		
        	    		result = 1;
    	    		}
    	    		else // 삭제버튼 안누름
    	    		{
    	    			pstmt = conn.prepareStatement("UPDATE comment "
        	    				+ "SET comment_date = SYSDATETIME, "
        	    				+ "comment_content = ? "
        	    				+ "WHERE "
        	    				+ "board_num = ? AND comment_num = ?");
    	    			
        	    		pstmt.setString(1, content);
        	    		pstmt.setInt(2, board_num);
        	    		pstmt.setInt(3, commentNum);
        	    		
        	    		 result = 1;
    	    		}
    	    		
    	    		int i = pstmt.executeUpdate();
    	    		
    	   //=======================================================================
    	    	} catch ( SQLException e ) {
    	    		e.printStackTrace();
    	    	} catch ( Exception e ) {
    	    		e.printStackTrace();
    	    	} finally {
    	    		try{	  
    	    			if ( rs != null ) rs.close();
    	    			if ( pstmt != null ) pstmt.close();
    	    			if ( conn != null ) conn.close();
    	    		}catch( Exception e ) {}
    	    	}
    	return result;
    }

    public ArrayList<Comment> read_comment(int board_num)
    {
    	Comment c = null;
    	ArrayList<Comment> arr = null;
    	
    	try {                                                                                                                                                                                 

    		pstmt = conn.prepareStatement("SELECT a.board_num, a.comment_num, a.comment_date, a.comment_content, a.comment_id, b.user_photo , a.comment_photo, a.comment_pw, a.comment_nickname, a.user_photo FROM comment a , user_info b WHERE a.comment_id = b.id AND a.board_num = ?");
    		pstmt.setInt(1, board_num);
    		rs = pstmt.executeQuery();
    		
    		arr = new ArrayList<Comment>();
    		
    		while(rs.next())
    		{
    	    	c = new Comment();
    			
    			c.board_num = rs.getInt(1);
    			c.comment_num = rs.getInt(2);
    			c.comment_date = rs.getString(3);
    			c.comment_content = rs.getString(4);
    			c.comment_id = rs.getString(5);
    			if(rs.getString(6).equals("No Photo"))
    			{
    				c.user_photo = rs.getString(6);
    			}
    			else
    			{
    				c.user_photo = path+"PlitImage/"+rs.getString(6);
    			}
    			if(rs.getString(7).equals("No Photo"))
    			{
    				c.comment_photo = rs.getString(7);
    			}
    			else
    			{
    				c.comment_photo = path+"PlitImage/"+rs.getString(7);
    			}
    			
    			c.comment_pw = rs.getString(8);
    			c.comment_nickname = rs.getString(9);
    			c.guest_photo = rs.getInt(10);
    			arr.add(c);
    			
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
    	
    	return arr;
    }
    
    public ArrayList<Comment> read_comment_from_one_user(String id)
    {
    	Comment c = null;
    	ArrayList<Comment> arr = null;
    	ArrayList<Integer> boardNums = new ArrayList<Integer>();
    	
    	try {                               
    		String str = "SELECT board_num FROM board WHERE id = ?";
    		pstmt = conn.prepareStatement(str);
    		pstmt.setString(1, id);
    		rs = pstmt.executeQuery();

    		while(rs.next())
    		{
    			boardNums.add(rs.getInt(1));
    		}

    		arr = new ArrayList<Comment>();
    		
    		for( int i=0; i<boardNums.size(); i++)
    		{
        		pstmt = conn.prepareStatement("SELECT a.board_num, a.comment_num, a.comment_date, a.comment_content, a.comment_id, b.user_photo , a.comment_photo, a.comment_pw, a.comment_nickname, a.user_photo FROM comment a , user_info b WHERE a.comment_id = b.id AND a.board_num = ?");
        		pstmt.setInt(1, boardNums.get(i) );
        		rs = pstmt.executeQuery();
        		
        		while(rs.next())
        		{
        	    	c = new Comment();
        			
        			c.board_num = rs.getInt(1);
        			c.comment_num = rs.getInt(2);
        			c.comment_date = rs.getString(3);
        			c.comment_content = rs.getString(4);
        			c.comment_id = rs.getString(5);
        			if(rs.getString(6).equals("No Photo"))
        			{
        				c.user_photo = rs.getString(6);
        			}
        			else
        			{
        				c.user_photo = path+"PlitImage/"+rs.getString(6);
        			}
        			if(rs.getString(7).equals("No Photo"))
        			{
        				c.comment_photo = rs.getString(7);
        			}
        			else
        			{
        				c.comment_photo = path+"PlitImage/"+rs.getString(7);
        			}
        			
        			c.comment_pw = rs.getString(8);
        			c.comment_nickname = rs.getString(9);
        			c.guest_photo = rs.getInt(10);
        			arr.add(c);
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
    	
    	return arr;
    }
    
    public Comment read_comment_once(int board_num, int commentNum)
    {
    	Comment c = null;
    	
    	try {                                                                                                                                                                                 

    		pstmt = conn.prepareStatement("SELECT a.board_num, a.comment_num, a.comment_date, a.comment_content, a.comment_id, b.user_photo , a.comment_photo, a.comment_pw, a.comment_nickname, a.user_photo FROM comment a , user_info b WHERE a.comment_id = b.id AND a.board_num = ? AND a.comment_num = ?");
    		pstmt.setInt(1, board_num);
    		pstmt.setInt(2, commentNum);
    		rs = pstmt.executeQuery();
    		
    		if(rs.next())
    		{
    	    	c = new Comment();
    			
    			c.board_num = rs.getInt(1);
    			c.comment_num = rs.getInt(2);
    			c.comment_date = rs.getString(3);
    			c.comment_content = rs.getString(4);
    			c.comment_id = rs.getString(5);
    			if(rs.getString(6).equals("No Photo"))
    			{
    				c.user_photo = rs.getString(6);
    			}
    			else
    			{
    				c.user_photo = path+"PlitImage/"+rs.getString(6);
    			}
    			if(rs.getString(7).equals("No Photo"))
    			{
    				c.comment_photo = rs.getString(7);
    			}
    			else
    			{
    				c.comment_photo = path+"PlitImage/"+rs.getString(7);
    			}
    			
    			c.comment_pw = rs.getString(8);
    			c.comment_nickname = rs.getString(9);
    			c.guest_photo = rs.getInt(10);
    			
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
    	
    	return c;
    }

	public String get_user_photo( String id ) {
		String result = "No Photo";

		try 
		{
			pstmt = conn.prepareStatement("SELECT user_photo FROM user_info WHERE id = ?;");
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				result = "PlitImage/" + rs.getString(1);
			}

			// =======================================================================
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} 
			catch (Exception e) {
			}
		}

		return result;
	}
}
