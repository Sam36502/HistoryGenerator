package ch.pearcenet.historygen;

import ch.pearcenet.historygen.exc.InvalidProvinceException;
import org.fusesource.jansi.Ansi;

import java.util.Random;

public class World {

    private static final int SEED_ISLAND_COUNT = 5;

    // Display characters for province heights
    private static final Ansi HEIGHT_DEEP_SEA = Ansi.ansi().fg(Ansi.Color.BLUE).a("\u2588\u2588").reset();
    private static final Ansi HEIGHT_SHALLOW_SEA = Ansi.ansi().fg(Ansi.Color.CYAN).a("\u2588\u2588").reset();
    private static final Ansi HEIGHT_BEACH = Ansi.ansi().fg(Ansi.Color.YELLOW).a("\u2588\u2588").reset();
    private static final Ansi HEIGHT_HILL = Ansi.ansi().fg(Ansi.Color.GREEN).a("\u2588\u2588").reset();
    private static final Ansi HEIGHT_MOUNTAIN = Ansi.ansi().fg(Ansi.Color.WHITE).a("\u2588\u2588").reset();

    /**
     * A grid of provinces that make up the map
     */
    private Province[][] map;

    /**
     * Random number sequence generator
     */
    private Random rand;

    /**
     * Generate a world with a random number seed
     * @param seed World seed
     * @param latitude Map 'width'
     * @param longitude Map 'height'
     */
    public World(long seed, int longitude, int latitude) {
        // Initialise map as all Ocean
        map = new Province[longitude][latitude];
        for (int x=0; x<longitude; x++) {
            for (int y = 0; y < latitude; y++) {
                try {
                    map[x][y] = new Province("Ocean", -2, 0);
                } catch (InvalidProvinceException e) {
                    e.printStackTrace();
                }
            }
        }

        // Random number generator with seed
        rand = new Random(seed);

        // Generate 'seed islands' to grow into full continents
        int[][] seeds = new int [SEED_ISLAND_COUNT][2];
        for (int i=0; i<SEED_ISLAND_COUNT; i++) {
            seeds[i][0] = randIntInRange(rand, 0, longitude);
            seeds[i][1] = randIntInRange(rand, 0, latitude);

            int size = randIntInRange(rand, 1, 5);

            // Create surrounding land

        }
    }

    private static int randIntInRange(Random rand, int min, int max) {
        return rand.nextInt(max - min) + min;
    }

    @Override
    public String toString() {

        // Top border
        StringBuilder result = new StringBuilder(Ansi.ansi().bg(Ansi.Color.BLUE).a("\u2554").reset().toString());
        for (int i=0; i<map.length; i++)
            result.append(Ansi.ansi().bg(Ansi.Color.BLUE).a("\u2550\u2550").reset().toString());
        result.append(Ansi.ansi().bg(Ansi.Color.BLUE).a("\u2557\n").reset().toString());

        // All rows
        for (int y=map[0].length-1; y>=0; y--) {
            result.append(Ansi.ansi().bg(Ansi.Color.BLUE).a("\u2551").reset().toString());

            for (int x=0; x<map.length; x++) {
                switch (map[x][y].getHeight()) {

                    case -2:
                        result.append(HEIGHT_DEEP_SEA);
                        break;

                    case -1:
                        result.append(HEIGHT_SHALLOW_SEA);
                        break;

                    case 0:
                        result.append(HEIGHT_BEACH);
                        break;

                    case 1:
                        result.append(HEIGHT_HILL);
                        break;

                    case 2:
                        result.append(HEIGHT_MOUNTAIN);
                        break;
                }
            }
            result.append(Ansi.ansi().bg(Ansi.Color.BLUE).a("\u2551\n").reset().toString());
        }

        // Bottom border
        result.append(Ansi.ansi().bg(Ansi.Color.BLUE).a("\u255A").reset().toString());
        for (int i=0; i<map.length; i++)
            result.append(Ansi.ansi().bg(Ansi.Color.BLUE).a("\u2550\u2550").reset().toString());
        result.append(Ansi.ansi().bg(Ansi.Color.BLUE).a("\u255D\n").reset().toString());

        return result.toString();
    }
}
