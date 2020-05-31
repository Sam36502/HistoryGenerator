package ch.pearcenet.historygen;

import java.util.Random;

/**
 * Used during Terrain generation
 */
public class Island {

    private static final Province SEA = new Province();

    private Province[][] grid;

    public Island(int size, Random rand) {
        switch (size) {
            case 1:
                grid = {
                        {},{}
                }
                break;
        }
    }

}
