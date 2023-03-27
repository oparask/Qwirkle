package g60085.qwirkle.model;

import java.util.List;

/**
 * Bag represents the bag of tiles
 */
public class Bag {
    private List<Tile> tiles;

    //constructeur
    public Bag() {
        for (int i = 0; i < Color.values().length; i++) {
            for (int j = 0; j < Shape.values().length; j++) {
                Tile tile = new Tile(Color.values()[i], Shape.values()[j]);
                tiles.add(tile);
                tiles.add(this.tiles.size() + 1, tile);
                tiles.add(this.tiles.size() + 2, tile);
            }
        }
    }
}
