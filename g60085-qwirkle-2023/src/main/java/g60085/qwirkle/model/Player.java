package g60085.qwirkle.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Player represents a player in the Qwirkle game.
 */
public class Player implements Serializable {
    private String name;
    private List<Tile> tiles;
    private int score;

    /**
     * Initializes a player with the given name and a starting score of 6.
     *
     * @param name the name or nickname of the player.
     */
    public Player(String name) {
        this.name = name;
        this.tiles = new ArrayList<>();
        this.score = 6;
    }

    /**
     * Gets the name of the player.
     *
     * @return the name or nickname of the player.
     */
    public String getName() {
        return this.name;
    }


    /**
     * Gets the score of the player.
     *
     * @return the score of the player.
     */
    public int getScore() {
        return this.score;
    }

    /**
     * Gets the hand of the player.
     *
     * @return an unmodifiable list of tiles representing the player's hand.
     */
    public List<Tile> getHand() {
        return Collections.unmodifiableList(this.tiles);
    }

    /**
     * Refills the player's hand by drawing tiles from the tile bag.
     * It fills the hand to have 6 tiles, if possible.
     */
    public void refill() {
        Bag bag = Bag.getInstance();
        Tile[] refill = bag.getRandomTiles(6 - this.tiles.size());
        Collections.addAll(this.tiles, refill);
    }

    /**
     * Removes the specified tiles from the player's hand.
     *
     * @param tiles the tiles to remove from the hand.
     */
    public void remove(Tile... tiles) {
        for (Tile t : tiles) {
            this.tiles.remove(this.tiles.indexOf(t));
        }
    }

    /**
     * Adds the specified value to the player's score.
     *
     * @param value the number of points to add to the score.
     */
    public void addScore(int value) {
        this.score += value;
    }


    /**
     * Set the score.
     * @param i the score.
     */
    //for tests
    public void setScore(int i) {
        this.score = i;
    }
}