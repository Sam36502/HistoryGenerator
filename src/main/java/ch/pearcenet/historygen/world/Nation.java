package ch.pearcenet.historygen.world;

import ch.pearcenet.historygen.language.Language;
import org.fusesource.jansi.Ansi;

import java.util.Random;

/**
 * Group of people with a common language
 */
public class Nation {

    private static final String NATION_NAME = "nation"; // The word to translate to the name of this nation
    private static final Ansi.Color[] AVAIL_COLOURS = {
            Ansi.Color.RED,
            Ansi.Color.YELLOW,
            Ansi.Color.GREEN,
            Ansi.Color.CYAN,
            Ansi.Color.BLUE,
            Ansi.Color.MAGENTA
    };

    private Language language;

    private Ansi.Color colour;

    public Nation(long seed) {
        Random r = new Random(seed);

        this.language = new Language(seed);
        this.colour = AVAIL_COLOURS[r.nextInt(AVAIL_COLOURS.length)];
    }

    public String getName() {
        return language.translateTo(NATION_NAME);
    }

    public Language getLanguage() {
        return language;
    }

    public String getAnsiDisplay() {
        return Ansi.ansi().fg(colour).a("\u2588\u2588").reset().toString();
    }

}
