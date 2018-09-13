package db;

public class BoardBean {

	private int boardNum;
	private String boardContent;
	private String dateBoard;
	private int good;
	private int hits;
	private String boardTag;
	private double latitude; // 위도
	private double longitude;// 경도
	private String id;
	private int category_num;
	private int comment_cnt;
	
	public BoardBean() {
		
	}
	
	public BoardBean(String content, String tag, double latitude, double longitude, String id, int category_num) {
		
		boardContent = content;
		boardTag = tag;
		this.latitude = latitude;
		this.longitude = longitude;
		this.id = id;
		this.category_num = category_num;
		
	}
	
	public int getBoardNum()
	{
		return boardNum;
	}
	public void setBoardNum(int boardNum)
	{
		this.boardNum = boardNum;
	}
	
	public String getBoardContent()
	{
		return boardContent;
	}
	public void setBoardContent(String boardContent)
	{
		this.boardContent = boardContent;
	}
	
	public String getDateBoard()
	{
		return dateBoard;
	}
	public void setDateBoard( String dateBoard )
	{
		this.dateBoard = dateBoard;
	}
	public int getGood()
	{
		return good;
	}
	public void setGood(int good)
	{
		this.good = good;
	}
	
	public int getHits()
	{
		return hits;
	}
	public void setHits(int hits)
	{
		this.hits = hits;
	}
	
	public String getBoardTag()
	{
		return boardTag;
	}
	public void setBoardTag(String boardTag)
	{
		this.boardTag = boardTag;
	}
	
	public double getLatitude()
	{
		return latitude;
	}
	public void setLatitude( double latitude )
	{
		this.latitude = latitude;
	}
	
	public double getLongitude()
	{
		return longitude;
	}
	public void setLongitude( double longitude )
	{
		this.longitude = longitude;
	}
	
	public String getId()
	{
		return id;
	}
	public void setId( String id )
	{
		this.id = id;
	}
	public int getCategory_num() {
		return category_num;
	}
	public void setCategory_num(int category_num) {
		this.category_num = category_num;
	}
	public int getComment_cnt() {
		return comment_cnt;
	}
	public void setComment_cnt(int comment_cnt) {
		this.comment_cnt = comment_cnt;
	}
	
	
}
