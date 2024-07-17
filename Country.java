package Classes_And_Objects;

public class Country {
	String name;
	String region;
	int population;
	double area;
	double popDensity;
	double coastline;
	double literacy;
	double phones;
	double birthrate;
	double deathrate;
	String literacyLevel;
	int gdp;
	double agriculture;
	
	Country (String[] parts){
		this.name = parts[0].trim();
		this.region = parts[1].trim();
		this.population = parseToInteger(parts[2]);
		this.area = parseToDouble(parts[3]);
		this.popDensity = parseToDouble(parts[4]);
		this.coastline = parseToDouble(parts[5]);
		this.literacy = parseToDouble(parts[9]);
		this.phones = Country.parseToDouble(parts[10]);
		try {
			this.birthrate = parseToDouble(parts[15]);
		} catch (Exception e) {
			this.birthrate = 0;
		}
		try {
			this.deathrate = parseToDouble(parts[16]);
		} catch (Exception e) {
			this.deathrate = 0;
		}
		AdvancedFileProcessing.countries.add(this);
		AdvancedFileProcessing.regions.add(this.region);
		if (this.literacy == 100) {
			AdvancedFileProcessing.maxLiteracy.add(this);
		} else {
			AdvancedFileProcessing.noMaxLiteracy.add(this);
		}
		this.literacyLevel = checkLiteracyLevel(this.literacy);
		this.gdp = parseToInteger(parts[8]);
		try {
			this.agriculture = parseToDouble(parts[17]);
		} catch (Exception e) {
			this.agriculture = 0;
		}
		//System.out.println(this);
	}
	
	static String checkLiteracyLevel (double literacy) {
		String level = "";
		if (literacy < 20) {
			level = "very bad(less than 20%)";
		}
		if (literacy >= 20 && literacy < 40) {
			level = "bad(20% - 39,99%)";
		}
		if (literacy >= 40 && literacy < 60) {
			level = "average(40% - 59,99%)";
		}
		if (literacy >= 60 && literacy < 80) {
			level = "good(60% - 79,99%)";
		}
		if (literacy >= 80) {
			level = "very good(more than 80%)";
		}
		return level;
	}
	
	static Double parseToDouble (String text) {
		double value;
		try {
			value = Double.parseDouble(text.trim().replace(",", "."));
		} catch (Exception e) {
			value = 0;
		}
		return value;
	}
	
	static int parseToInteger (String text) {
		try {
			int value = Integer.parseInt(text.trim());
			return value;
		} catch (Exception e) {
			return 0;
		}
	}
	
	public String toString() {
		return this.name + " is in " + this.region + " with population of " + this.population + " people.";
	}

}
