package org.translation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * Main class for this program.
 * Complete the code according to the "to do" notes.<br/>
 * The system will:<br/>
 * - prompt the user to pick a country name from a list<br/>
 * - prompt the user to pick the language they want it translated to from a list<br/>
 * - output the translation<br/>
 * - at any time, the user can type quit to quit the program<br/>
 */
public class Main {

    /**
     * This is the main entry point of our Translation System!<br/>
     * A class implementing the Translator interface is created and passed into a call to runProgram.
     * @param args not used by the program
     */
    public static void main(String[] args) {
        // Use the JSONTranslator once it's implemented
        Translator translator = new JSONTranslator();
        runProgram(translator);
    }

    /**
     * This is the method which we will use to test your overall program, since
     * it allows us to pass in whatever translator object that we want!
     * See the class Javadoc for a summary of what the program will do.
     * @param translator the Translator implementation to use in the program
     */
    public static void runProgram(Translator translator) {
        while (true) {
            String quit = "quit";

            // Create Scanner object inside the method
            String country = promptForCountry(translator);
            if (quit.equalsIgnoreCase(country)) {
                break;
            }
            String language = promptForLanguage(translator, country);
            if (quit.equalsIgnoreCase(language)) {
                break;
            }

            // Translate the country and language and print the result
            String translation = translator.translate(country, language);
            {
                CountryCodeConverter ccc = new CountryCodeConverter("country-codes.txt");
                LanguageCodeConverter lcc = new LanguageCodeConverter("language-codes.txt");
                System.out.println(ccc.fromCountryCode(country)
                        + " in " + lcc.fromLanguageCode(language)
                        + " is " + translation);
            }

            System.out.println("Press enter to continue or quit to exit.");

            // Create Scanner inside this method for new input
            Scanner s = new Scanner(System.in);
            String textTyped = s.nextLine();

            if (quit.equalsIgnoreCase(textTyped)) {
                break;
            }
        }
    }

    /**
     * Prompts the user to select a country by name from the list of available countries.
     * Converts the country names back to 3-letter country codes for translation.
     * @param  translator the argument that the user gives
     * @return Prompts the user
     */
    private static String promptForCountry(Translator translator) {
        List<String> countries = translator.getCountries();

        CountryCodeConverter converter = new CountryCodeConverter("country-codes.txt");
        List<String> countryNames = new ArrayList<>();
        for (String code : countries) {
            String countryName = converter.fromCountryCode(code);
            if (countryName != null) {
                countryNames.add(countryName);
            }
        }

        Collections.sort(countryNames);
        for (String name : countryNames) {
            System.out.println(name);
        }

        System.out.println("select a country from above:");

        // Create a new Scanner object inside the method
        Scanner scanner = new Scanner(System.in);
        String selectedCountryName = scanner.nextLine();

        if (selectedCountryName.equalsIgnoreCase("quit")) {
            return "quit";
        }

        return converter.fromCountry(selectedCountryName);
    }

    /**
     * Prompts the user to select a language by name from the list of available languages for the selected country.
     * Converts the language names back to 2-letter language codes for translation.
     * @param translator an argument given by the user which is a language chosen by them
     * @param country The country chosen by the user
     * @return returns the converted language name in 2 letter language code
     */
    private static String promptForLanguage(Translator translator, String country) {
        List<String> languages = translator.getCountryLanguages(country);
        LanguageCodeConverter converter = new LanguageCodeConverter("language-codes.txt");
        List<String> languageNames = new ArrayList<>();
        for (String code : languages) {
            String languageName = converter.fromLanguageCode(code);
            if (languageName != null) {
                languageNames.add(languageName);
            }
        }

        Collections.sort(languageNames);

        for (String name : languageNames) {
            System.out.println(name);
        }

        System.out.println("select a language from above:");

        // Create a new Scanner object inside the method
        Scanner scanner = new Scanner(System.in);
        String selectedLanguageName = scanner.nextLine();

        if (selectedLanguageName.equalsIgnoreCase("quit")) {
            return "quit";
        }

        return converter.fromLanguage(selectedLanguageName);
    }
}