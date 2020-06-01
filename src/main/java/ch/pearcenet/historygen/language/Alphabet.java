package ch.pearcenet.historygen.language;

import ch.pearcenet.historygen.exc.InvalidAlphabetException;

import java.util.Random;

public class Alphabet {

    private char[] consonants;

    private char[] vowels;

    public static Alphabet getAlphabet(String name) throws InvalidAlphabetException {
        name = name.toUpperCase();
        Alphabet alpha = new Alphabet();

        switch (name) {

            case "LATIN":
                alpha.setConsonants("bcdfghjklmnpqrstvwxyz".toCharArray());
                alpha.setVowels("aeiou".toCharArray());
                break;

            case "RUNIC":
                alpha.setConsonants(("" +
                        "\u16A0" +
                        "\u16A6" +
                        "\u16B1" +
                        "\u16B2" +
                        "\u16B7" +
                        "\u16B9" +
                        "\u16BA" +
                        "\u16BE" +
                        "\u16C3" +
                        "\u16C8" +
                        "\u16C9" +
                        "\u16CA" +
                        "\u16CF" +
                        "\u16D2" +
                        "\u16D7" +
                        "\u16DA" +
                        "\u16DC" +
                        "\u16DE").toCharArray());
                alpha.setVowels(("" +
                        "\u16A2" +
                        "\u16AB" +
                        "\u16C1" +
                        "\u16C7" +
                        "\u16D6" +
                        "\u16DF").toCharArray());
                break;

            case "GREEK":
                //TODO: Add greek alphabet
                alpha.setConsonants("bcdfghjklmnpqrstvwxyz".toCharArray());
                alpha.setVowels("aeiou".toCharArray());
                break;

            case "CYRILLIC":
                //TODO: Add cyrillic alphabet
                alpha.setConsonants("bcdfghjklmnpqrstvwxyz".toCharArray());
                alpha.setVowels("aeiou".toCharArray());
                break;

            case "ETRUSCAN":
                //TODO: Add etruscan alphabet
                alpha.setConsonants("bcdfghjklmnpqrstvwxyz".toCharArray());
                alpha.setVowels("aeiou".toCharArray());
                break;

            default:
                throw new InvalidAlphabetException("Alphabet '" + alpha + "' is invalid.");
        }

        return alpha;
    }

    public char[] getConsonants() {
        return consonants;
    }

    public Alphabet setConsonants(char[] consonants) {
        this.consonants = consonants;
        return this;
    }

    public char[] getVowels() {
        return vowels;
    }

    public Alphabet setVowels(char[] vowels) {
        this.vowels = vowels;
        return this;
    }

    public char getRandVowel(long seed) {
        Random r = new Random(seed);
        return vowels[r.nextInt(vowels.length)];
    }

    public char getRandConsonant(long seed) {
        Random r = new Random(seed);
        return consonants[r.nextInt(consonants.length)];
    }
}
