package g60085.qwirkle.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Player represents a player in the game.
 */
public class Player {
    private String name;
    private List<Tile> tiles;

    /**
     * Initializes the name of the player.
     *
     * @param name representing his name or nickname.
     */
    public Player(String name) {
        this.name = name;
        this.tiles = new ArrayList<>();
    }

    /**
     * @return the name or nickname of the player as String.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Gives access to the player's tiles without modifying them.
     *
     * @return the hand of the player.
     */
    public List<Tile> getHand() {
        return Collections.unmodifiableList(this.tiles);
    }

    /**
     * Fills the player's hand by completing the tile list for player to have 6 by drawing from tile bag 2.
     */
    public void refill() {
        Bag bag = Bag.getInstance();
        //System.out.println(bag);
        //System.out.println(bag.size());
        Tile[] refill = bag.getRandomTiles(6 - this.tiles.size());
        Collections.addAll(this.tiles, refill);
    }

    /**
     * Removes tiles from the player's hand.
     *
     * @param ts tiles to remove.
     */
    public void remove(Tile... ts) {
        for (Tile t : ts) {
            this.tiles.remove(this.tiles.indexOf(t));
        }
    }
}
