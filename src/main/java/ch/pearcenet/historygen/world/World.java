package ch.pearcenet.historygen.world;

import ch.pearcenet.historygen.exc.InvalidProvinceException;
import de.articdive.jnoise.JNoise;
import de.articdive.jnoise.interpolation.InterpolationType;
import org.fusesource.jansi.Ansi;

import java.util.ArrayList;
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
    static final double MAP_BARREN = -0.3;
    static final double MAP_DESERT = 0.1;
    static final double MAP_MEDIUM = 0.3;

    private Province[][] map;

    private ArrayList<Nation> nations;

    Random rand;

    private int year;

    public World(long seed) {
        this.rand = new Random(seed);

        // Build Perlin noise generator
        JNoise terrainGen = JNoise.newBuilder().perlin().setInterpolationType(InterpolationType.LINEAR).setSeed(rand.nextInt()).build();
        JNoise fertilityGen = JNoise.newBuilder().perlin().setInterpolationType(InterpolationType.LINEAR).setSeed(rand.nextInt()).build();

        // Set starting year
        this.year = rand.nextInt(100 - 1) + 1; // Starting year is somewhere between 1 - 100

        // Fill map with land from Perlin noise
        map = new Province[MAP_WIDTH][MAP_HEIGHT];
        for (int x=0; x<MAP_WIDTH; x++) {
            for (int y = 0; y < MAP_HEIGHT; y++) {
                double pX = ((double) x)/MAP_ZOOM;
                double pY = ((double) y)/MAP_ZOOM;

                double height = terrainGen.getNoise(pX, pY);
                double fert = fertilityGen.getNoise(pX, pY);

                // Get fertility
                int fertility = 3;
                if (fert <= MAP_BARREN)
                    fertility = 0;
                else if (fert <= MAP_DESERT)
                    fertility = 1;
                else if (fert <= MAP_MEDIUM)
                    fertility = 2;

                if (height <= MAP_OCEAN) {
                    try {
                        map[x][y] = new Province("Terra Nullius", -2, fertility);
                    } catch (InvalidProvinceException e) {
                        e.printStackTrace();
                    }
                } else if (height <= MAP_COAST) {
                    try {
                        map[x][y] = new Province("Terra Nullius", -1, fertility);
                    } catch (InvalidProvinceException e) {
                        e.printStackTrace();
                    }
                } else if (height <= MAP_BEACH) {
                    try {
                        map[x][y] = new Province("Terra Nullius", 0, fertility);
                    } catch (InvalidProvinceException e) {
                        e.printStackTrace();
                    }
                } else if (height <= MAP_HILLS) {
                    try {
                        map[x][y] = new Province("Terra Nullius", 1, fertility);
                    } catch (InvalidProvinceException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        map[x][y] = new Province("Terra Nullius", 2, fertility);
                    } catch (InvalidProvinceException e) {
                        e.printStackTrace();
                    }
                }

            }
        }

        // Populate the world with random nations
        nations = new ArrayList<>();
        for (int n=0; n<rand.nextInt(15 - 5) + 5; n++) { // 5 - 15 Starting nations

            // Generate random seed coords on land
            int x;
            int y;
            do {
                x = rand.nextInt(MAP_WIDTH);
                y = rand.nextInt(MAP_HEIGHT);
            } while (map[x][y].getHeight() < 0 || map[x][y].getOwner() != null);

            // Add nation to world
            Nation nation = new Nation(rand.nextLong());
            map[x][y].setOwner(nation);
            nations.add(nation);
        }

    }

    public void nextYear() {
        this.year++;

        // Expand each nation according to its fecundity
        for (int y=0; y<MAP_HEIGHT; y++) {
            for (int x=0; x<MAP_WIDTH; x++) {

                // Check land even belongs to a nation
                Nation owner = map[x][y].getOwner();
                if (owner == null)
                    continue;

                // Check borders and expand if possible
                if (x+1 < MAP_WIDTH && map[x+1][y].getOwner() == null && map[x+1][y].getHeight() >= 0)
                    if (rand.nextInt(owner.getFecundity()+1)>=owner.getSize()) map[x+1][y].setOwner(owner);

                if (x - 1 >= 0 && map[x-1][y].getOwner() == null && map[x-1][y].getHeight() >= 0)
                    if (rand.nextInt(owner.getFecundity()+1)>=owner.getSize()) map[x-1][y].setOwner(owner);

                if (y+1 < MAP_HEIGHT && map[x][y+1].getOwner() == null && map[x][y+1].getHeight() >= 0)
                    if (rand.nextInt(owner.getFecundity()+1)>=owner.getSize()) map[x][y+1].setOwner(owner);

                if (y - 1 >= 0 && map[x][y-1].getOwner() == null && map[x][y-1].getHeight() >= 0)
                    if (rand.nextInt(owner.getFecundity()+1)>=owner.getSize()) map[x][y-1].setOwner(owner);

            }
        }

    }

    public String getGeoMap() {
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
                        result.append(Ansi.ansi().fg(Ansi.Color.BLUE).a("\u2588\u2588").reset());
                        break;

                    case -1:
                        result.append(Ansi.ansi().fg(Ansi.Color.CYAN).a("\u2588\u2588").reset());
                        break;

                    case 0:
                        result.append(Ansi.ansi().fg(Ansi.Color.YELLOW).a("\u2588\u2588").reset());
                        break;

                    case 1:
                        result.append(Ansi.ansi().fg(Ansi.Color.GREEN).a("\u2588\u2588").reset());
                        break;

                    case 2:
                        result.append(Ansi.ansi().fg(Ansi.Color.WHITE).a("\u2588\u2588").reset());
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

    public String getPolMap() {
        // Top border
        StringBuilder result = new StringBuilder(Ansi.ansi().bg(Ansi.Color.BLACK).a("\u2554").reset().toString());
        for (int i=0; i<map.length; i++)
            result.append(Ansi.ansi().bg(Ansi.Color.BLACK).a("\u2550\u2550").reset().toString());
        result.append(Ansi.ansi().bg(Ansi.Color.BLACK).a("\u2557\n").reset().toString());

        // All rows
        for (int y=map[0].length-1; y>=0; y--) {
            result.append(Ansi.ansi().bg(Ansi.Color.BLACK).a("\u2551").reset().toString());

            for (int x=0; x<map.length; x++) {
                if (map[x][y].getOwner() != null) {
                    result.append(map[x][y].getOwner().getAnsiDisplay());
                } else {
                    if (map[x][y].getHeight() < 0)
                        // Ocean
                        result.append(Ansi.ansi().fg(Ansi.Color.BLACK).a("\u2588\u2588").reset());
                    else
                        // Terra Nullius
                        result.append(Ansi.ansi().fg(Ansi.Color.WHITE).a("\u2588\u2588").reset());
                }
            }
            result.append(Ansi.ansi().bg(Ansi.Color.BLACK).a("\u2551\n").reset().toString());
        }

        // Bottom border
        result.append(Ansi.ansi().bg(Ansi.Color.BLACK).a("\u255A").reset().toString());
        for (int i=0; i<map.length; i++)
            result.append(Ansi.ansi().bg(Ansi.Color.BLACK).a("\u2550\u2550").reset().toString());
        result.append(Ansi.ansi().bg(Ansi.Color.BLACK).a("\u255D\n").reset().toString());

        return result.toString();
    }

    public String getFerMap() {
        // Top border
        StringBuilder result = new StringBuilder(Ansi.ansi().bg(Ansi.Color.BLACK).a("\u2554").reset().toString());
        for (int i=0; i<map.length; i++)
            result.append(Ansi.ansi().bg(Ansi.Color.BLACK).a("\u2550\u2550").reset().toString());
        result.append(Ansi.ansi().bg(Ansi.Color.BLACK).a("\u2557\n").reset().toString());

        // All rows
        for (int y=map[0].length-1; y>=0; y--) {
            result.append(Ansi.ansi().bg(Ansi.Color.BLACK).a("\u2551").reset().toString());

            for (int x=0; x<map.length; x++) {
                switch (map[x][y].getFertility()) {

                    case 0:
                        result.append(Ansi.ansi().fg(Ansi.Color.RED).a("\u2588").reset());
                        break;

                    case 1:
                        result.append(Ansi.ansi().fg(Ansi.Color.MAGENTA).a("\u2588").reset());
                        break;

                    case 2:
                        result.append(Ansi.ansi().fg(Ansi.Color.YELLOW).a("\u2588").reset());
                        break;

                    case 3:
                        result.append(Ansi.ansi().fg(Ansi.Color.GREEN).a("\u2588").reset());
                        break;

                }
                switch (map[x][y].getHeight()) {

                    case -2:
                        result.append(Ansi.ansi().fg(Ansi.Color.BLUE).a("\u2588").reset());
                        break;

                    case -1:
                        result.append(Ansi.ansi().fg(Ansi.Color.CYAN).a("\u2588").reset());
                        break;

                    case 0:
                        result.append(Ansi.ansi().fg(Ansi.Color.YELLOW).a("\u2588").reset());
                        break;

                    case 1:
                        result.append(Ansi.ansi().fg(Ansi.Color.GREEN).a("\u2588").reset());
                        break;

                    case 2:
                        result.append(Ansi.ansi().fg(Ansi.Color.WHITE).a("\u2588").reset());
                        break;
                }
            }
            result.append(Ansi.ansi().bg(Ansi.Color.BLACK).a("\u2551\n").reset().toString());
        }

        // Bottom border
        result.append(Ansi.ansi().bg(Ansi.Color.BLACK).a("\u255A").reset().toString());
        for (int i=0; i<map.length; i++)
            result.append(Ansi.ansi().bg(Ansi.Color.BLACK).a("\u2550\u2550").reset().toString());
        result.append(Ansi.ansi().bg(Ansi.Color.BLACK).a("\u255D\n").reset().toString());

        return result.toString();
    }

    public int getYear() {
        return year;
    }

    public ArrayList<Nation> getNations() {
        return nations;
    }
}
