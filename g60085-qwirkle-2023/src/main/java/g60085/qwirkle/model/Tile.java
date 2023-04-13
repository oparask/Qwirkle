package g60085.qwirkle.model;

/**
 * Tile represents a tile in the game.
 */
public record Tile(Color color, Shape shape) {
    /**
     * Allows to see the shape and color of a tile as text.
     *
     * @return a string representation of the object Tile.
     */
    @Override
    public String toString() {
        return this.shape.name() + ":" + this.color.name();
    }

}
