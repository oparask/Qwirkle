package g60085.qwirkle.model;

public enum Direction {
    UP(-1, 0),
    DOWN(1, 0),
    LEFT(0, -1),
    RIGHT(0, 1);

    private int deltaRow;
    private int deltaCol;

    //constructeur
    Direction(int deltaRow, int deltaCol) { //il faut donner des valeurs aux énumerations une fois qu'on met un constructeur
        this.deltaRow = deltaRow;
        this.deltaCol = deltaCol;
    }

    //getter
    public int getDeltaRow() {
        return this.deltaRow;
    }

    public int getDeltaCol() {
        return this.deltaCol;
    }

    public Direction opposite() {
        switch (this) {
            case UP:
                return DOWN;
            case DOWN:
                return UP;
            case LEFT:
                return RIGHT;
            case RIGHT:
                return LEFT;
            default:
                throw new IllegalArgumentException("pas une direction");
        }
    }
}
