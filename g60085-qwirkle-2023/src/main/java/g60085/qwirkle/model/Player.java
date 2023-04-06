package g60085.qwirkle.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Player {
    private String name;
    private List<Tile>tiles;

    public Player(String name) {
        this.name = name;
        this.tiles = new ArrayList<>();
    }

    public String getName() {
        return this.name;
    }

    public List<Tile> getTiles() {
        return Collections.unmodifiableList(this.tiles);
    }

    public void refill(){
        Bag bag = Bag.getInstance();
        //System.out.println(bag);
        //System.out.println(bag.size());
        Tile[]refill = bag.getRandomTiles(6-this.tiles.size());
        Collections.addAll(this.tiles, refill);
    }

    public void remove(Tile... ts){
         for(int i = 0; i< ts.length; i++){
             this.tiles.remove(this.tiles.indexOf(ts[i]));
         }
    }
}
