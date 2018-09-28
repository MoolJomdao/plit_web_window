package bean;

public class Store_Page {
	public String storeName;
	public String stateMessage;
	public double lat;
	public double lon;
	public int commentCount;
	
	public void Store_Page( String name, String meg, double lat, double lon, int cnt )
	{
		this.storeName = name;
		this.stateMessage = meg;
		this.lat = lat;
		this.lon = lon;
		this.commentCount = cnt;
	}
}
