package org.translation;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * An implementation of the Translator interface which reads in the translation
 * data from a JSON file. The data is read in once each time an instance of this class is constructed.
 */
public class JSONTranslator implements Translator {

    private final List<String> countryCodes;
    private final Map<String, List<String>> countryLanguages;
    private final Map<String, Map<String, String>> translations;

    /**
     * Constructs a JSONTranslator using data from the sample.json resources file.
     */
    public JSONTranslator() {
        this("sample.json");
    }

    /**
     * Constructs a JSONTranslator populated using data from the specified resources file.
     * @param filename the name of the file in resources to load the data from
     * @throws RuntimeException if the resource file can't be loaded properly
     */
    public JSONTranslator(String filename) {
        countryCodes = new ArrayList<>();
        countryLanguages = new HashMap<>();
        translations = new HashMap<>();

        try {
            String jsonString = Files.readString(Paths.get(getClass().getClassLoader().getResource(filename).toURI()));

            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject countryObj = jsonArray.getJSONObject(i);
                String countryCode = countryObj.getString("alpha3");
                countryCodes.add(countryCode);

                List<String> languages = new ArrayList<>();
                Map<String, String> countryTranslations = new HashMap<>();

                for (String key : countryObj.keySet()) {
                    if (!"alpha2".equals(key) && !"alpha3".equals(key) && !"id".equals(key)) {
                        languages.add(key);
                        countryTranslations.put(key, countryObj.getString(key));
                    }
                }

                countryLanguages.put(countryCode, languages);
                translations.put(countryCode, countryTranslations);
            }

        }
        catch (IOException | URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<String> getCountryLanguages(String country) {
        return new ArrayList<>(countryLanguages.getOrDefault(country, new ArrayList<>()));
    }

    @Override
    public List<String> getCountries() {
        return new ArrayList<>(countryCodes);
    }

    @Override
    public String translate(String country, String language) {
        Map<String, String> countryTranslation = translations.get(country.toLowerCase());
        if (countryTranslation != null) {
            return countryTranslation.get(language.toLowerCase());
        }
        return null;
    }
}
