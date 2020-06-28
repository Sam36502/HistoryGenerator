package ch.pearcenet.historygen.display;

import ch.pearcenet.historygen.world.Nation;
import ch.pearcenet.historygen.world.Province;
import ch.pearcenet.historygen.world.World;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class MapDisplay extends Canvas {

    private static final int PIXEL_SIZE = 10; // side length in pixels of each larger pixel

    private static final Color[] FERTILITY_COLOURS = {
            Color.RED, //       Barren
            Color.YELLOW, //    Low Fertility
            Color.ORANGE, //    Medium Fertility
            Color.GREEN //      High Fertility
    };

    private static final Color[] GEOGRAPHIC_COLOURS = {
            Color.BLUE, //      Ocean
            Color.CYAN, //      Sea
            Color.YELLOW, //    Beach
            Color.GREEN, //     Hills
            Color.GRAY //       Mountains
    };

    public static enum Mapmode {
        GEOGRAPHIC,
        FERTILITY,
        POLITICAL
    }

    private JFrame frame;

    private Mapmode mode;
    private World world;

    public MapDisplay(Mapmode mode, World world) {
        this.mode = mode;
        this.world = world;
        frame = new JFrame("World Map");
        this.setSize(PIXEL_SIZE * World.MAP_WIDTH, PIXEL_SIZE * World.MAP_HEIGHT);
        frame.add(this);
        frame.pack();
        frame.setVisible(true);
    }

    @Override
    public void paint(Graphics g) {
        Map<Nation, Color> colourMap = new HashMap<>();
        colourMap.put(null, Color.white);

        for (int y=0; y<World.MAP_HEIGHT; y++) {
            for (int x=0; x<World.MAP_WIDTH; x++) {
                Province province = world.getMap()[x][y];

                switch (mode) {
                    case FERTILITY:
                        g.setColor(FERTILITY_COLOURS[province.getFertility()]);
                        g.fillRect(x * PIXEL_SIZE, y * PIXEL_SIZE, PIXEL_SIZE, PIXEL_SIZE);
                        break;

                    case POLITICAL:
                        // Make the oceans black
                        if (province.getHeight() < 0) {
                            g.setColor(Color.BLACK);
                            g.fillRect(x * PIXEL_SIZE, y * PIXEL_SIZE, PIXEL_SIZE, PIXEL_SIZE);
                            break;
                        }

                        // Assign a random colour to each nation if it doesn't already have one
                        if (!colourMap.containsKey(province.getOwner())) {
                            Color col;
                            do {
                                col = new Color(
                                        world.getRandInt(10, 240),
                                        world.getRandInt(10, 240),
                                        world.getRandInt(10, 240)
                                );
                            } while (colourMap.containsKey(province.getOwner()));
                            colourMap.put(province.getOwner(), col);
                        }

                        g.setColor(colourMap.get(province.getOwner()));
                        g.fillRect(x * PIXEL_SIZE, y * PIXEL_SIZE, PIXEL_SIZE, PIXEL_SIZE);
                        break;

                    case GEOGRAPHIC:
                        g.setColor(GEOGRAPHIC_COLOURS[province.getHeight() + 2]);
                        g.fillRect(x * PIXEL_SIZE, y * PIXEL_SIZE, PIXEL_SIZE, PIXEL_SIZE);
                        break;
                }

            }
        }
    }

}
