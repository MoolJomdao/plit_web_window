package bean;

import java.util.ArrayList;

public class Read_Board {
	
	private int boardNum;
	private String content;
	private double boardLatitude;
	private double boardLongitude;
	private String userId;
	public String storeName;
	private String userPhoto;
	public ArrayList<String> photos = new ArrayList<>();
	
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
	public double getBoardLatitude() {
		return boardLatitude;
	}
	public void setBoardLatitude(double boardLatitude) {
		this.boardLatitude = boardLatitude;
	}
	public double getBoardLongitude() {
		return boardLongitude;
	}
	public void setBoardLongitude(double boardLongitude) {
		this.boardLongitude = boardLongitude;
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
}
