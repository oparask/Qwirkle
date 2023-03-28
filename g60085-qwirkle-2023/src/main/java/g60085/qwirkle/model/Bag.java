package g60085.qwirkle.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Bag represents the bag of tiles
 */
public class Bag {
    private List<Tile> tiles;

    //constructeur
    private Bag() {
        this.tiles = new ArrayList<>();
        for (int i = 0; i < Color.values().length; i++) {
            for (int j = 0; j < Shape.values().length; j++) {
                Tile tile1 = new Tile(Color.values()[i], Shape.values()[j]);
                Tile tile2 = new Tile(Color.values()[i], Shape.values()[j]);
                Tile tile3 = new Tile(Color.values()[i], Shape.values()[j]);
                this.tiles.add(tile1);
                this.tiles.add(tile2);
                this.tiles.add(tile3);
            }
        }
    }

    public static Bag getInstance() {
        Bag bag = new Bag();
        return bag;
    }

    public Tile[] getRandomTiles(int n){
        Tile[] bag = new Tile[108];
        for(int i = 0; i< bag.length; i++){
            bag[i] = this.tiles[i];
        }
    }
}
