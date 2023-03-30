package g60085.qwirkle.model;

/**
 * Tile represents a tile in the game
 */
public class Tile_bis {
    private final Color color;
    private final Shape shape;

    //constructeur javadoc
    public Tile_bis(Color color, Shape shape) {
        this.color = color;
        this.shape = shape;
    }

    //accesseurs javadoc
    public Color getColor() {
        return this.color;
    }

    public Shape getShape() {
        return this.shape;
    }

    //toString javadoc
    @Override
    public String toString() {
        return this.shape.name() + ":" + this.color.name(); //on peut checher le nom d'une enum
    }


}
