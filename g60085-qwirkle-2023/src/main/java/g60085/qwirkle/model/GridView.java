package g60085.qwirkle.model;

/**
 * GridView represents a view on the game grid that provides read-only access to the grid.
 * It allows retrieving tiles from specific positions in the grid and checking if the grid is empty.
 */
public class GridView {
    private Grid grid;

    /**
     * Initializes the GridView with the game grid.
     *
     * @param grid the game grid.
     */
    public GridView(Grid grid) {
        this.grid = grid;
    }

    /**
     * Retrieves the tile at the specified row and column.
     *
     * @param row the row index.
     * @param col the column index.
     * @return the requested tile.
     */
    public Tile get(int row, int col) {
        return this.grid.get(row, col);
    }

    /**
     * Checks if the grid is empty.
     *
     * @return true if the grid is empty, false otherwise.
     */
    public boolean isEmpty() {
        return this.grid == null;
    }
}

