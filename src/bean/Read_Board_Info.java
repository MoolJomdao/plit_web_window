package bean;

public class Read_Board_Info {

	private int board_Num;
	private String content;
	private String date;
	private int good;
	private int hits;
	private double latitude;
	private double longitude;
	private String user_Id;
	private String user_Photo;
	private int is_Good;
	
	public int getBoard_Num() {
		return board_Num;
	}
	public void setBoard_Num(int board_Num) {
		this.board_Num = board_Num;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public int getGood() {
		return good;
	}
	public void setGood(int good) {
		this.good = good;
	}
	public int getHits() {
		return hits;
	}
	public void setHits(int hits) {
		this.hits = hits;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public String getUser_Id() {
		return user_Id;
	}
	public void setUser_Id(String user_Id) {
		this.user_Id = user_Id;
	}
	public String getUser_Photo() {
		return user_Photo;
	}
	public void setUser_Photo(String user_Photo) {
		this.user_Photo = user_Photo;
	}
	
	public int getIs_Good() {
		return is_Good;
	}
	
	public void setIs_Good(int is_good) {
		is_Good = is_good;
	}
	
	
}
