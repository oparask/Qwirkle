package g60085.qwirkle.model;

/**
 * Direction represents the direction in which multiple tiles are placed at once.
 */
public enum Direction {
    UP(-1, 0),
    DOWN(1, 0),
    LEFT(0, -1),
    RIGHT(0, 1);

    private int deltaRow;
    private int deltaCol;

    /**
     * Initializes the direction with the specified row and column offsets.
     *
     * @param deltaRow The row offset for the direction.
     * @param deltaCol The column offset for the direction.
     */
    Direction(int deltaRow, int deltaCol) {
        this.deltaRow = deltaRow;
        this.deltaCol = deltaCol;
    }

    /**
     * Gives access to the deltaRow attribute.
     *
     * @return The deltaRow attribute.
     */
    public int getDeltaRow() {
        return this.deltaRow;
    }

    /**
     * Gives access to the deltaCol attribute.
     *
     * @return The deltaCol attribute.
     */
    public int getDeltaCol() {
        return this.deltaCol;
    }

    /**
     * Returns the opposite direction of the current one.
     *
     * @return The opposite direction.
     */
    public Direction opposite() {
        return switch (this) {
            case UP -> DOWN;
            case DOWN -> UP;
            case LEFT -> RIGHT;
            case RIGHT -> LEFT;
        };
    }

    /**
     * Returns the diagonal direction of the current one.
     *
     * @return The diagonal direction.
     */
    public Direction diagonal() {
        return switch (this) {
            case UP, DOWN -> RIGHT;
            case LEFT, RIGHT -> DOWN;
        };
    }
}
