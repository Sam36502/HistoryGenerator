package ch.pearcenet.historygen.world;

import ch.pearcenet.historygen.exc.InvalidProvinceException;
import de.articdive.jnoise.JNoise;
import de.articdive.jnoise.interpolation.InterpolationType;
import org.fusesource.jansi.Ansi;

import java.util.Random;

public class World {

    // World Generation constants
    static final int MAP_WIDTH = 100; // Longitude
    static final int MAP_HEIGHT = 50; // Latitude
    static final double MAP_ZOOM = 15.0; // Map complexity
    static final double MAP_OCEAN = 0.0; // Height up to which counts as ocean
    static final double MAP_COAST = 0.2; // Height up to which counts as coast
    static final double MAP_BEACH = 0.28; // Height up to which counts as beach
    static final double MAP_HILLS = 0.6; // Height up to which counts as hills. Anything above is a mountain

    // Display characters for province heights
    private static final Ansi HEIGHT_DEEP_SEA = Ansi.ansi().fg(Ansi.Color.BLUE).a("\u2588\u2588").reset();
    private static final Ansi HEIGHT_SHALLOW_SEA = Ansi.ansi().fg(Ansi.Color.CYAN).a("\u2588\u2588").reset();
    private static final Ansi HEIGHT_BEACH = Ansi.ansi().fg(Ansi.Color.YELLOW).a("\u2588\u2588").reset();
    private static final Ansi HEIGHT_HILL = Ansi.ansi().fg(Ansi.Color.GREEN).a("\u2588\u2588").reset();
    private static final Ansi HEIGHT_MOUNTAIN = Ansi.ansi().fg(Ansi.Color.WHITE).a("\u2588\u2588").reset();

    private Province[][] map;

    public World(int seed) {
        // Build Perling noise generator
        JNoise perlin = JNoise.newBuilder().perlin().setInterpolationType(InterpolationType.LINEAR).setSeed(seed).build();

        Random rand = new Random(seed);

        // Fill map with
        map = new Province[MAP_WIDTH][MAP_HEIGHT];
        for (int x=0; x<MAP_WIDTH; x++) {
            for (int y = 0; y < MAP_HEIGHT; y++) {
                double pX = ((double) x)/MAP_ZOOM;
                double pY = ((double) y)/MAP_ZOOM;

                double height = perlin.getNoise(pX, pY);

                if (height <= MAP_OCEAN) {
                    try {
                        map[x][y] = new Province("DEFAULT", -2, rand.nextInt(2) + 1);
                    } catch (InvalidProvinceException e) {
                        e.printStackTrace();
                    }
                } else if (height <= MAP_COAST) {
                    try {
                        map[x][y] = new Province("DEFAULT", -1, rand.nextInt(2) + 1);
                    } catch (InvalidProvinceException e) {
                        e.printStackTrace();
                    }
                } else if (height <= MAP_BEACH) {
                    try {
                        map[x][y] = new Province("DEFAULT", 0, rand.nextInt(2) + 1);
                    } catch (InvalidProvinceException e) {
                        e.printStackTrace();
                    }
                } else if (height <= MAP_HILLS) {
                    try {
                        map[x][y] = new Province("DEFAULT", 1, rand.nextInt(2) + 1);
                    } catch (InvalidProvinceException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        map[x][y] = new Province("DEFAULT", 2, rand.nextInt(2) + 1);
                    } catch (InvalidProvinceException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
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
