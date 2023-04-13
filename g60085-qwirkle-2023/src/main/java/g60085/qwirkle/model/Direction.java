package g60085.qwirkle.model;

/**
 * Direction represents the direction in which multiple tiles are placed at once.
 */
public enum Direction {
    UP(-1, 0),
    DOWN(1, 0),
    LEFT(0, -1),
    RIGHT(0, 1);


    private int deltaCol;
    private int deltaRow;


    /**
     * Initializes the values of the Direction enumeration with the values passed in parameters.
     *
     * @param deltaRow tiles placement line index.
     * @param deltaCol tiles placement column index.
     */
    Direction(int deltaRow, int deltaCol) { 
        this.deltaRow = deltaRow;
        this.deltaCol = deltaCol;
    }

    /**
     * Gives access to the deltaRow attribute.
     *
     * @return the deltaRow attribute.
     */
    public int getDeltaRow() {
        return this.deltaRow;
    }

    /**
     * Gives access to the deltaCol attribute.
     *
     * @return the deltaCol attribute.
     */
    public int getDeltaCol() {
        return this.deltaCol;
    }

    /**
     * @return the opposite direction of the current one.
     */

    public Direction opposite() {
        return switch (this) {
            case UP -> DOWN;
            case DOWN -> UP;
            case LEFT -> RIGHT;
            case RIGHT -> LEFT;
            default -> throw new IllegalArgumentException("Not a direction");
        };
    }
}
