package g60085.qwirkle.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Bag represents the bag of tiles
 */
public class Bag {
    private List<Tile> tiles;
    private static Bag bag = new Bag();

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
        return bag;
    }

    public Tile[] getRandomTiles(int n) {
        if (this.tiles == null) {
            return null;
        }
        Tile[] randomTiles = new Tile[n];
        if (n <= this.tiles.size()) {
            for (int i = 0; i < n; i++) {
                randomTiles[i] = tiles.remove(tiles.size() - 1); //retourne l'élément supprimé
            }
        } else {
           /* for (int i = 0; i < this.tiles.size(); i++) {
                randomTiles[i] = tiles.remove(tiles.size() - 1);
            }*/
            size();
        }
        return randomTiles;
    }

    @Override
    public String toString() {
        return "Bag{" +
                "tiles=" + tiles +
                '}';
    }

    public int size(){
        return this.tiles.size();
    }

    public static void main(String[] args) {
        Bag bag = new Bag();
        System.out.println(bag);
        Tile tile = new Tile(Color.RED, Shape.CROSS);
        System.out.println(tile);

    }
}
