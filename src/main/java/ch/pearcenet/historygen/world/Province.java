package ch.pearcenet.historygen.world;

import ch.pearcenet.historygen.exc.InvalidProvinceException;
import org.fusesource.jansi.Ansi;

/**
 * Contains information about single tiles on the board
 */
public class Province {

    /**
     * Name of the province.
     * Based on home nation language.
     */
    private String name;

    /**
     * Province height
     *  2 -> Mountain
     *  1 -> Hill
     *  0 -> Sea Level
     * -1 -> Shallow Water
     * -2 -> Deep Water
     */
    private int height;

    /**
     * Province food production
     * 3 = Abundant
     * 2 = Sustainable
     * 1 = Scarce
     * 0 = Produces no food
     */
    private int fertility;

    private Nation owner;

    /**
     * Province Constructor
     * @param name Name of the province
     * @param height Province height
     * @param fertility Province fertility (Food production)
     * @throws InvalidProvinceException Thrown if invalid height or fertility is passed.
     */
    public Province(String name, int height, int fertility) throws InvalidProvinceException {
        if (height < -2 || height > 2)
            throw new InvalidProvinceException("Invalid height in province '" + name + "'.");
        if (fertility < 0 || fertility > 3)
            throw new InvalidProvinceException("Invalid fertility in province '" + name + "'.");

        this.name = name;
        this.height = height;
        this.fertility = fertility;
        this.owner = null;
    }

    /**
     * Sets the name of the province
     * @param name New Province name
     * @return The current Province
     */
    public Province setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * @return The Province name
     */
    public String getName() { return this.name; }

    /**
     * @return The Province height
     */
    public int getHeight() { return this.height; }

    /**
     * @return The Province fertility
     */
    public int getFertility() { return this.fertility; }

    public Province setOwner(Nation owner) {
        this.owner = owner;
        this.name = this.owner.getLanguage().newWord();
        return this;
    }

    public Nation getOwner() {
        return owner;
    }
}
