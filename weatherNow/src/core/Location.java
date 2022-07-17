package core;

import java.net.*;
import java.io.*;
import java.util.*;
import org.json.simple.*;
import org.json.simple.parser.*;


public class Location {
	public String city;
	public String country;
	public double latitude;
	public double longitude;
	
	public Location(String city) {
		this.city = city;
		JSONObject data = getGeoLocation(city.replace(" ", "%20"));	// URL Encoding " "
		if(data == null) {
			this.country = null;
			this.latitude = 999;
			this.longitude = 999;
		} else {
			this.country = (String) data.get("country");
			this.latitude = (double) data.get("latitude");
			this.longitude = (double) data.get("longitude");
		}

	}
	
	private JSONObject getGeoLocation(String city) {
		URL link = null;
		try {
			link = new URL("https://geocoding-api.open-meteo.com/v1/search?name="+city);
			HttpURLConnection conn = (HttpURLConnection) link.openConnection();
			conn.setRequestMethod("GET");
			conn.connect();
			if (conn.getResponseCode() != 200) {
			    throw new RuntimeException("HttpResponseCode: " + conn.getResponseCode());
			} else {
			  
			    String inline = "";
			    Scanner scanner = new Scanner(link.openStream());
			  
			   //Write all the JSON data into a string using a scanner
			    while (scanner.hasNext()) {
			       inline += scanner.nextLine();
			    }
			    
			    //Close the scanner
			    scanner.close();

			    //Using the JSON simple library parse the string into a json object
			    JSONParser prs = new JSONParser();
			    JSONObject tmp_obj = (JSONObject) prs.parse(inline);
			    JSONArray tmp_arr = (JSONArray) tmp_obj.get("results");
			    return (JSONObject) tmp_arr.get(0);
			}
		} catch(Exception ex) {
			return null;
		}
	}

}
