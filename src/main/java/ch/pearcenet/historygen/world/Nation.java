package ch.pearcenet.historygen.world;

import ch.pearcenet.historygen.language.Language;
import org.fusesource.jansi.Ansi;

import java.util.ArrayList;
import java.util.Random;

/**
 * Group of people with a common language
 */
public class Nation implements Comparable<Nation> {

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

    private ArrayList<Province> land;

    private Ansi.Color colour;

    public Nation(long seed) {
        Random r = new Random(seed);

        this.language = new Language(seed);
        this.colour = AVAIL_COLOURS[r.nextInt(AVAIL_COLOURS.length)];
        this.land = new ArrayList<>();
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

    public ArrayList<Province> getLand() {
        return land;
    }

    public int getFecundity() {
        int fec = 0;
        for (Province p: land)
            fec += p.getFertility();
        return fec;
    }

    public int getSize() {
        return land.size();
    }

    @Override
    public int compareTo(Nation o) {
        return Integer.valueOf(this.getSize()).compareTo(Integer.valueOf(o.getSize()));
    }
}
