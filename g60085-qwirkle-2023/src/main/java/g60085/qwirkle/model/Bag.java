package g60085.qwirkle.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Bag represents the bag of tiles in the game.
 */
public class Bag implements Serializable {
    private final List<Tile> tiles;
    private static Bag instance;

    /**
     * Creates an instance of the type Bag by creating 108 tiles.
     * Since there are 6 colors and 6 shapes, it initializes 36 different tiles with 3 copies of each tile.
     */
    private Bag() {
        this.tiles = new ArrayList<>();
        for (int i = 0; i < Color.values().length; i++) {
            for (int j = 0; j < Shape.values().length; j++) {
                this.tiles.add(new Tile(Color.values()[i], Shape.values()[j]));
                this.tiles.add(new Tile(Color.values()[i], Shape.values()[j]));
                this.tiles.add(new Tile(Color.values()[i], Shape.values()[j]));
            }
        }
        Collections.shuffle(tiles);
    }

    /**
     * Instantiate a single tile bag using the design pattern Singleton.
     *
     * @return the only instance of type Bag.
     */
    public static Bag getInstance() {
        if (instance == null) {
            instance = new Bag();
        }
        return instance;
    }

    /**
     * Allows access to tiles from the bag in other classes.
     *
     * @param n number of tiles requested.
     * @return an array with the requested tiles, null if the bag is empty or
     * an array of remaining tiles if there are not enough left.
     */
    public Tile[] getRandomTiles(int n) throws QwirkleException {
        if (n == 0 || n > 6) {
            throw new QwirkleException("Invalid number");
        }
        if (this.tiles == null) {
            return null;
        }
        Tile[] randomTiles;
        if (n <= this.tiles.size()) {
            randomTiles = new Tile[n];
            for (int i = 0; i < randomTiles.length; i++) {
                randomTiles[i] = tiles.remove(tiles.size() - 1); //returns the removed element
            }
        } else {
            randomTiles = new Tile[tiles.size()];
            for (int i = 0; i < randomTiles.length; i++) {
                randomTiles[i] = tiles.remove(tiles.size() - 1);
            }
        }
        return randomTiles;
    }

    //For tests
    /**
     * Gives access to the size of the tiles attribute.
     *
     * @return the size of the bag of tiles.
     */
    public int size() {
        return this.tiles.size();
    }
}
