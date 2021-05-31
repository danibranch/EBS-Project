package EBS_Proj.EBS_Proj;

import java.time.LocalDate;

public class WeatherModel {
	private int stationId;
	private String city;
	private int temp;
	private double rain;
	private int wind;
	private String direction;
	private LocalDate date;

	public WeatherModel() {
		
	}
	
	public WeatherModel(int stationId, String city, int temp, double rain, int wind, String direction, LocalDate date) {
		this.stationId = stationId;
		this.city = city;
		this.temp = temp;
		this.rain = rain;
		this.wind = wind;
		this.direction = direction;
		this.date = date;
	}
	
	public int getStationId() {
		return this.stationId;
	}
	
	public void setStationId(int stationId) {
		this.stationId = stationId;
	}
	
	public String getCity() {
		return this.city;
	}
	
	public void setCity(String city) {
		this.city = city;
	}
	
	public int getTemp() {
		return this.temp;
	}
	
	public void setTemp(int temp) {
		this.temp = temp;
	}

	public double getRain() {
		return rain;
	}

	public void setRain(double rain) {
		this.rain = rain;
	}

	public int getWind() {
		return wind;
	}

	public void setWind(int wind) {
		this.wind = wind;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}
}
