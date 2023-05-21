package g60085.qwirkle.model;

/**
 * TileAtPosition represents a tile at a specific position on the game grid.
 * The row and col parameters represent the row and column indices of the grid where the tile is located.
 * The tile parameter represents the specific tile placed at that position.
 * The TileAtPosition class is implemented as a record, which automatically generates the necessary constructors,
 * getters, equals(), hashCode(), and toString() methods.
 */
public record TileAtPosition(int row, int col, Tile tile) {
}
