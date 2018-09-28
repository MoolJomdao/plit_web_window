package db;

public class UserBean {

	// 아이디
	// 비밀번호
	// 생년월일 ( yyyymmdd )
	private String id;
	private String pw;
    private String birth;
    private String message;
    private String userPhoto;
    
    public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPw() {
		return pw;
	}
	public void setPw(String pw) {
		this.pw = pw;
	}
	public String getBirth() {
		return birth;
	}
	public void setBirth(String birth) {
		this.birth = birth;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage( String message ) {
		this.message = message;
	}
	public String getuserPhoto() {
		return userPhoto;
	}
	public void setuserPhoto( String photo ) {
		this.userPhoto = photo;
	}
}
