package bean;

import java.util.ArrayList;

public class Read_Board_Info {

	private int boardNum;
	private String content;
	private String date;
	private int good;
	private int hits;
	private double latitude;
	private double longitude;
	private String userId;
	private String userPhoto;
	private int is_Good;
	private ArrayList<String> boardPhotos;
	
	public ArrayList<String> getBoardPhoto() {
		return boardPhotos;
	}
	public void setBoardPhoto(String boardPhoto) {
		this.boardPhotos.add( boardPhoto );
	}
	public int getBoardNum() {
		return boardNum;
	}
	public void setBoardNum(int boardNum) {
		this.boardNum = boardNum;
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
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserPhoto() {
		return userPhoto;
	}
	public void setUserPhoto(String userPhoto) {
		this.userPhoto = userPhoto;
	}
	public int getIs_Good() {
		return is_Good;
	}
	public void setIs_Good(int is_Good) {
		this.is_Good = is_Good;
	}
	
	
	
}
