package g60085.qwirkle.model;

import java.io.Serializable;

/**
 *  Tile represents a tile in the Qwirkle game.
 *  Each tile has a specific color and shape.
 *  The Tile class is a record, providing automatic generation of getters, equals(), hashCode(), and toString().
 *  This class is serializable, allowing tiles to be saved, loaded, and transmitted between different components of the game.
 */
public record Tile(Color color, Shape shape) implements Serializable {
}
