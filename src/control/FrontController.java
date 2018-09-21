package control;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FrontController 
{
	DAO dao = new DAO();
	
	public JSONObject control(JSONObject jobj) throws JSONException
	{
		JSONObject result = new JSONObject();
		JSONObject code = new JSONObject();
		
		switch(jobj.getString("flag"))
		{
		case "Login":
			result.put("result", login(jobj.getJSONObject("Login_data")));
			break; 
		case "write_board":
			result.put("result", write_board(jobj.getJSONObject("Board_data")));
			break;
		case "board_num":
			result.put("result", write_board_photo(jobj));
			break;
		case "ReadBoardInfo":
			result.put("result", readBoardInfo(jobj.getJSONObject("Board_Info_data")));
			break;
		case "write_comment":
			result.put("result",write_comment(jobj.getJSONObject("write_comment_data")));
			break;
		case "read_comment":
			result.put("result", read_comment(jobj.getJSONObject("read_comment_data")));
			break;
		case "read_friends":
			result.put("result", read_friends(jobj.getJSONObject("read_friends_data")));
			break;
		case "add_friends":
			result.put("result", add_friends(jobj.getJSONObject("add_friends_data")));
			break;
		case "read_board_location":
			result.put("result", read_board_location(jobj.getJSONObject("read_board_location_data")));
			break;
		case "read_board_list":
			result.put("result", read_board_list(jobj.getJSONObject("read_board_list_data")));
			break;
		case "read_myboard":
			result.put("result", read_myboard(jobj.getJSONObject("read_myboard_data")));
			break;
		case "read_myPage":
			result.put("result", read_myPage(jobj.getJSONObject("read_myPage_data")));
			break;
		case "check_Id":
			result.put("result", check_Id(jobj.getJSONObject("check_Id_data")));
			break;
		case "membership":
			result.put("result", membership(jobj.getJSONObject("membership_data")));
			break;
		case "plus_good":
			result.put("result", plus_good(jobj.getJSONObject("plus_good_data")));
			break;
		case "read_board_camera":
			result.put("result", read_board_camera(jobj.getJSONObject("read_board_camera_data")));
			break;
		case "save_locate":
			result.put("result", save_locate(jobj.getJSONObject("save_locate_data")));
			break;
		case "delete_locate":
			result.put("result", delete_locate(jobj.getJSONObject("delete_locate_data")));
			break;
		case "load_locate":
			result.put("result", load_locate(jobj.getJSONObject("load_locate_data")));
			break;
		case "change_board":
			result.put("result", change_board(jobj.getJSONObject("change_board_data")));
			break;
		case "delete_board_photo":
			result.put("result", delete_board_photo(jobj.getJSONObject("delete_board_photo_data")));
			break;
		case "count_friends":
			result.put("result", count_friends(jobj.getJSONObject("count_friends_data")));
			break;
		case "delete_board":
			result.put("result", delete_board(jobj.getJSONObject("delete_board_data")));
			break;
		case "write_user_photo":
			result.put("result", write_user_photo(jobj));
			break;
		case "del_user_phto":
			result.put("result", del_user_phto(jobj.getJSONObject("del_user_phto_data")));
			break;
        case "change_massage":
            result.put("result", change_massage(jobj.getJSONObject("change_massage_data")));
            break;
        case "delete_friend":
            result.put("result", delete_friend(jobj.getJSONObject("delete_friend_data")));
            break;
        case "search_board":
            result.put("result", search_board(jobj.getJSONObject("search_board_data")));
            break;
		case "read_board_list2":
			result.put("result", read_board_list2(jobj.getJSONObject("read_board_list_data")));
			break;
		case "delete_comment":
			result.put("result", delete_comment(jobj.getJSONObject("delete_comment_data")));
			break;
		case "change_nickname":
			result.put("result", delete_comment(jobj.getJSONObject("change_nickname_data")));
			break;
			
		}
		
		
		/*code.put("code", -1);
		result.put(code);*/
		
		return result;
	}
	
	
	private String change_nickname(JSONObject jobj) throws JSONException
	{
		return dao.change_nickname(jobj.getString("nickname"), jobj.getString("id"));
	}
	
	private String delete_comment(JSONObject jobj) throws JSONException
	{
		return dao.delete_Comment(jobj.getInt("board_num"), jobj.getInt("comment_num"));
	}
	
	private String read_board_list2(JSONObject jobj) throws JSONException
	{
		return dao.read_board_List(jobj.getDouble("min_lat"), jobj.getDouble("max_lat"), jobj.getDouble("min_lon"),jobj.getDouble("max_lon"));
	}
	
    private String search_board(JSONObject jobj) throws JSONException
    {
            return dao.search_board(jobj.getString("tag"));
    }

    private String delete_friend(JSONObject jobj) throws JSONException
    {
            return dao.delete_friend(jobj.getString("id_applicant"), jobj.getString("id_respondent"));
    }

    private String change_massage(JSONObject jobj) throws JSONException
    {
            return dao.change_massage(jobj.getString("massage"), jobj.getString("id"));
    }

	
	private String del_user_phto(JSONObject jobj) throws JSONException
	{
		return dao.del_user_phto(jobj.getString("user_name"));
	}
	
	private String write_user_photo(JSONObject jobj) throws JSONException
	{
		return dao.write_user_phto(jobj.getString("num"), jobj.getString("name"));
	}
	
	private String delete_board(JSONObject jobj) throws JSONException
	{
		return dao.delete_board(jobj.getInt("board_num"));
	}
	
	private String count_friends(JSONObject jobj) throws JSONException
	{
		return dao.count_friends(jobj.getString("user_name"));
	}
	
	private String delete_board_photo(JSONObject jobj) throws JSONException
	{
		return dao.delete_board_photo(jobj.getString("photo_name"));
	}
	
	private String change_board(JSONObject jobj) throws JSONException
	{
		return dao.change_board( jobj.getInt("board_num"),jobj.getString("content"), jobj.getString("tag"),jobj.getDouble("longitude"),jobj.getDouble("latitude"));
	}
	
	private String load_locate(JSONObject jobj) throws JSONException
	{
		return dao.load_locate(jobj.getString("id"));
	}
	
	private String delete_locate(JSONObject jobj) throws JSONException
	{
		return dao.delete_locate(jobj.getInt("save_num"));
	}
	
	private String save_locate(JSONObject jobj) throws JSONException
	{
		return dao.save_locate(jobj.getString("id"), jobj.getDouble("lat"), jobj.getDouble("lon"), jobj.getString("massage"));
	}
	
	private String read_board_camera(JSONObject jobj) throws JSONException
	{
		return dao.read_board_camera(jobj.getDouble("lat"), jobj.getDouble("lon"));
	}
	
	private String plus_good(JSONObject jobj) throws JSONException
	{
		return dao.plus_good(jobj.getString("board_num"),jobj.getString("user_name"));
	}
	
	private String membership(JSONObject jobj) throws JSONException
	{
		return dao.membership(jobj.getString("user_id"), jobj.getString("passwd"), jobj.getString("birth"));
	}
	
	private String check_Id(JSONObject jobj) throws JSONException
	{
		return dao.check_Id(jobj.getString("ID"));
	}
	
	private String read_myPage(JSONObject jobj) throws JSONException
	{
		return dao.read_myPage(jobj.getString("user_name"));
	}
	
	private String read_myboard(JSONObject jobj) throws JSONException
	{
		return dao.read_myboard(jobj.getString("user_name"), jobj.getInt("limit"));
	}
	
	private String read_board_list(JSONObject jobj) throws JSONException
	{
		return dao.read_board_List(jobj.getDouble("min_lat"), jobj.getDouble("max_lat"), jobj.getDouble("min_lon"),jobj.getDouble("max_lon"),jobj.getInt("limit"));
	}
	
	private String read_board_location(JSONObject jobj) throws JSONException
	{
		return dao.read_board_location(jobj.getDouble("min_lat"), jobj.getDouble("max_lat"), jobj.getDouble("min_lon"),jobj.getDouble("max_lon"));
	}
	
	private String add_friends(JSONObject jobj) throws JSONException
	{
		return dao.add_frirends(jobj.getString("id_applicant"), jobj.getString("id_respondent"));
	}
	
	private String read_friends(JSONObject jobj) throws JSONException
	{
		return dao.read_friends(jobj.getString("user_name"));
	}
	
	private String read_comment(JSONObject jobj) throws JSONException
	{
		
		return dao.read_comment(jobj.getString("Board_num"));
	}
	
	private String write_comment(JSONObject jobj) throws JSONException
	{
		return dao.write_Comment(jobj.getString("Board_num"), 
								jobj.getString("user_name"),
								jobj.getString("content"));
	}
	
	
	private String readBoardInfo(JSONObject jobj) throws JSONException
	{
		String result = "-1";
		
		result =dao.read_board_info(jobj.getString("Board_num"),jobj.getString("user_name"));
		
		
		return result;
	}
	
	
	private String write_board_photo(JSONObject jobj) throws JSONException
	{
		return dao.write_board_phto(jobj.getString("num"), jobj.getString("name"));
	}
	
	private String write_board(JSONObject jobj) throws JSONException
	{
		return dao.write_board(
				jobj.getString("content"), 
				jobj.getString("user_name"), 
				jobj.getString("tag"), 
				jobj.getDouble("latitude"), 
				jobj.getDouble("longitude"),
				jobj.getInt("category")
				);
	}
	
	
	
	private String login(JSONObject jobj) throws JSONException
	{
		return dao.Login(jobj.getString("ID"), jobj.getString("passwd"));	
	}

}
