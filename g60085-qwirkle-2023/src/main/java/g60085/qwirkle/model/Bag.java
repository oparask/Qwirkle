package g60085.qwirkle.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Bag represents the bag of tiles
 */
public class Bag {
    private List<Tile> tiles;
    private static Bag instance;

    //constructeur
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


    public static Bag getInstance() {
        if(instance == null){
            instance = new Bag();
        }
        return instance;
    }

    public Tile[] getRandomTiles(int n) {
        if (this.tiles == null) {
            return null;
        }
        if (n <= this.tiles.size()) {
            Tile[] randomTiles = new Tile[n];
            for (int i = 0; i < n; i++) {
                randomTiles[i] = tiles.remove(tiles.size() - 1); //retourne l'élément supprimé
            }
            return randomTiles;
        } else {
            Tile[] randomTiles = new Tile[tiles.size()];
            for (int i = 0; i < randomTiles.length; i++) {
                randomTiles[i] = tiles.remove(tiles.size() - 1);
            }
            return randomTiles;
        }
    }

    @Override
    public String toString() {
        return "tiles = " + this.tiles;
    }

    public int size() {
        return this.tiles.size();
    }


}
