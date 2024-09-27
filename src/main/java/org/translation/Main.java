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

        // Translator translator = new JSONTranslator(null);
        Translator translator = new InLabByHandTranslator();

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
            String country = promptForCountry(translator);
            if (quit.equals(country)) {
                break;
            }

            String language = promptForLanguage(translator, country);
            if (quit.equals(language)) {
                break;
            }
            System.out.println(country + " in " + language + " is " + translator.translate(country, language));
            System.out.println("Press enter to continue or quit to exit.");
            Scanner s = new Scanner(System.in);
            String textTyped = s.nextLine();

            if (quit.equals(textTyped)) {
                break;
            }
        }
    }

    // Note: CheckStyle is configured so that we don't need javadoc for private methods
    private static String promptForCountry(Translator translator) {
        List<String> countries = translator.getCountries();

        CountryCodeConverter converter = new CountryCodeConverter("country-codes.txt");

        // convert country codes to names
        List<String> names = new ArrayList<>();

        for (String code : countries) {
            // System.out.println("code: " + code);
            String name = converter.fromCountryCode(code);
            // System.out.println("name: " + name);
            names.add(name);
        }
        // System.out.println("names array: " + names);
        Collections.sort(names);

        for (String name : names) {
            System.out.println(name);
        }

        System.out.println("select a country from above:");

        Scanner s = new Scanner(System.in);
        String countryName = s.nextLine();

        return converter.fromCountry(countryName);
    }

    // Note: CheckStyle is configured so that we don't need javadoc for private methods
    private static String promptForLanguage(Translator translator, String country) {
        List<String> languages = translator.getCountryLanguages(country);
        LanguageCodeConverter converter = new LanguageCodeConverter("language-codes.txt");
        List<String> languageNames = new ArrayList<>();

        for (String code : languages) {
            String name = converter.fromLanguageCode(code);
            languageNames.add(name);
        }

        Collections.sort(languageNames);
        for (String name : languageNames) {
            System.out.println(name);
        }

        System.out.println("select a language from above:");

        Scanner s = new Scanner(System.in);
        String languageName = s.nextLine();

        return converter.fromLanguage(languageName);
    }
}
