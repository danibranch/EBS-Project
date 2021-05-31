package EBS_Proj.EBS_Proj;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WeatherGenerator {
	private Random rand;
	private int maxNrOfDays = 365;
	private List<String> cities = new ArrayList<String>() {{
		add("Iasi");
		add("Bucuresti");
		add("Cluj");
		add("Arad");
		add("Timisoara");
		add("Galati");
		add("Brasov");
	}};
	private List<String> directions = new ArrayList<String>() {{
		add("N");
		add("NE");
		add("E");
		add("SE");
		add("S");
		add("SW");
		add("W");
		add("NW");
	}};
	
	private int generateRandomInt(int min, int max) {
		return min + rand.nextInt(max - min);
	}
	
	private LocalDate generateRandomDate() {
		return LocalDate.now().minusDays(rand.nextInt(this.maxNrOfDays));
	}
	
	private String generateRandomFromArray(List<String> array) {
		return array.get(generateRandomInt(0, array.size()));
	}
	
	public WeatherGenerator() {
		rand = new Random();
	}
	
	public WeatherModel GenerateWeather() {
		return new WeatherModel(
				generateRandomInt(0, 25),
				generateRandomFromArray(this.cities),
				generateRandomInt(-10, 25),
				rand.nextDouble(),
				generateRandomInt(0, 20),
				generateRandomFromArray(directions),
				generateRandomDate()
			);
	}
}
