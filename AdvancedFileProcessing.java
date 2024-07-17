package Classes_And_Objects;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public class AdvancedFileProcessing {
	
	static String filePath = "https://github.com/arnolds-helmanis/Homework_File_Processing/blob/main/countryList.csv";
	static String decoding = "UTF-8";
	static String delimiter = ";";
	static ArrayList<Country> countries = new ArrayList<Country>();
	static HashSet<String> regions = new HashSet<String>();
	static HashMap<String, Integer> countriesInRegion = new HashMap<String, Integer>();
	static HashMap<String, ArrayList<Country>> countriesPerRegion = new HashMap<String, ArrayList<Country>>();
	static ArrayList<Country> countriesNoCL = new ArrayList<Country>();
	static ArrayList<Country> maxLiteracy = new ArrayList<Country>();
	static ArrayList<Country> noMaxLiteracy = new ArrayList<Country>();
	static HashMap<String, ArrayList<Country>> countriesPerLiteracyLevel = new HashMap <String, ArrayList<Country>>();
	static HashMap<String, Integer> countriesWithLiteracyAndCL = new HashMap <String, Integer>();
	static ArrayList<Country> countriesWithCL = new ArrayList<Country>();
	static ArrayList<Country> betterThanAvgCountries = new ArrayList<Country>();

	public static void main(String[] args) {
		importCountries();
		calculateData();
		noCoastline();
		negativeBR();
		avgPhoneCount();
		literacyAndCoastline();
		longestCoastline();
		GDPcompare();
		gdpAndAgriculture();

	}
	
	// Calculates the average GDP and agriculture in the world. Then calculates the average GDP and agriculture
	// in 'rich' countries and then compares it to the average scores of the world.
	static void gdpAndAgriculture ()  {
		int allGDP = 0;
		double allAgriculture = 0;
		for (Country country : countries) {
			allGDP += country.gdp;
			allAgriculture += country.agriculture;
		}
		double avgGDP = allGDP / countries.size();
		double avgAgriculture = rounding(allAgriculture / countries.size(), 2);
		double richCountryAgriculture = 0;
		int richGDP = 0;
		for (Country country : countries) {
			if (country.gdp > avgGDP) {
				betterThanAvgCountries.add(country);
				richCountryAgriculture += country.agriculture;
				richGDP += country.gdp;
			}
		}
		double avgRichCountryAgri = rounding(richCountryAgriculture / betterThanAvgCountries.size(), 2);
		double avgRichGDP = richGDP / betterThanAvgCountries.size();
		System.out.println("The average GDP per capita in rich countries(better GDP than the average in the world - " + avgGDP + ") is " + avgRichGDP + ". In the rich countries the average\nagriculture score is " + avgRichCountryAgri + ", which is significantly lower than the average agriculture score in the world - " + avgAgriculture + ".");
		
	}
	
	// Compares the average GDP in countries with a coastline and average GDP in countries without a coastline
	static void GDPcompare() {
		int gdpNoCL = 0;
		int gdpWithCL = 0;
		for (Country country : countries) {
			if (country.coastline > 0) {
				countriesWithCL.add(country);
				gdpWithCL += country.gdp;
			} else {
				gdpNoCL += country.gdp;
			}
		}
		double avgNoCL = gdpNoCL / countriesNoCL.size();
		double avgWithCL = gdpWithCL / countriesWithCL.size();
		boolean check = avgWithCL > avgNoCL;
		if (check) {
			System.out.println("Coastline positively affects the GDP. The average GDP in countries with coastline is " + avgWithCL + " dollars per capita\nand the average GDP in countries without coastline is " + avgNoCL + " dollars per capita.");
		} else {
			System.out.println("Coastline has no positive effect on GDP. In countries without coastline average GDP is " + avgNoCL + " and in countries with coastline - " + avgWithCL);
		}
		System.out.println("");
	}
	
	// Prints out the country with longest coastline and the length of the coastline
	static void longestCoastline() {
		double length = 0;
		String winner = "";
		for (Country country : countries) {
			if (country.area * country.coastline > length) {
				length = country.area * country.coastline;
				winner = country.name;
			}
		}
		System.out.println(winner + " has the longest coastline - " + length + "km.");
		System.out.println("");
	}
	
	static void literacyAndCoastline() {
		// This 'for' cycle iterates through all countries and groups them in a HashMap by their literacy level, which is
		// assigned in the 'Country' constructor when making a 'Country' object. HashMap's 'key' is the literacy level
		// and HashMap's 'value' is an ArrayList with countries in that particular literacy level.
		for (Country country : countries) {
			if(!countriesPerLiteracyLevel.containsKey(country.literacyLevel)) {
				ArrayList<Country> cs = new ArrayList<Country>();
				cs.add(country);
				countriesPerLiteracyLevel.put(country.literacyLevel, cs);
			} else {
				ArrayList<Country> cs = countriesPerLiteracyLevel.get(country.literacyLevel);
				cs.add(country);
				countriesPerLiteracyLevel.put(country.literacyLevel, cs);
			}
		}
		// This 'for' cycle iterates through every 'key' of 'countriesPerLiteracyLevel' HashMap and puts countries 
		// with a coastline, in that particular literacy level, in a different HashMap, where 'key' is country literacy
		// level and 'value' is a number of countries with a coastline in that particular literacy level.
		for (String key : countriesPerLiteracyLevel.keySet()) {
			ArrayList<Country> list = countriesPerLiteracyLevel.get(key);
			for (Country country : list) {
				if(!countriesWithLiteracyAndCL.containsKey(key) && country.coastline > 0) {
					countriesWithLiteracyAndCL.put(key, 1);
				} else if (countriesWithLiteracyAndCL.containsKey(key) && country.coastline > 0) {
					int num = countriesWithLiteracyAndCL.get(key);
					countriesWithLiteracyAndCL.put(key, num + 1);
				} else {
					continue;
				}
			}
		}
		// Calculates and prints out the percentage of countries with a coastline in a particular literacy group
		for (String key : countriesPerLiteracyLevel.keySet()) {
			double countriesInLiteracyLevel = countriesPerLiteracyLevel.get(key).size();
			double countriesWithCoastline = countriesWithLiteracyAndCL.get(key);
			double result = rounding(((countriesWithCoastline / countriesInLiteracyLevel) * 100), 2);
			System.out.println(result + "% of the countries in '" + key + "' literacy group have a coastline.");
		}
		System.out.println("");
	}
	
	static double rounding (double number, int places) {
		double scale = Math.pow(10, places);
		return Math.round(number * scale) / scale;
		
	}
	
	// Calculates average number of phones per 1000 people in the countries with 100% literacy and in  countries
	// with literacy less than 100%
	static void avgPhoneCount () {
		double maxPhones = 0;
		double noMaxPhones = 0;
		for (Country country : maxLiteracy) {
			maxPhones += country.phones;
		}
		for (Country country : noMaxLiteracy) {
			noMaxPhones += country.phones;
		}
		double avgMax = rounding(maxPhones / maxLiteracy.size(), 2);
		double avgNoMax = rounding(noMaxPhones / noMaxLiteracy.size(), 2);
		System.out.println("In coutries with 100% literacy, on average there are " + avgMax + " phones per 1000 citizens.\nBut in the countries with literacy lower than 100%, on average there are " + avgNoMax + " phones per 1000 citizens.");
		System.out.println("");
	}
	
	// Prints out names of all countries with a negative birth rate
	static void negativeBR () {
		System.out.println("These countries have a negative birthrate:");
		for (Country country : countries) {
			if (country.birthrate < country.deathrate) {
				System.out.println(" - " + country.name);
			}
		}
		System.out.println("");
	}
	
	// Puts all country names without a coastline in a ArrayList and then prints them out. 
	static void noCoastline () {
		for (Country country : countries) {
			if (country.coastline == 0) {
				countriesNoCL.add(country);
			}
		}
		System.out.println("These countries have no coastline:");
		for (Country country : countriesNoCL) {
			System.out.println(" - " + country.name);
		}
		System.out.println("");
	}
	
	static void calculateData() {
		/*for (String region : regions) {
			System.out.println(region);
		}*/
		for (Country country : countries) {
			if (!countriesInRegion.containsKey(country.region)) {
				countriesInRegion.put(country.region, 1);
				ArrayList<Country> cs = new ArrayList<Country>();
				cs.add(country);
				countriesPerRegion.put(country.region, cs);
			} else {
				Integer count = countriesInRegion.get(country.region);
				countriesInRegion.put(country.region, count + 1);
				ArrayList<Country> cs = countriesPerRegion.get(country.region);
				cs.add(country);
			}
		}
		for (String region : countriesInRegion.keySet()) {
			System.out.println(region + " has " + countriesInRegion.get(region) + " countries");
		}
		System.out.println("");
		for (String region : regions) {
			System.out.println(region + " has following countries:");
			for (Country country : countries) {
				if (country.region.equals(region)) {
					System.out.println(" - " + country.name);
				}
			}
		}
	}
	
	static void importCountries() {
		try {
			File file = new File(filePath);
			Scanner fileReader = new Scanner (file, decoding);
			fileReader.nextLine();
			while (fileReader.hasNextLine()) {
				String line = fileReader.nextLine();
				String[] parts = line.split(delimiter);
				if (parts.length < 2) continue;
				new Country(parts);
			}
			fileReader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
