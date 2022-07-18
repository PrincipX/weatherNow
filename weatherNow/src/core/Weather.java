package core;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.stream.Stream;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Weather {
	public Location location;
	public double currentTemp;
	public String sunriseToday;
	public String sunsetToday;
	public double maxTemp;
	public double minTemp;
	public Flag flag;
	
	public Weather(Location location) {
		if(location == null) {
			this.currentTemp = 999;
			this.sunriseToday = null;
			this.sunsetToday = null;
			this.maxTemp = 999;
			this.minTemp = 999;
			this.flag = null;
		} else {
			JSONObject data = getWeather(location.latitude, location.longitude, location.timezone);
			JSONObject other_info = (JSONObject) data.get("daily");
			JSONObject currentWeather = (JSONObject) data.get("current_weather");
			JSONArray sunrise_arr = (JSONArray) other_info.get("sunrise");
			JSONArray sunset_arr = (JSONArray) other_info.get("sunset");
			JSONArray max_temps = (JSONArray) other_info.get("temperature_2m_max");
			JSONArray min_temps = (JSONArray) other_info.get("temperature_2m_min");
			
			this.location = location;
			this.currentTemp = (double) currentWeather.get("temperature");
			this.sunriseToday = dateFormating(sunrise_arr.get(0));
			this.sunsetToday = dateFormating(sunset_arr.get(0));
			this.maxTemp = (double) max_temps.get(0);
			this.minTemp = (double) min_temps.get(0);
			this.flag = new Flag(location.country);
		}
	}
	
	private JSONObject getWeather(double lat, double lon, String timezone) {
		URL url = null;
		try {
			url = new URL("https://api.open-meteo.com/v1/forecast?latitude="+lat+"&longitude="+lon+"&daily=temperature_2m_max,temperature_2m_min,sunrise,sunset&current_weather=true&timezone="+timezone.replaceAll("/", "%2F"));	// URL Encoding
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.connect();
			if (conn.getResponseCode() != 200) {
			    throw new RuntimeException("HttpResponseCode: " + conn.getResponseCode());
			} else {
			  
			    String inline = "";
			    Scanner scanner = new Scanner(url.openStream());
			  
			   //Write all the JSON data into a string using a scanner
			    while (scanner.hasNext()) {
			       inline += scanner.nextLine();
			    }
			    
			    //Close the scanner
			    scanner.close();

			    //Using the JSON simple library parse the string into a json object
			    JSONParser prs = new JSONParser();
			    return (JSONObject) prs.parse(inline);
			}
		} catch(Exception ex) {
			return null;
		}
	}
	
	private String dateFormating(Object date) {
		String date_str = (String) date;
		return date_str.substring(date_str.length()-5);
	}
}
