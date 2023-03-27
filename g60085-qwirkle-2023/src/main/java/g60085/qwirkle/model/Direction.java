package g60085.qwirkle.model;

public enum Direction {
    UP(-1, 0),
    DOWN(1,0),
    LEFT0(0,-1),
    RIGHT(0, 1);

    private int deltaRow;
    private int deltaCol;

    //constructeur
    Direction(int deltaRow, int deltaCol) { //il faut donner des valeurs aux énumerations une fois qu'on met un constructeur
        this.deltaRow = deltaRow;
        this.deltaCol = deltaCol;
    }
}
