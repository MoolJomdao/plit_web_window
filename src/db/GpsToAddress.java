package db;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class GpsToAddress {
	double latitude;
	double longitude;
	String regionAddress;

	public GpsToAddress(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
		
		try {
			this.regionAddress = getRegionAddress(getJSONData(getApiAddress()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String getApiAddress() {
		String apiURL = "https://maps.googleapis.com/maps/api/geocode/json?latlng="+ latitude +","+ longitude +"&key=AIzaSyB1O3_xjyaGDGbrQ38g-i3kjUpCgjuWEWw&language=ko";
		
		return apiURL;
	}

	private String getJSONData(String apiURL) throws Exception {
		String jsonString = new String();
		String buf;
		URL url = new URL(apiURL);
		URLConnection conn = url.openConnection();
		BufferedReader br = new BufferedReader(new InputStreamReader(
				conn.getInputStream(), "UTF-8"));
		while ((buf = br.readLine()) != null) {
			jsonString += buf;
		}
		return jsonString;
	}

	private String getRegionAddress(String jsonString) {
		JSONObject jObj = (JSONObject) JSONValue.parse(jsonString);
		JSONArray jArray = (JSONArray) jObj.get("results");
		jObj = (JSONObject) jArray.get(0);
		return (String) jObj.get("formatted_address");
	}

	public String getAddress() {
		return regionAddress;
	}
}