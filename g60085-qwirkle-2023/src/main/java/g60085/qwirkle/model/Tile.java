package g60085.qwirkle.model;

//on a deja un constructeur et un getter
/**
 * Tile represents a tile in the game
 */
public record Tile(Color color, Shape shape) {

    @Override
    public String toString() {
        return this.shape.name() + ":" + this.color.name(); //on peut checher le nom d'une enum
    }

}
