package g60085.qwirkle.model;

import java.io.Serializable;

/**
 * Tile represents a tile in the game.
 */
public record Tile(Color color, Shape shape) implements Serializable {
}
