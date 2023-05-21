package g60085.qwirkle.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Provides a single instance of the Bag class using the Singleton design pattern.
 * The Bag instance represents a single tile bag in the game.
 */
public class Bag implements Serializable {
    private final List<Tile> tiles;
    private static Bag instance;

    /**
     * Private constructor to prevent direct instantiation from outside the class.
     * Initializes an instance of the Bag class by creating 108 tiles.
     * Each tile consists of a combination of color and shape.
     * There are 6 colors and 6 shapes, resulting in 36 different tiles.
     * Each tile is added to the bag with 3 copies.
     * The tiles are shuffled randomly to provide a randomized order.
     */
    private Bag() {
        this.tiles = new ArrayList<>();

        // Create tiles for each combination of color and shape
        for (Color color : Color.values()) {
            for (Shape shape : Shape.values()) {
                // Add 3 copies of each tile to the bag
                for (int i = 0; i < 3; i++) {
                    this.tiles.add(new Tile(color, shape));
                }
            }
        }

        // Shuffle the tiles randomly
        Collections.shuffle(tiles);
    }

    /**
     * Retrieves the instance of the Bag class.
     *
     * @return The single instance of the Bag class.
     */
    public static Bag getInstance() {
        if (instance == null) {
            instance = new Bag();
        }
        return instance;
    }

    /**
     * Provides a way to retrieve a specified number of random tiles from the bag.
     * The tiles are removed from the bag when retrieved.
     *
     * @param n The number of tiles requested.
     * @return An array with the requested tiles, or an array of remaining tiles if there are not enough tiles left.
     *  *         Returns null if the bag is empty.
     * @throws QwirkleException If an invalid number of tiles is requested (must be between 1 and 6 inclusive).
     */
    public Tile[] getRandomTiles(int n) throws QwirkleException {
        if (n <= 0 || n > 6) {
            throw new QwirkleException("Invalid number of tiles requested. Must be between 1 and 6 (inclusive).");
        }

        if (this.tiles.isEmpty()) {
            return null;
        }

        int tilesToRetrieve = Math.min(n, this.tiles.size());
        Tile[] randomTiles = new Tile[tilesToRetrieve];

        for (int i = 0; i < tilesToRetrieve; i++) {
            randomTiles[i] = tiles.remove(tiles.size() - 1);
        }

        return randomTiles;
    }

    /**
     * Retrieves the size of the bag of tiles.
     *
     * @return The size of the bag of tiles.
     */
    public int size() {
        return this.tiles.size();
    }
}
