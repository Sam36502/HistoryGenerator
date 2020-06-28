package ch.pearcenet.historygen.world;

import ch.pearcenet.historygen.exc.InvalidProvinceException;
import com.flowpowered.noise.module.source.Perlin;

import java.util.ArrayList;
import java.util.Random;

public class World {

    // World Generation constants
    public static final int MAP_WIDTH = 100; // Total width (Longitude)
    public static final int MAP_HEIGHT = 50; // Total height (Latitude)
    public static final double MAP_ZOOM = 15.0; // Map complexity
    public static final double MAP_OCEAN = 0.0; // Height up to which counts as ocean
    public static final double MAP_COAST = 0.2; // Height up to which counts as coast
    public static final double MAP_BEACH = 0.28; // Height up to which counts as beach
    public static final double MAP_HILLS = 0.6; // Height up to which counts as hills. Anything above is a mountain
    public static final double MAP_BARREN = -0.3;
    public static final double MAP_DESERT = 0.1;
    public static final double MAP_MEDIUM = 0.3;

    private Province[][] map;

    private ArrayList<Nation> nations;

    Random rand;

    private int year;

    public World(long seed) {
        this.rand = new Random(seed);

        // Build Perlin noise generator
        Perlin terrainGen = new Perlin();
        terrainGen.setSeed(this.rand.nextInt());
        Perlin fertilityGen = new Perlin();
        fertilityGen.setSeed(this.rand.nextInt());

        // Set starting year
        this.year = getRandInt(1, 100);

        // Fill map with land from Perlin noise
        map = new Province[MAP_WIDTH][MAP_HEIGHT];
        for (int x=0; x<MAP_WIDTH; x++) {
            for (int y = 0; y < MAP_HEIGHT; y++) {
                double pX = ((double) x)/MAP_ZOOM;
                double pY = ((double) y)/MAP_ZOOM;

                double height = terrainGen.getValue(pX, pY, 0);
                double fert = fertilityGen.getValue(pX, pY, 0);

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
        for (int n=0; n<getRandInt(5, 15); n++) {

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

    public int getRandInt(int min, int max) {
        return this.rand.nextInt(max - min) + min;
    }

    public int getYear() {
        return year;
    }

    public ArrayList<Nation> getNations() {
        return nations;
    }

    public Province[][] getMap() {
        return map;
    }
}
