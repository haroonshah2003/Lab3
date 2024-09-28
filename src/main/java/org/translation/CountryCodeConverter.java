package org.translation;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class provides the service of converting country codes to their names.
 */
public class CountryCodeConverter {

    private static final int MIN_PARTS_LENGTH = 4;
    private final Map<String, String> countryToCode;
    private final Map<String, String> codeToCountry;

    /**
     * Default constructor which will load the country codes from "country-codes.txt"
     * in the resources folder.
     */
    public CountryCodeConverter() {
        this("country-codes.txt");
    }

    /**
     * Overloaded constructor which allows us to specify the filename to load the country code data from.
     * @param filename the name of the file in the resources folder to load the data from
     * @throws RuntimeException if the resource file can't be loaded properly
     */
    public CountryCodeConverter(String filename) {
        countryToCode = new HashMap<>();
        codeToCountry = new HashMap<>();

        try {
            List<String> lines = Files.readAllLines(Paths.get(getClass().getClassLoader()
                    .getResource(filename).toURI()));

            for (String currentLine : lines.subList(1, lines.size())) {
                String trimmedLine = currentLine.trim();

                if (trimmedLine.isEmpty()) {
                    continue;
                }

                String[] parts = trimmedLine.split("\\t+");

                if (parts.length < MIN_PARTS_LENGTH) {
                    System.out.println("Warning: Invalid line format - " + trimmedLine);
                    continue;
                }

                String countryName = parts[0].trim();
                String alpha3Code = parts[2].trim();
                countryToCode.put(countryName, alpha3Code);
                codeToCountry.put(alpha3Code.toLowerCase(), countryName);
            }
        }
        catch (IOException | URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Returns the name of the country for the given country code.
     * @param code the 3-letter code of the country
     * @return the name of the country corresponding to the code
     */
    public String fromCountryCode(String code) {
        return codeToCountry.get(code.toLowerCase());
    }

    /**
     * Returns the code of the country for the given country name.
     * @param country the name of the country
     * @return the 3-letter code of the country
     */
    public String fromCountry(String country) {
        return countryToCode.get(country);
    }

    /**
     * Returns how many countries are included in this code converter.
     * @return how many countries are included in this code converter.
     */
    public int getNumCountries() {
        return countryToCode.size();
    }
}
