package ch.pearcenet.historygen.world;

import ch.pearcenet.historygen.exc.InvalidAlphabetException;
import ch.pearcenet.historygen.language.Alphabet;
import ch.pearcenet.historygen.language.Language;

import java.util.HashMap;

/**
 * Group of people with a common language
 */
public class Nation {

    private static final String NATION_NAME = "nation"; // The word to translate to the name of this nation

    private Language language;

    private HashMap<String, Boolean> coords;

    public Nation(long seed) {
        this.language = new Language(seed);
    }

    public String getName() {
        return language.translateTo(NATION_NAME);
    }

    public Language getLanguage() {
        return language;
    }

    public boolean isCoordInNation(int x, int y) {
        return coords.get(x + "," + y);
    }

    public Nation addCoord(int x, int y) {
        coords.put(x + "," + y, true);
        return this;
    }

}
