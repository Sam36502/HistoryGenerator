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

        // Create a word for each english counterpart
        this.alphabet = Alphabet.getAlphabet(alpha);
        for (String engWord: englishDictionary) {
            dictionary.put(engWord, newWord(rand));
        }

    }

    private String newWord(Random rand) {
        String newWord = "";
        int len = rand.nextInt(9)+1;

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

    }
}
