package ch.pearcenet.historygen.language;

import ch.pearcenet.historygen.exc.InvalidAlphabetException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

/**
 * Holds all the information about a language
 */
public class Language {

    private static final String ENG_DICT_FILE = "english.txt";

    private static ArrayList<String> englishDictionary;

    private Alphabet alphabet;

    private HashMap<String, String> dictionary;
    private HashMap<String, String> reverseDictionary;

    public Language(String alpha, long seed) throws InvalidAlphabetException {
        // Load english dictionary
        if (englishDictionary == null) {
            englishDictionary = new ArrayList<>();
            try {
                FileInputStream fis = new FileInputStream(ENG_DICT_FILE);
                Scanner in = new Scanner(fis);

                while (in.hasNextLine())
                    englishDictionary.add(in.nextLine());

                in.close();
                fis.close();
            } catch (IOException e) {
                System.err.println("[ERROR] Failed to load English dictionary.");
            }
        }

        Random rand = new Random(seed);
        dictionary = new HashMap<>();
        reverseDictionary = new HashMap<>();

        // Create a word for each english counterpart
        this.alphabet = Alphabet.getAlphabet(alpha);
        for (String engWord: englishDictionary) {
            String newWord = newWord(rand);

            while (dictionary.containsKey(newWord) || reverseDictionary.containsKey(newWord)) {
                newWord = newWord(rand);
            }

            dictionary.put(engWord, newWord);
            reverseDictionary.put(newWord, engWord);
        }

    }

    private String newWord(Random rand) {
        String newWord = "";
        int len = rand.nextInt(9)+2;

        if (len % 2 != 0) {
            len--;
            if (rand.nextInt(1) == 0)
                newWord += alphabet.getRandConsonant(rand.nextLong());
            else
                newWord += alphabet.getRandConsonant(rand.nextLong());
        }

        for (int i=0; i<len; i+=2) {
            newWord += alphabet.getRandConsonant(rand.nextLong());
            newWord += alphabet.getRandVowel(rand.nextLong());
        }

        return newWord;
    }

    public Alphabet getAlphabet() {
        return alphabet;
    }

    public HashMap<String, String> getDictionary() {
        return dictionary;
    }

    /**
     * Translates from English to this language.
     * @param orig English text
     * @return Text in this language
     */
    public String translateTo(String orig) {
        // Remove any unnecessary characters
        String charset = new String(alphabet.getConsonants()) + new String(alphabet.getVowels());
        charset += charset.toUpperCase();
        orig = orig.replaceAll("[^" + charset + "\\s]", "");
        orig = orig.toLowerCase();

        String translation = "";

        String[] words = orig.split("\\s");
        for (String word: words) {
            if (dictionary.containsKey(word)) {
                translation += dictionary.get(word) + " ";
            } else {
                translation += word + " ";
            }
        }
        if (translation.length() > 0) translation = translation.substring(0, translation.length() - 1);

        return translation;
    }

    /**
     * Translates from this language to English.
     * @param orig Text in this language
     * @return English text
     */
    public String translateFrom(String orig) {
        // Remove any unnecessary characters
        orig = orig.replaceAll("[^a-zA-Z\\s]", "");
        orig = orig.toLowerCase();

        String translation = "";

        String[] words = orig.split("\\s");
        for (String word: words) {
            if (reverseDictionary.containsKey(word)) {
                translation += reverseDictionary.get(word) + " ";
            } else {
                translation += word + " ";
            }
        }
        if (translation.length() > 0) translation = translation.substring(0, translation.length() - 1);

        return translation;
    }
}
