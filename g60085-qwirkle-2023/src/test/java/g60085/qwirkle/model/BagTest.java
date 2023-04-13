package g60085.qwirkle.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BagTest {

    @Test
    void getInstance() {
        Bag bag = Bag.getInstance();
        Bag bag1 = Bag.getInstance();
        assertEquals(bag1, bag);
        assertEquals(bag.size(), 108);
    }

    @Test
    void getRandomTiles() {
        Bag bag = Bag.getInstance();
        Tile[] randomTiles = bag.getRandomTiles(3);
        assertEquals(randomTiles.length, 3);
        assertEquals(bag.size(), 105);
    }

    @Test
    void getRandomTiles_Null() {
        Bag bag = Bag.getInstance();
        Tile[] randomTiles1 = bag.getRandomTiles(108);
        Tile[] randomTiles2 = bag.getRandomTiles(4);
        assertEquals(randomTiles2.length, 0);
        assertEquals(bag.size(), 0);
    }

    @Test
    void getRandomTiles_RemainingTiles() {
        Bag bag = Bag.getInstance();
        Tile[] randomTiles1 = bag.getRandomTiles(107);
        Tile[] randomTiles2 = bag.getRandomTiles(4);
        assertEquals(randomTiles2.length, 1);
        assertEquals(bag.size(), 0);
    }
}