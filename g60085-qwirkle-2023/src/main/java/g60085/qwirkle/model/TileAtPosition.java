package g60085.qwirkle.model;

/**
 * TileAtPosition represents a tile at any position on the grid;
 *
 * @param row row of the grid;
 * @param col col of the grid;
 * @param tile any tile.
 */
public record TileAtPosition(int row, int col, Tile tile) {
}
