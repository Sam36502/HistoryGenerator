package ch.pearcenet.historygen;

import ch.pearcenet.historygen.exc.InvalidProvinceException;

import java.util.Random;

/**
 * Used during Terrain generation
 */
public class Island {

    private Province[][] heightmap;

    // Side length
    private int len;

    private Province DEFAULT_SEA;

    // Default Island templates
    private int[][][] DEFAULTS = {
            {
                    {-1, -1, -1},
                    {-1,  0, -1},
                    {-1, -1, -1}
            },
            {
                    {-2, -1, -1, -1, -2},
                    {-1,  0,  0,  0, -1},
                    {-1,  0,  1,  0, -1},
                    {-1,  0,  0,  0, -1},
                    {-2, -1, -1, -1, -2}
            },
            {
                    {-2, -1, -1, -1, -1, -1, -2},
                    {-1, -1,  0,  0,  0, -1, -1},
                    {-1,  0,  1,  1,  1,  0, -1},
                    {-1,  0,  1,  2,  1,  0, -1},
                    {-1,  0,  1,  1,  1,  0, -1},
                    {-1, -1,  0,  0,  0, -1, -1},
                    {-2, -1, -1, -1, -1, -1, -2},
            },
            {
                    {-2, -1, -1, -1, -1, -1, -1, -1, -1},
                    {-1, -1,  0,  0,  0,  0,  0, -1, -1},
                    {-1,  0,  0,  1,  1,  1,  0,  0, -1},
                    {-1,  0,  1,  1,  2,  1,  1,  0, -1},
                    {-1,  0,  1,  2,  2,  2,  1,  0, -1},
                    {-1,  0,  1,  1,  2,  1,  1,  0, -1},
                    {-1,  0,  0,  1,  1,  1,  0,  0, -1},
                    {-1, -1,  0,  0,  0,  0,  0, -1, -1},
                    {-2, -1, -1, -1, -1, -1, -1, -1, -1},
            },
            {
                    {-2, -2, -2, -2, -1, -1, -1, -1, -1, -1, -1, -1, -1, -2, -2, -2, -2},
                    {-2, -2, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -2, -2},
                    {-2, -1, -1, -1, -1, -1,  0,  0,  0,  0,  0, -1, -1, -1, -1, -1, -2},
                    {-2, -1, -1, -1,  0,  0,  0,  0,  0,  0,  0,  0,  0, -1, -1, -1, -2},
                    {-1, -1, -1,  0,  0,  0,  1,  1,  1,  1,  1,  0,  0,  0, -1, -1, -1},
                    {-1, -1, -1,  0,  0,  1,  1,  1,  1,  1,  1,  1,  0,  0, -1, -1, -1},
                    {-1, -1,  0,  0,  1,  1,  1,  1,  1,  1,  1,  1,  1,  0,  0, -1, -1},
                    {-1, -1,  0,  0,  1,  1,  1,  2,  2,  2,  1,  1,  1,  0,  0, -1, -1},
                    {-1, -1,  0,  0,  1,  1,  1,  2,  2,  2,  1,  1,  1,  0,  0, -1, -1},
                    {-1, -1,  0,  0,  1,  1,  1,  2,  2,  2,  1,  1,  1,  0,  0, -1, -1},
                    {-1, -1,  0,  0,  1,  1,  1,  1,  1,  1,  1,  1,  1,  0,  0, -1, -1},
                    {-1, -1, -1,  0,  0,  1,  1,  1,  1,  1,  1,  1,  0,  0, -1, -1, -1},
                    {-1, -1, -1,  0,  0,  0,  1,  1,  1,  1,  1,  0,  0,  0, -1, -1, -1},
                    {-2, -1, -1, -1,  0,  0,  0,  0,  0,  0,  0,  0,  0, -1, -1, -1, -2},
                    {-2, -1, -1, -1, -1, -1,  0,  0,  0,  0,  0, -1, -1, -1, -1, -1, -2},
                    {-2, -2, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -2, -2},
                    {-2, -2, -2, -2, -1, -1, -1, -1, -1, -1, -1, -1, -1, -2, -2, -2, -2},
            }
    };

    public Island(int size, Random rand) {
        if (size < 1) size = 1;
        if (size > 5) size = 5;

        try {
            DEFAULT_SEA = new Province("Ocean", -2, 0);
        } catch (InvalidProvinceException e) {
            e.printStackTrace();
        }

        int[][] template = DEFAULTS[size - 1];
        len = template.length;
        this.heightmap = new Province[len][len];

        // Create Province Map
        for (int y=0; y<len; y++) {
            for (int x=0; x<len; x++) {
                // Randomise layout slightly
                int height = template[x][y];
                if (rand.nextInt(50) == 0) {
                    if (rand.nextInt(1) == 0)
                        height++;
                    else
                        height--;
                }

                // Check height's in range
                if (height < -2) height++;
                if (height > 2) height--;

                try {
                    heightmap[x][y] = new Province("DEFAULT", height, rand.nextInt(2) + 1);
                } catch (InvalidProvinceException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }

    public int getLen() { return len; }

    public Province getProvince(int x, int y) {
        int off = (len -1) / 2;
        x += off;
        y += off;

        if (x < 0 || x > len || y < 0 || y > len) return DEFAULT_SEA;

        return heightmap[x][y];
    }

}
